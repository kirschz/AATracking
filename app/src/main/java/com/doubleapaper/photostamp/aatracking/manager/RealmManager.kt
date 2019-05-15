package com.doubleapaper.photostamp.aatracking.manager


import android.content.Context
import com.doubleapaper.photostamp.aatracking.database.GPSStamp
import com.doubleapaper.photostamp.aatracking.database.LocationTracking
import io.realm.Realm
import io.realm.RealmList

class RealmManager private constructor() {

    private val mContext: Context

    init {
        mContext = Contextor.getInstance().context!!
    }

    companion object {
        private var instance: RealmManager? = null

        fun getInstance(): RealmManager {
            if (instance == null)
                instance = RealmManager()
            return instance!!
        }
    }

    fun inserGPS(gpsStamp: GPSStamp) {

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val gps = realm.createObject(GPSStamp::class.java!!, gpsStamp.timestamp)
        gps.datestamp = gpsStamp.datestamp;
        gps.iMEI = gpsStamp.iMEI;

        var loc = realm.createObject(LocationTracking::class.java)
        loc.lat = gpsStamp.location!!.lat
        loc.lng = gpsStamp.location!!.lng
        gps.location = loc
        realm.commitTransaction()
        realm.close()
    }

    fun getGPSStamp(): List<GPSStamp> {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(GPSStamp::class.java!!)
            .findAll()
        val listResult = realm.copyFromRealm(result)
        realm.close()
        return listResult
    }
}
