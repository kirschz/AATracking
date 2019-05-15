package com.doubleapaper.photostamp.aatracking.database

import io.realm.RealmObject

open class LocationTracking:RealmObject() {
    var lat: Double = 0.toDouble()
    var lng: Double = 0.toDouble()
}