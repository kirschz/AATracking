package com.doubleapaper.photostamp.aatracking

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.work.*
import com.doubleapaper.photostamp.aatracking.manager.RealmManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.TimeUnit
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.doubleapaper.photostamp.aatracking.background.PhotoUploadWorker
import com.doubleapaper.photostamp.aatracking.dao.SaveMobile
import com.doubleapaper.photostamp.aatracking.dao.ServiceResponse
import com.doubleapaper.photostamp.aatracking.database.GPSStamp
import com.doubleapaper.photostamp.aatracking.fragment.LoginFragment
import com.doubleapaper.photostamp.aatracking.fragment.MainFragment
import com.doubleapaper.photostamp.aatracking.manager.PrefManage
import com.doubleapaper.photostamp.aatracking.service.CallService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.nlopez.smartlocation.SmartLocation
import io.realm.Case
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private var fragment:Fragment = LoginFragment::class.java.newInstance()
    private lateinit var manager:FragmentManager
    private lateinit var userAction:String
    private lateinit var saveMobile: SaveMobile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            val remoteConfig = FirebaseRemoteConfig.getInstance()
                            val configSettings = FirebaseRemoteConfigSettings.Builder()
                                .setMinimumFetchIntervalInSeconds(4200)
                                .build()
                            remoteConfig.setConfigSettings(configSettings)
                            remoteConfig.setDefaults(R.xml.remote_config_defaults)
                            remoteConfig.fetch(4200)
                                .addOnCompleteListener(OnCompleteListener<Void> { task ->
                                    if (task.isSuccessful) {
                                        remoteConfig.activateFetched()
                                    } else {

                                    }
                                    if (!remoteConfig.getBoolean("use_app")) {
                                        Toast.makeText(this@MainActivity, "Can not Use App", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                })
                        } else {
                            finish()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }

                }).check()
        }
        val s1:String
        val tel = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            s1= "imeino"
        }
        else
            s1 = tel.deviceId
        userAction = "OPEN"

        PrefManage.getInstance().setIMEI(s1)
        var UserID = PrefManage.getInstance().getUserName()

        var locationState = SmartLocation.with(applicationContext).location().state()
        var token =""
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("joke", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result?.token.toString()
                saveMobile = SaveMobile(SimpleDateFormat("yyyy-MM-dd").format(Date()),
                    Build.VERSION.SDK_INT,
                    BuildConfig.VERSION_NAME,
                    s1, locationState.isAnyProviderAvailable(),
                    locationState.isNetworkAvailable(),
                    locationState.isPassiveAvailable(),
                    locationState.isGpsAvailable(),
                    locationState.locationServicesEnabled(),
                    Build.MODEL,token,userAction,UserID)
                sendDataToServer(saveMobile)
            })

        val data = Data.Builder()
            .putString("imei", s1)
            .build()
        val periodicRequest = PeriodicWorkRequest.Builder(PhotoUploadWorker::class.java,15, TimeUnit.MINUTES)
            .setInputData(data)
            .addTag("aatracking")
            .build()
        fab.setOnClickListener { view ->
            if (PrefManage.getInstance().getUserName() != "") {
                startWorker(periodicRequest)
                saveMobile = SaveMobile(SimpleDateFormat("yyyy-MM-dd").format(Date()),
                    Build.VERSION.SDK_INT,
                    BuildConfig.VERSION_NAME,
                    s1, locationState.isAnyProviderAvailable(),
                    locationState.isNetworkAvailable(),
                    locationState.isPassiveAvailable(),
                    locationState.isGpsAvailable(),
                    locationState.locationServicesEnabled(),
                    Build.MODEL,token,"START",PrefManage.getInstance().getUserName())
                sendDataToServer(saveMobile)
                NotificationHelper(applicationContext).createNotification("Start", "Now")
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                finish()
            } else  Toast.makeText(this, "Input User", Toast.LENGTH_SHORT).show()
        }

        fragment.arguments =  bundleOf(
            "userAction" to userAction
        )


        WorkManager.getInstance().enqueue(periodicRequest)
        // WorkManager.getInstance().getWorkInfoById(periodicRequest.id).
        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicRequest.id).observe(this, Observer {
            if (it != null && it.state == WorkInfo.State.SUCCEEDED) {
                val url = it.outputData.getString("imei")
            }

        })

        manager = supportFragmentManager
        manager.beginTransaction().replace(R.id.contentContainer, fragment).commit()
    }
    private fun startWorker( periodicRequest:PeriodicWorkRequest){

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)

       /* val oneTimeRequest = OneTimeWorkRequest.Builder(PhotoUploadWorker::class.java)
            .setInputData(data)
            .setConstraints(constraints.build())
            .addTag("demo")
            .build()*/
        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicRequest.id).observe(this, Observer {
            if (it != null ) {
                WorkManager.getInstance().cancelWorkById(periodicRequest.id)
                Log.i("joke","stop work " + periodicRequest.id)
            }
        })
       Toast.makeText(this, "Starting worker", Toast.LENGTH_SHORT).show()
       WorkManager.getInstance().enqueueUniquePeriodicWork("DAA1991", ExistingPeriodicWorkPolicy.KEEP, periodicRequest)

    }
    fun sendDataToServer(saveMobile: SaveMobile) {
        var server: CallService.SaveMobileDataService =  CallService().retrofit.create(CallService.SaveMobileDataService::class.java)

        val call = server.GetTruckActivityActive(saveMobile)
        call.enqueue(object : Callback<ServiceResponse> {
            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {
                if (response.isSuccessful()) {
                    var result = response.body()
                    //RealmManager.getInstance().updateGPSStamp(gps,result?.status!!)
                }
            }

            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                Log.i("joke","Throwable" + t.message)
            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_map -> fragment = MapsActivity::class.java.newInstance()
            R.id.action_settings -> fragment = MainFragment::class.java.newInstance()
            else -> super.onOptionsItemSelected(item)
        }
        fragment.arguments
        manager = supportFragmentManager
        manager.beginTransaction().replace(R.id.contentContainer, fragment).commit()
        return true
    }
}
