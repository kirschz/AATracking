package com.doubleapaper.photostamp.aatracking.background


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import android.util.Log


class FirebaseBackgroundService : BroadcastReceiver() {
    private val TAG = "BackgroundService"
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            for (key in intent.getExtras()!!.keySet()) {
                val value = intent.getExtras()!!.get(key)
                Log.e(TAG, "Key: $key Value: $value")
                if (key == "gps") {

                }
            }
        }
    }


}