package com.doubleapaper.photostamp.aatracking.background

import android.content.Context

import android.util.Log
import androidx.core.os.bundleOf
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

import com.firebase.jobdispatcher.*

import java.text.DateFormat
import java.util.*

class PhotoUploadWorker(private val mContext: Context, workerParameters: WorkerParameters) :
    Worker(mContext, workerParameters) {

    private var sent_time= "";
    private var date_time= "";
    override fun doWork(): Result {
        val inputData = getInputData()
        val filePath = inputData.getString("imei")
        val outputData = Data.Builder().apply {
            putString("timestamp", DateFormat.getDateTimeInstance().format(Date()))
        }.build()
        val currentDateTime = DateFormat.getDateTimeInstance().format(Date())
        sent_time = ""+(System.currentTimeMillis())
        date_time = currentDateTime


        return try {
            val dispatcher = FirebaseJobDispatcher( GooglePlayDriver(mContext))
            dispatcher.cancel("aaGPSTrackingService")
            val myJob = dispatcher.newJobBuilder()
                .setService(MyService::class.java)
                .setTag("aaGPSTrackingService")
                .setExtras(bundleOf(
                    "imei" to filePath
                ))
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(5, 30))
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build()
            dispatcher.mustSchedule(myJob)
            Result.success()
        } catch (e: Exception) {
            Log.i("joke","error $e.message")
            Result.failure()
        }
    }
}