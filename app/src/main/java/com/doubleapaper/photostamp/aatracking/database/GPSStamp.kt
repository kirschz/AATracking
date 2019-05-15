package com.doubleapaper.photostamp.aatracking.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class GPSStamp:RealmObject() {
    var iMEI: String? = null
    var location: LocationTracking? = null
    @PrimaryKey
    var timestamp: Long = 0
    var datestamp: String? = null
}
