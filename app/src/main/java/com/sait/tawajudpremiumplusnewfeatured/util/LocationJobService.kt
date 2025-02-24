package com.sait.tawajudpremiumplusnewfeatured.util

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

import java.text.SimpleDateFormat
import java.util.Locale

class LocationJobService: JobService() {
    private var gpsCoordinatesStr: String? = null
    private val targetLocation = Location("")

    // Flags and counters for notifications
    private var enteredNotificationShown = false
    private var exitNotificationCount = 0
    private val maxExitNotifications = 5
    private var distanceValue: Int = 0
    private var notificationId: Int = 0

    private var isNotificationShown = false
    private var timerText: String? = null
    private var lastTransactionStatus: Boolean? = false
    private var punchIntimeDuration: Int? = 0
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var car_park_timer: Boolean? = false

    // Timing control for notifications
    private var lastNotificationTime: Long = 0
    private val notificationInterval = 60 * 1000 // 1 minute in milliseconds
    private var notificationCounter: Int = 1 // Start with a non-zero ID

    override fun onStartJob(params: JobParameters?): Boolean {

        // Extract data from the intent
        val intent = Intent().apply {
            for ((key, value) in UserShardPrefrences.getJobData(applicationContext)!!) {
                when (value) {
                    is Int -> putExtra(key, value)
                    is String -> putExtra(key, value)
                    is Boolean -> putExtra(key, value)
                    // Add other types if needed
                }
            }
        }
        distanceValue = intent.getIntExtra("distance", 0) ?: 0
        gpsCoordinatesStr = intent.getStringExtra("gpsCoordinates")
        notificationId = intent.getIntExtra("tawajud_NiotificationsTypesId", 0) ?: 0
        timerText = intent.getStringExtra("timer_txt")
        lastTransactionStatus = intent.getBooleanExtra("Last_transactionStatus", false)
        punchIntimeDuration = intent.getIntExtra("punch_in_timeDuration", 0)
        car_park_timer = intent.getBooleanExtra("car_park_timer", false)

        if (gpsCoordinatesStr != null && gpsCoordinatesStr!!.isNotEmpty()) {
            Log.e("gpsCoordinatesStr", gpsCoordinatesStr.toString())
            val cleanedString = gpsCoordinatesStr!!
                .replace("[", "")
                .replace("]", "")

            val gpsCoordinates = cleanedString
                .split(",")
                .map { it.trim().toDouble() }
            targetLocation.latitude = gpsCoordinates[0]
            targetLocation.longitude = gpsCoordinates[1]
        }
        checkDistanceFromLocation()

//        BackgroundTask().execute(params)


       // this.jobFinished(params,true)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    private fun checkDistanceFromLocation() {
        // Check if car_park_timer is true

        if (car_park_timer == true) {
            carParkTimer()
        }


    }

    private fun carParkTimer() {
        if (notificationId == 0 && UserShardPrefrences.getNotificationData(this)[3].isActive && !lastTransactionStatus!!) {
            val notificationData = UserShardPrefrences.getNotificationData(this)[0]
            if (UserShardPrefrences.getLanguage(this) == "0") {
                updateForegroundNotification(
                    notificationData.notificationEnglishTitle,
                    notificationData.templateEnglish.replace("{Time}", timerText.toString())
                )
            } else {
                updateForegroundNotification(
                    notificationData.notificationArabicTitle,
                    notificationData.templateArabic.replace("12:00", timerText.toString())
                )
            }
            UserShardPrefrences.setisInside(this, true)
        } else if (notificationId == 0 && UserShardPrefrences.getNotificationData(this)[0].isActive && !lastTransactionStatus!!) {
            val notificationData = UserShardPrefrences.getNotificationData(this)[0]
            if (UserShardPrefrences.getLanguage(this) == "0") {
                updateForegroundNotification(
                    notificationData.notificationEnglishTitle,
                    notificationData.templateEnglish.replace("{Time}", timerText.toString())
                )
            } else {
                updateForegroundNotification(
                    notificationData.notificationArabicTitle,
                    notificationData.templateArabic.replace("12:00", timerText.toString())
                )
            }
            UserShardPrefrences.setisInside(this, true)
        }
    }

    private fun updateForegroundNotification(title: String, message: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = getNotification(title, message)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)

    }

    private fun getNotification(title: String, message: String): Notification {
        val channelId = "job_scheduler_channel"
        val channelName = "Job Scheduler Channel"

        // Create notification channel if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for job scheduler notifications"
                enableLights(false)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "home_fragment")
        }
        // Generate a unique notification ID using a counter or timestamp
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.app_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Automatically removes the notification when the user taps it
            .build()
// Use a unique ID for each notification
        return notification
    }

    private inner class BackgroundTask : AsyncTask<JobParameters, Void, Void>() {
        override fun doInBackground(vararg params: JobParameters?): Void? {
            checkDistanceFromLocation()
          /*  if (Looper.getMainLooper().isCurrentThread) {
                Log.d("LocationJobService", "Running on the main thread")
            } else {
                Log.d("LocationJobService", "Not running on the main thread")
            }*/
            return null
        }

        override fun onPostExecute(result: Void?) {
            // Task completed
        }
    }
}