package com.doubleapaper.photostamp.aatracking.background

import android.content.Context

import android.util.Log
import com.doubleapaper.photostamp.aatracking.NotificationHelper
import com.doubleapaper.photostamp.aatracking.background.GetLocation
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

import java.text.DateFormat
import java.util.*

class MyService : JobService() {

    private lateinit var mContext:Context
    private var sent_time= "";
    private var date_time= "";
    private var imei = ""
    override fun onStopJob(job: JobParameters): Boolean {
        var buddle = job.extras
        if (buddle != null) {
          //  imei = buddle.getString("imei","no")

        }
        Log.i("joke","stop job")
        return false
    }

    override fun onStartJob(job: JobParameters): Boolean {
        var buddle = job.extras
        mContext = applicationContext
        val currentDateTime = DateFormat.getDateTimeInstance().format(Date())
        sent_time = ""+(System.currentTimeMillis())
        date_time = currentDateTime
        if (buddle != null) {
            imei = buddle.getString("imei","no")
        }
        Log.i("joke", "timestamp $currentDateTime "  +imei)

        GetLocation.getInstance().startLocation(date_time, sent_time, imei)
        val notificationHelper = NotificationHelper(mContext)
        notificationHelper.createNotification("Tracking", "Runtime $date_time")

        return true
    }

}