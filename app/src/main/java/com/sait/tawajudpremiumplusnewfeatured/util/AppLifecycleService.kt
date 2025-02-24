package com.sait.tawajudpremiumplusnewfeatured.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class AppLifecycleService: Service() {


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)


        Log.d("AppLifecycleService", "App was swiped away from recent apps.")
        //stopSelf()
        Log.d("AppLifecycleService", "Service stopped successfully.")

    }

    private fun sendAppShutdownNotificationToServer() {
        // Example: Notify your server that the app was removed
        // Replace with your actual API call and logic
        Log.d("AppLifecycleService", "Sending shutdown notification to server...")
        // For example, use Retrofit to notify the server here
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}