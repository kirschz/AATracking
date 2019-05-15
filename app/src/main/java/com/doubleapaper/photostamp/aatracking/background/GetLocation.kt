package com.doubleapaper.photostamp.aatracking.background

import android.content.Context
import android.location.Location
import android.util.Log
import com.doubleapaper.photostamp.aatracking.database.GPSStamp
import com.doubleapaper.photostamp.aatracking.database.LocationTracking
import com.doubleapaper.photostamp.aatracking.manager.Contextor
import com.doubleapaper.photostamp.aatracking.manager.RealmManager
import com.google.firebase.database.FirebaseDatabase
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationAccuracy
import io.nlopez.smartlocation.location.config.LocationParams
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider

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

    fun startLocation(dateStamp:String, timestamp:String, imei:String) {
        provider = LocationGooglePlayServicesWithFallbackProvider(mContext)

        val params = LocationParams.Builder()
            .setAccuracy(LocationAccuracy.HIGH)
            .setDistance(1f)
            .setInterval(1000)
            .build()
        val smartLocation = SmartLocation.Builder(mContext).logging(true).build()
        smartLocation.location(provider).config(params).start { location -> showLocation(location, dateStamp, timestamp, imei) }
        // smartLocation.location(provider).config(params).start(mContext.li);
        // smartLocation.activity().start(mContext);

        // Create some geofences
        //GeofenceModel mestalla = new GeofenceModel.Builder("1").setTransition(Geofence.GEOFENCE_TRANSITION_ENTER).setLatitude(39.47453120000001).setLongitude(-0.358065799999963).setRadius(1).build();
        //smartLocation.geofencing().add(mestalla).start(this);
    }
    private fun showLocation(location: Location?, dateStamp:String, timestamp:String, imei:String) {

        if (location != null) {
            var gps = GPSStamp()
            gps.datestamp = dateStamp
            gps.timestamp = java.lang.Long.parseLong(timestamp)
            var  loc = LocationTracking()
            loc.lat = location.latitude
            loc.lng = location.longitude
            gps.location = loc
            gps.iMEI=imei
            RealmManager.getInstance().inserGPS(gps)
            val database = FirebaseDatabase.getInstance()
            val myLocation = database.getReference("Location")
            val myRef = myLocation.child("imei-$imei")
            val myTime = myRef.child(timestamp)
            val myGPs = myTime.child("GPS")
            val myMessage = myTime.child("message")
            myGPs.setValue(location.latitude.toString() + ", " + location.longitude)
            myMessage.setValue(dateStamp)

            Log.i("joke", "showLocation " + location!!.latitude + ", " + location!!.longitude )
            SmartLocation.with(mContext).location().stop()
            // val mBuilder = NotificationCompat.Builder(applicationContext)
            // mBuilder.setOngoing(true)
        } else {
            Log.i("joke", "showLocation null")
        }
    }
}