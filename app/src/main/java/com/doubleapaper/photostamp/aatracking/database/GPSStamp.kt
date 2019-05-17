package com.doubleapaper.photostamp.aatracking.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class GPSStamp:RealmObject() {
    var iMEI: String? = null
    var location: LocationTracking? = null
    @PrimaryKey
    var timestamp: Long = 0
    var datestamp: String? = null
    var address:String? = null
    var sendResult:String? =null
    var fromService:String? =null
    override fun toString(): String {
        return "GPSStamp(iMEI=$iMEI, location=${location.toString()}, timestamp=$timestamp, datestamp=$datestamp, address=$address, sendResult=$sendResult)"
    }
}
