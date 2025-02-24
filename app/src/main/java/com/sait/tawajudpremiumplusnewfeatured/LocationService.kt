package com.sait.tawajudpremiumplusnewfeatured

import android.annotation.SuppressLint
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
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.sait.tawajudpremiumplusnewfeatured.util.CheckInListener
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import getCurrentTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

import androidx.core.content.ContextCompat

import java.util.*

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var distanceValue: Int = 0
    private var notificationId: Int = 0
    private var gpsCoordinatesStr: String? = null
    private val targetLocation = Location("")
    private var isNotificationShown = false
    private var timerText: String? = null
    private var lastTransactionStatus: Boolean? = false
    private var punchIntimeDuration: Int? = 0
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var car_park_timer: Boolean? = false
    private var lastCheckIn: Date? = null

    private var lastDistance: Float = Float.MAX_VALUE // Track the last known distance
    private var lastNotificationTime: Long = 0L

    private var notificationCounter: Int = 1 // Start with a non-zero ID

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
     //   lastNotificationTime = intent?.getLongExtra("lastNotificationTime", 0L) ?: 0L
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

        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    checkDistanceFromLocation(location)
                }
            }
        }

        startLocationUpdates()

        return START_STICKY
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    private fun checkDistanceFromLocation(location: Location) {
        val distance = location.distanceTo(targetLocation)
        val currentlyInside = distance < distanceValue

        // Check if car_park_timer is not null and false
      /*  if (car_park_timer != true) {
            if (currentlyInside && (lastDistance == Float.MAX_VALUE || lastDistance >= distanceValue)) {
                if (!UserShardPrefrences.getisInside(this)) {
                    updateForegroundNotification(
                        "Entered specific location",
                        "You have entered your work location radius."
                    )
                    UserShardPrefrences.setisInside(this, true)
                }
            } else if (!currentlyInside && lastDistance < distanceValue) {
                updateForegroundNotification(
                    "Exited specific location",
                    "You have left the work location radius."
                )
                UserShardPrefrences.setisInside(this, false)
            } else if (currentlyInside && lastTransactionStatus == false && notificationId == 1 && UserShardPrefrences.getNotificationData(this)[0].isActive) {
                val notificationData = UserShardPrefrences.getNotificationData(this)[0]
                val language = UserShardPrefrences.getLanguage(this)
                if (language == "0") {
                    updateForegroundNotification(
                        notificationData.notificationEnglishTitle,
                        notificationData.templateEnglish.replace("{Time}", timerText.toString())
                    )
                } else {
                    updateForegroundNotification(
                        notificationData.notificationArabicTitle,
                        notificationData.templateArabic
                    )
                }
            }
        }*/

        // Check if car_park_timer is true
        if (car_park_timer == true) {
            carParkTimer(currentlyInside)
        }

        lastDistance = distance
    }

    private fun carParkTimer(currentlyInside: Boolean) {
        if (currentlyInside && notificationId == 0 && UserShardPrefrences.getNotificationData(this)[3].isActive && !lastTransactionStatus!!) {
            val notificationData = UserShardPrefrences.getNotificationData(this)[0]
            if (UserShardPrefrences.getLanguage(this) == "0") {
                updateForegroundNotification(notificationData.notificationEnglishTitle, notificationData.templateEnglish.replace("{Time}", timerText.toString()))
            } else {
                updateForegroundNotification(notificationData.notificationArabicTitle, notificationData.templateArabic.replace("{Time}", timerText.toString()))
            }
            UserShardPrefrences.setisInside(this, true)
        } else if (currentlyInside && notificationId == 0 && UserShardPrefrences.getNotificationData(this)[0].isActive && !lastTransactionStatus!!) {
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









