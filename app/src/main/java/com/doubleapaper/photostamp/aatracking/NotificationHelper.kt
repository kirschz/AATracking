package com.doubleapaper.photostamp.aatracking

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.*
import java.util.concurrent.TimeUnit

class NotificationHelper {
    private val mContext: Context
    private var mNotificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    val NOTIFICATION_CHANNEL_ID = "19091"

    constructor(mContext: Context) {
        this.mContext = mContext
    }

    fun createNotification(title: String, message: String) {
        /**Creates an explicit intent for an Activity in your app */
        val resultIntent = Intent(mContext, MainActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val resultPendingIntent = PendingIntent.getActivity(
            mContext,
            0 /* Request code */, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        this.mBuilder = NotificationCompat.Builder(mContext)
        this.mBuilder!!.setSmallIcon(R.mipmap.ic_launcher)
        this.mBuilder!!.setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(false)
            .setOngoing(true)
            //.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(resultPendingIntent)

        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance)
            notificationChannel.enableLights(false)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(false)
            // notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert(mNotificationManager != null)
            this.mBuilder!!.setChannelId(NOTIFICATION_CHANNEL_ID)
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
        assert(mNotificationManager != null)
        mNotificationManager!!.notify(1991 /* Request Code */, mBuilder!!.build())
    }

}