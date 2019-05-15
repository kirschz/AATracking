package com.doubleapaper.photostamp.aatracking

import android.app.Application
import com.doubleapaper.photostamp.aatracking.manager.Contextor
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Contextor.getInstance().init(applicationContext)
        if (FirebaseApp.getApps(this).isNotEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("aatracking.realm")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.getInstance(config)
        Realm.setDefaultConfiguration(config)
    }

}