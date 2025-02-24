package com.sait.tawajudpremiumplusnewfeatured.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.registerReceiver
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences


class LocationServiceWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        try {
            // Create an instance of the receiver
            val allowedDistance = inputData.getInt("distance", 0)
            val gpsCoordinates = inputData.getString("gpsCoordinates") ?: ""
            val notificationTypeId = inputData.getInt("tawajud_NiotificationsTypesId", 0)
            val minutes = inputData.getString("timer_txt") ?: ""
            val lastTransactionStatus = inputData.getBoolean("Last_transactionStatus", false)
            val carParkTimer = inputData.getBoolean("car_park_timer", false)

            val intent = Intent(applicationContext, LocationJobService::class.java).apply {
                putExtra("distance", allowedDistance)
                putExtra("gpsCoordinates", gpsCoordinates)
                putExtra("tawajud_NiotificationsTypesId", notificationTypeId)
                putExtra("timer_txt", minutes)
                putExtra("Last_transactionStatus", lastTransactionStatus)
                putExtra("car_park_timer", carParkTimer)
            }
            UserShardPrefrences.saveJobData(applicationContext,intent)
            val jobInfo = JobInfo.Builder(
                1,
                ComponentName(applicationContext, LocationJobService::class.java)
            )
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build()

            val jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)
       /*     val allowedDistance = inputData.getInt("distance", 0)
            val gpsCoordinates = inputData.getString("gpsCoordinates") ?: ""
            val notificationTypeId = inputData.getInt("tawajud_NiotificationsTypesId", 0)
            val minutes = inputData.getString("timer_txt") ?: ""
            val lastTransactionStatus = inputData.getBoolean("Last_transactionStatus", false)
            val carParkTimer = inputData.getBoolean("car_park_timer", false)

            val intent = Intent(applicationContext, LocationServiceBackUp::class.java).apply {
                putExtra("distance", allowedDistance)
                putExtra("gpsCoordinates", gpsCoordinates)
                putExtra("tawajud_NiotificationsTypesId", notificationTypeId)
                putExtra("timer_txt", minutes)
                putExtra("Last_transactionStatus", lastTransactionStatus)
                putExtra("car_park_timer", carParkTimer)
            }

            startLocationService(intent)*/

            return Result.success()

        } catch (e: Exception) {
          // setForegroundAsync(createForegroundInfo())
            // Create an instance of the receiver
            val allowedDistance = inputData.getInt("distance", 0)
            val gpsCoordinates = inputData.getString("gpsCoordinates") ?: ""
            val notificationTypeId = inputData.getInt("tawajud_NiotificationsTypesId", 0)
            val minutes = inputData.getString("timer_txt") ?: ""
            val lastTransactionStatus = inputData.getBoolean("Last_transactionStatus", false)
            val carParkTimer = inputData.getBoolean("car_park_timer", false)

            val intent = Intent(applicationContext, LocationJobService::class.java).apply {
                putExtra("distance", allowedDistance)
                putExtra("gpsCoordinates", gpsCoordinates)
                putExtra("tawajud_NiotificationsTypesId", notificationTypeId)
                putExtra("timer_txt", minutes)
                putExtra("Last_transactionStatus", lastTransactionStatus)
                putExtra("car_park_timer", carParkTimer)
            }
            UserShardPrefrences.saveJobData(applicationContext,intent)
            val jobInfo = JobInfo.Builder(
                1,
                ComponentName(applicationContext, LocationJobService::class.java)
            )
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build()

            val jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)
            // UserShardPrefrences.setfirstTimeNotificationAppears(applicationContext,false)
            return Result.failure()
        }


    }





}