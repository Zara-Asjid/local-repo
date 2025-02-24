package com.sait.tawajudpremiumplusnewfeatured

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.text.SimpleDateFormat
import java.util.Locale

class LocationServiceBackUp : Service() {


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

    override fun onCreate() {
        super.onCreate()
        startForeground(notificationCounter, getNotification("", ""))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        distanceValue = intent?.getIntExtra("distance", 0) ?: 0
        gpsCoordinatesStr = intent?.getStringExtra("gpsCoordinates")
        notificationId = intent?.getIntExtra("tawajud_NiotificationsTypesId", 0) ?: 0
        timerText = intent?.getStringExtra("timer_txt")
        lastTransactionStatus = intent?.getBooleanExtra("Last_transactionStatus", false)
        punchIntimeDuration = intent?.getIntExtra("punch_in_timeDuration", 0)
        car_park_timer = intent?.getBooleanExtra("car_park_timer", false)
        if (gpsCoordinatesStr != null) {
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

        return START_STICKY
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
                updateForegroundNotification(notificationData.notificationEnglishTitle, notificationData.templateEnglish.replace("{Time}", timerText.toString()))
            } else {
                updateForegroundNotification(notificationData.notificationArabicTitle, notificationData.templateArabic.replace("{Time}", timerText.toString()))
            }
            UserShardPrefrences.setisInside(this, true)
        } else if (notificationId == 0 && UserShardPrefrences.getNotificationData(this)[0].isActive && !lastTransactionStatus!!) {
            val notificationData = UserShardPrefrences.getNotificationData(this)[0]
            if (UserShardPrefrences.getLanguage(this) == "0") {
                updateForegroundNotification(notificationData.notificationEnglishTitle, notificationData.templateEnglish.replace("{Time}", timerText.toString()))
            } else {
                updateForegroundNotification(notificationData.notificationArabicTitle, notificationData.templateArabic.replace("{Time}", timerText.toString()))
            }
            UserShardPrefrences.setisInside(this, true)
        }
    }
    private fun updateForegroundNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = getNotification(title, message)
        notificationManager.notify(notificationCounter++, notification)
        /*   val currentTime = System.currentTimeMillis()
           val oneMinuteInMillis = 60 * 1000

           // Check if the last notification was sent more than one minute ago
           if (currentTime - lastNotificationTime >= oneMinuteInMillis) {
               val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
               val notification = getNotification(title, message)
               notificationManager.notify(notificationCounter++, notification)
               lastNotificationTime = currentTime // Update the last notification time
           }*/
    }
    private fun sendNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "location_channel_id"
        val channelName = "Location Channel"

        // Create an intent to launch the main activity
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)  // Set the intent that will fire when the user taps the notification
            .setAutoCancel(true)  // Automatically removes the notification when the user taps it
            .build()

        notificationManager.notify(notificationCounter, notification)
    }

    private fun getNotification(title: String, message: String): Notification {
        val channelId = "foreground_service_channel"
        val channelName = "Foreground Service Channel"

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "home_fragment")
        }
        val pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW).apply {
                description = "Channel for foreground service notifications"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.app_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}






