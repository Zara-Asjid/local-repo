package com.sait.tawajudpremiumplusnewfeatured

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.sait.tawajudpremiumplusnewfeatured.util.ContinuousTaskManager
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.ensureValidUrlScheme
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.scheduleAppTerminateJob
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class AppLifecycleObserver(private val application: TawajudApplication) : DefaultLifecycleObserver {

    private val handler = Handler(Looper.getMainLooper())
    private val retryInterval: Long = 2000 // Retry interval in milliseconds
    private val maxRetries = 10 // Maximum number of retries
    private var internetRetryCount = 0
    private var serverRetryCount = 0



    @RequiresApi(Build.VERSION_CODES.M)
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        checkAndReconnect()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkAndReconnect() {
        if (!isNetworkAvailable()) {
            handleNoInternetConnection()
        } else {
            retryServerConnection()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun handleNoInternetConnection() {
        Log.d("AppLifecycleObserver", "No internet connection")
        if (internetRetryCount < maxRetries) {
            internetRetryCount++
            handler.postDelayed({ checkAndReconnect() }, retryInterval)
        } else {
            Log.d("AppLifecycleObserver", "Max retries reached for internet connection check.")
            // Optionally handle maximum retry exceeded scenario
        }
    }

    private fun retryServerConnection() {
        val serverUrl = ensureValidUrlScheme(DateTime_Op.removeApiSegment(UserShardPrefrences.getBaseUrl(application))+"/Swagger/index.html")
     //   val serverUrl = "https://sgi.software/TawajudAPIs/api/"

        if(DateTime_Op.removeApiSegment(UserShardPrefrences.getBaseUrl(application))!=null && DateTime_Op.removeApiSegment(UserShardPrefrences.getBaseUrl(application)).isNotEmpty()) {
            if (serverUrl != null) {
                Log.e("serverUrl", serverUrl)
            }
            val client = OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build()
            val request = Request.Builder()
                .url(serverUrl!!)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.isSuccessful) {

                        Log.d("AppLifecycleObserver", "Connected to server.")
                        serverRetryCount = 0
                        // Handle successful connection here


                    } else {
                        Log.d(
                            "AppLifecycleObserver",
                            "Failed to connect to server: ${response.code}"
                        )
                        retryOrHandleServerFailure()
                    }
                }

                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("AppLifecycleObserver", "Failed to connect to server: ${e.message}")
                    retryOrHandleServerFailure()
                }
            })
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.d("AppLifecycleObserver", "App in foreground, starting continuous task.")
        ContinuousTaskManager(application.applicationContext).startContinuousTask()
    }
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.d("AppLifecycleObserver", "App in background, stopping continuous task.")
        ContinuousTaskManager(application.applicationContext).stopContinuousTask()
        // App is in the background, cancel the alarm
        //TawajudApplication.getInstance().cancelAppTerminateAlarm(application.applicationContext)
    }
    private fun retryOrHandleServerFailure() {
        if (serverRetryCount < maxRetries) {
            serverRetryCount++
            handler.postDelayed({ retryServerConnection() }, retryInterval)
        } else {
            Log.d("AppLifecycleObserver", "Max retries reached. Failed to connect to server.")
            // Optionally handle maximum retry exceeded scenario
        }
    }

}




