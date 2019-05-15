package com.doubleapaper.photostamp.aatracking

import android.Manifest
import android.content.Context
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.doubleapaper.photostamp.aatracking.background.PhotoUploadWorker


class MainActivity : AppCompatActivity() {

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
        var gps = RealmManager.getInstance().getGPSStamp()
        var location:String = ""
        var i = 0
        for (l in gps){
            i++
            if (i > gps.size - 20)
            location +=  l.datestamp +" | " + l.location!!.lat +", " +l.location!!.lng + "\n"
        }
        val s1:String
        val tel = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            s1= "imeino"
        }
        else
        s1 = tel.deviceId

        text1.text =location
        val data = Data.Builder()
            .putString("imei", s1)
            .build()
        val periodicRequest = PeriodicWorkRequest.Builder(PhotoUploadWorker::class.java,15, TimeUnit.MINUTES)
            .setInputData(data)
            .addTag("aatracking")
            .build()
        fab.setOnClickListener { view ->
            startWorker(periodicRequest)
            NotificationHelper(applicationContext).createNotification("title","message")
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            finish()
        }

        WorkManager.getInstance().enqueue(periodicRequest)
        // WorkManager.getInstance().getWorkInfoById(periodicRequest.id).
        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicRequest.id).observe(this, Observer {
            if (it != null && it.state == WorkInfo.State.SUCCEEDED) {
                val url = it.outputData.getString("imei")
                Log.i("joke","imei : $url")
            }
            Log.i("joke","state : " + it.state)
        })
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
