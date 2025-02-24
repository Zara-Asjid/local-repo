package com.sait.tawajudpremiumplusnewfeatured

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import com.pixplicity.easyprefs.library.Prefs
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.util.AppTerminateAlarmReceiver
import com.sait.tawajudpremiumplusnewfeatured.util.AppTerminateJobService
import com.sait.tawajudpremiumplusnewfeatured.util.Const
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.from_background
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils
import java.util.Date
import java.util.concurrent.TimeUnit


class TawajudApplication : Application(),Configuration.Provider {
    private lateinit var screenReceiver: ScreenReceiver

    companion object {
        private lateinit var sInstance: TawajudApplication

        fun getInstance(): TawajudApplication {
            return sInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this

        // Initialize app components
        val appLifecycleObserver = AppLifecycleObserver(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)

        ApiClient.init()

        // Setup shared preferences
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()


        // Register Activity Lifecycle Callbacks
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                Log.e("background_app", "${activity.localClassName} has come to the foreground")
                from_background = true
            }
            override fun onActivityResumed(activity: Activity) {
                Log.e("background_app", "onActivityResumed")
            }
            override fun onActivityPaused(activity: Activity) {
                Log.e("background_app", "onActivityPaused")
            }
            override fun onActivityStopped(activity: Activity) {
                Log.e("background_app", "onActivityStopped")
                from_background = false
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.e("background_app", "onActivitySaveInstanceState")
                from_background = true
            }
            override fun onActivityDestroyed(activity: Activity) {
                Log.e("background_app", "onActivityDestroyed")
            }
        })
        PrefUtils.cancelLocationJob(applicationContext,1)

        // Register the ScreenReceiver
        screenReceiver = ScreenReceiver()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(screenReceiver, filter)
       // scheduleJob()
        //scheduleAppTerminateAlarm(applicationContext)

        /*    WorkManager.getInstance(applicationContext)
                .cancelAllWork()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // Ensure network is connected
                .build()
            val periodicWorkRequest = OneTimeWorkRequestBuilder<AppTerminateWorker>()
                .setInitialDelay(0, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(applicationContext)
                .enqueueUniqueWork(
                    "HeartbeatWorker",
                    ExistingWorkPolicy.KEEP,
                    periodicWorkRequest
                )
            WorkManager.getInstance(applicationContext).getWorkInfosForUniqueWorkLiveData("HeartbeatWorker")
                .observeForever { workInfos ->
                    workInfos.forEach { workInfo ->
                        Log.d("WorkManager", "WorkInfo state: ${workInfo.state}")
                    }
                }*/
        // Start the AppLifecycleService to handle app shutdown notification
        //startService(Intent(applicationContext, AppLifecycleService::class.java))


    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(screenReceiver)

    }

    override fun attachBaseContext(base: Context) {
        var lang = PrefUtils.getStringWithContext(base, Const.SharedPrefs.SELECTED_LANGUAGE)
        if (lang == null || lang == "")
            lang = Const.Other.ENGLISH_LANG_CODE
        super.attachBaseContext(LocaleHelper.onAttach(base, lang))
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleAppTerminateAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AppTerminateAlarmReceiver::class.java)
        if (alarmManager.canScheduleExactAlarms()) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

                    val triggerAtMillis = System.currentTimeMillis()+ 1 * 60 * 1000L // 1 minutes from now
                    val intervalMillis = 1 * 60 * 1000L // 1 minutes interval

           /* val triggerAtMillis = System.currentTimeMillis() + 2000L // 2 seconds from now
            val intervalMillis = 2000L // 2 seconds interval*/

            Log.d("AlarmManager", "Scheduling alarm: Trigger at ${Date(triggerAtMillis)} with interval $intervalMillis ms")

            // Set a repeating alarm
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }else{
            // Request permission
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
        }

    }
    fun cancelAppTerminateAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AppTerminateAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        Log.d("AlarmHelper", "Cancelling alarm")
        alarmManager.cancel(pendingIntent)
    }
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()

}
