package com.sait.tawajudpremiumplusnewfeatured

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables

class ScreenReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val appContext = context?.applicationContext ?: return

        when (intent?.action) {
            Intent.ACTION_SCREEN_ON -> {
                Log.e("background_app", "Screen ON")
                GlobalVariables.from_background = true

                // Handle screen on logic here
            }
            Intent.ACTION_SCREEN_OFF -> {
                Log.e("background_app", "Screen OFF")
                GlobalVariables.from_background = false

                // Handle screen off logic here
            }
            Intent.ACTION_USER_PRESENT -> {
                Log.e("background_app", "User Present (unlocked)")
                // Handle screen unlock logic here
            }
        }
    }
}

