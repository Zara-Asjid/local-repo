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
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.sait.tawajudpremiumplusnewfeatured.util.CheckInListener
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import getCurrentTime

class LocationServiceCarParkTimer : Service(){

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationUpdateHandler: Handler
    private var distanceValue: Int = 0
    private var notificationId: Int= 0
    private var gpsCoordinatesStr: String? = null
    private val targetLocation = Location("")
    private var isNotificationShown = false
    private var timerText : String=""
    private var lastTransactionStatus : Boolean? = false
    private var lastDistance: Float = Float.MAX_VALUE // Track the last known distance

    // private var isInside: Boolean = false
    private var checkInListener: CheckInListener? = null

    companion object {
        const val ACTION_CHECK_IN = "com.sait.tawajudpremiumplusnewfeatured.ACTION_CHECK_IN"
    }
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startForeground(3, getNotification("",""))

    }

    private fun showNotificationOnce() {
        if (!isNotificationShown) {
            if (UserShardPrefrences.getLanguage(this)!! == "0") {
                showSeparateNotification(
                    UserShardPrefrences.getNotificationData(this)[0].notificationEnglishTitle,
                    UserShardPrefrences.getNotificationData(this)[0].templateEnglish
                )
            } else {
                showSeparateNotification(
                    UserShardPrefrences.getNotificationData(this)[0].notificationArabicTitle,
                    UserShardPrefrences.getNotificationData(this)[0].templateArabic
                )
            }
            isNotificationShown = true
        }
    }
    /*private fun checkNotificationPerm(){
        val notificationManager = NotificationManagerCompat.from(this)
        if (!notificationManager.areNotificationsEnabled()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
               *//* val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(settingsIntent)*//*
            ActivityCompat.requestPermissions(
                MainActivity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION
            )
            return
        }

        // Stop the service until notifications are enabled
        stopSelf()

    }
}*/
   private fun extractLatLong(locationString: String): Pair<Double, Double>? {
        val regex = """Location\[\s*([-+]?[0-9]*\.?[0-9]+),\s*([-+]?[0-9]*\.?[0-9]+)""".toRegex()
        val matchResult = regex.find(locationString)
        return if (matchResult != null) {
            val (latitude, longitude) = matchResult.destructured
            Pair(latitude.toDouble(), longitude.toDouble())
        } else {
            null
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start the service as a foreground service immediately
     //   startForeground(1, getNotification("Location Service", "Running..."))
        // Handle intent actions
        distanceValue = intent?.getIntExtra("distance", 0) ?: 0
        gpsCoordinatesStr = intent?.getStringExtra("gpsCoordinates")
        notificationId = intent?.getIntExtra("tawajud_NiotificationsTypesId",0)?: 0
        timerText= intent?.getStringExtra("timer_txt").toString()
        lastTransactionStatus= intent?.getBooleanExtra("Last_transactionStatus",false)

        /*  targetLocation.latitude =  33.6901273
          targetLocation.longitude = 73.0424758*/

        if (gpsCoordinatesStr != null) {
            val gpsCoordinates = extractLatLong(gpsCoordinatesStr.toString()).toString().trim().removeSurrounding("(", ")").split(",").map { it.toDouble() }
            targetLocation.latitude = gpsCoordinates[0]
            targetLocation.longitude = gpsCoordinates[1]
        }
        else {
            targetLocation.latitude = 24.4973959
            targetLocation.longitude = 54.3261002
        }

        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        locationCallback = object : LocationCallback() {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    checkDistanceFromLocation(location)
                }
            }
        }
        locationUpdateHandler = Handler(Looper.getMainLooper())

        startLocationUpdates()

        return START_STICKY
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    private fun checkDistanceFromLocation(location: Location) {
        val distance = location.distanceTo(targetLocation)
         val currentlyInside = distance < distanceValue


        if(currentlyInside){
            carParkTimer(currentlyInside)
        }



        lastDistance = distance

        /*    if (!currentlyInside && notificationId==0 &&
                UserShardPrefrences.getCheckInStatus(this) &&
                UserShardPrefrences.getNotificationData(this)[3].isActive&& !lastTransactionStatus!!
            ) {
                updateForegroundNotification(
                    "Exited specific location",
                    "You have left the work location radius."
                )
                UserShardPrefrences.setisInside(this, false)
            }*/

    }



    private fun carParkTimer(currentlyInside: Boolean) {
        if (currentlyInside && notificationId==0 &&
            !UserShardPrefrences.getCheckInStatus(this) &&
            UserShardPrefrences.getNotificationData(this)[3].isActive&& !lastTransactionStatus!!
        ){
            if(UserShardPrefrences.getLanguage(this)!! == "0"){
                updateForegroundNotification(
                    UserShardPrefrences.getNotificationData(this)[3].notificationEnglishTitle,
                    UserShardPrefrences.getNotificationData(this)[3].templateEnglish
                )
            }else
            {
                updateForegroundNotification(
                    UserShardPrefrences.getNotificationData(this)[3].notificationArabicTitle,
                    UserShardPrefrences.getNotificationData(this)[3].templateArabic
                )
            }
            // triggerCheckIn()
            UserShardPrefrences.setisInside(this, true)
        } else
            if (currentlyInside && notificationId==0 &&
                !UserShardPrefrences.getCheckInStatus(this) &&
                UserShardPrefrences.getNotificationData(this)[3].isActive&& !lastTransactionStatus!!
            ){
                if(UserShardPrefrences.getLanguage(this)!! == "0"){
                    updateForegroundNotification(
                        UserShardPrefrences.getNotificationData(this)[3].notificationEnglishTitle,
                        UserShardPrefrences.getNotificationData(this)[3].templateEnglish
                    )
                }else
                {
                    updateForegroundNotification(
                        UserShardPrefrences.getNotificationData(this)[3].notificationArabicTitle,
                        UserShardPrefrences.getNotificationData(this)[3].templateArabic
                    )
                }
                // triggerCheckIn()
                UserShardPrefrences.setisInside(this, true)
            }
    }



    private fun updateForegroundNotification(title: String, message: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = getNotification(title, message)
        notificationManager.notify(3, notification)
    }
    private fun showSeparateNotification(title: String, message: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val formattedMessage = message.replace("{Time}", getCurrentTime())
        val notification = getNotification(title, formattedMessage)
        val uniqueNotificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(uniqueNotificationId, notification)
    }
    private fun getNotification(title: String, message: String): Notification {
        val channelId = "foreground_service_channel_new"
        val channelName = "Foreground Service Channel_new"
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW).apply {
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
            .setContentIntent(pendingIntent)  // Set the intent that will fire when the user taps the notification
            .setAutoCancel(true)  // Automatically removes the notification when the user taps it
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // Ensure the priority is high
            .setDefaults(NotificationCompat.DEFAULT_ALL)  // Set default sound, vibration, and lights
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}




