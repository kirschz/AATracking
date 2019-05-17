package com.doubleapaper.photostamp.aatracking.background


import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.TelephonyManager

import android.util.Log
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.*


class FirebaseBackgroundService : BroadcastReceiver() {
    private val TAG = "BackgroundService"
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            for (key in intent.getExtras()!!.keySet()) {
                val value = intent.getExtras()!!.get(key)
                Log.e(TAG, "Key: $key Value: $value")
                if (key == "gps") {
                    val imei:String
                    val tel = context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        imei= "imeino"
                    }
                    else
                        imei = tel.deviceId
                    var currentDateTime =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                    var sent_time = ""+(System.currentTimeMillis())

                    GetLocation.getInstance().startLocation(currentDateTime, sent_time, imei,"notification")
                }

            }
        }
    }

}