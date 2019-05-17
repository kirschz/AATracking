package com.doubleapaper.photostamp.aatracking.manager


import android.content.Context
import com.doubleapaper.photostamp.aatracking.database.GPSStamp
import com.doubleapaper.photostamp.aatracking.database.LocationTracking
import io.realm.Case
import io.realm.Realm
import io.realm.RealmList
import io.realm.Sort

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
        gps.address = gpsStamp.address
        gps.fromService = gpsStamp.fromService
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
    fun getGPSStampByField(fieldName: String, fieldValue: String, sort: Sort = Sort.ASCENDING): List<GPSStamp> {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(GPSStamp::class.java!!)
            .like(fieldName, fieldValue, Case.INSENSITIVE)
            .sort(fieldName,sort)
            .findAll()
        val listResult = realm.copyFromRealm(result)
        realm.close()
        return listResult
    }
    fun updateGPSStamp (gpsStamp: GPSStamp, sendResult:String){
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val toEdit = realm.where(GPSStamp::class.java)
            .equalTo("timestamp", gpsStamp.timestamp).findFirst()
        toEdit!!.sendResult =sendResult
        realm.commitTransaction()
        realm.close()
    }
}
