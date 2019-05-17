package com.doubleapaper.photostamp.aatracking.background

import android.content.Context
import android.location.Address
import android.location.Location
import android.text.TextUtils
import android.util.Log
import com.doubleapaper.photostamp.aatracking.dao.SaveMobile
import com.doubleapaper.photostamp.aatracking.dao.SaveTracking
import com.doubleapaper.photostamp.aatracking.dao.ServiceResponse
import com.doubleapaper.photostamp.aatracking.database.GPSStamp
import com.doubleapaper.photostamp.aatracking.database.LocationTracking
import com.doubleapaper.photostamp.aatracking.manager.Contextor
import com.doubleapaper.photostamp.aatracking.manager.PrefManage
import com.doubleapaper.photostamp.aatracking.manager.RealmManager
import com.doubleapaper.photostamp.aatracking.service.CallService
import com.google.firebase.database.FirebaseDatabase
import io.nlopez.smartlocation.BuildConfig
import io.nlopez.smartlocation.OnReverseGeocodingListener
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationAccuracy
import io.nlopez.smartlocation.location.config.LocationParams
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.ArrayList

class GetLocation  private constructor() {
    private val mContext: Context
    private var provider: LocationGooglePlayServicesWithFallbackProvider? = null
    init {
        mContext = Contextor.getInstance().context!!
    }

    companion object {
        private var instance: GetLocation? = null

        fun getInstance(): GetLocation {
            if (instance == null)
                instance =
                    GetLocation()
            return instance!!
        }
    }

    fun startLocation(dateStamp:String, timestamp:String, imei:String, from:String) {
        provider = LocationGooglePlayServicesWithFallbackProvider(mContext)

        val params = LocationParams.Builder()
            .setAccuracy(LocationAccuracy.HIGH)
            .setDistance(1f)
            .setInterval(1000)
            .build()
        val smartLocation = SmartLocation.Builder(mContext).logging(true).build()
        smartLocation.location(provider).config(params).start { location -> showLocation(location, dateStamp, timestamp, imei, from) }
    }
    private fun showLocation(location: Location?, dateStamp:String, timestamp:String, imei:String, from:String) {
        var address =""
        if (location != null) {


            var gps = GPSStamp()
            gps.datestamp = dateStamp
            gps.timestamp = java.lang.Long.parseLong(timestamp)
            var  loc = LocationTracking()
            loc.lat = location.latitude
            loc.lng = location.longitude
            gps.location = loc
            gps.iMEI=imei
            gps.address = address
            gps.fromService = from
            RealmManager.getInstance().inserGPS(gps)

            val database = FirebaseDatabase.getInstance()
            val myLocation = database.getReference("Location")
            val myRef = myLocation.child("imei-$imei")
            val myTime = myRef.child(timestamp)
            val myGPs = myTime.child("GPS")
            val myMessage = myTime.child("message")
            val myAddress = myTime.child("address")
            myGPs.setValue(location.latitude.toString() + ", " + location.longitude)
            myMessage.setValue(dateStamp)
            myAddress.setValue(address + from)
            var tracking = SaveTracking(address,
                com.doubleapaper.photostamp.aatracking.BuildConfig.VERSION_NAME,dateStamp,from,imei,location.latitude, location.longitude,"" , java.lang.Long.parseLong(timestamp),PrefManage.getInstance().getUserName())

            if (from == "foreground"){
                try {
                    SmartLocation.with(mContext).geocoding().reverse(
                        location
                    ) { original, results ->
                        if (results.size > 0) {
                            val result = results[0]
                            val builder = StringBuilder()
                            builder.append("")
                            val addressElements = ArrayList<String>()
                            for (i in 0..result.maxAddressLineIndex) {
                                addressElements.add(result.getAddressLine(i))
                            }
                            builder.append(TextUtils.join(", ", addressElements))
                            address = builder.toString()
                            tracking.addressName = address
                            sendDataToServer(tracking,gps)
                        }else {
                            sendDataToServer(tracking,gps)
                        }
                    }
                }catch (e:Exception){
                    address = "Error:${e.message}"
                }
            }else {
                sendDataToServer(tracking,gps)
            }


            SmartLocation.with(mContext).location().stop()
            // val mBuilder = NotificationCompat.Builder(applicationContext)
            // mBuilder.setOngoing(true)
        } else {
            Log.i("joke", "showLocation null")
        }
    }
    fun sendDataToServer(saveTracking: SaveTracking, gps:GPSStamp) {
        var server: CallService.SaveTrackingDataService =  CallService().retrofit.create(CallService.SaveTrackingDataService::class.java)

        val call = server.SaveTrackingData(saveTracking)
        call.enqueue(object : Callback<ServiceResponse> {
            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {
                if (response.isSuccessful()) {
                    var result = response.body()
                    RealmManager.getInstance().updateGPSStamp(gps,result?.status!!)
                }
            }

            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                Log.i("joke","Throwable" + t.message)
            }
        })

    }
}