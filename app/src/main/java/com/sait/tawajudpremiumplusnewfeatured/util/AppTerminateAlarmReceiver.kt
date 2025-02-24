package com.sait.tawajudpremiumplusnewfeatured.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppTerminateAlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmManager", "Alarm triggered!")

        // Your task logic goes here
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("AlarmManager", "Executing the scheduled task")

            // Reschedule the alarm for 30 minutes later
            TawajudApplication.getInstance().scheduleAppTerminateAlarm(context)
        }
    }
}