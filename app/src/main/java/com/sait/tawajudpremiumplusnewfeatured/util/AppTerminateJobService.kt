package com.sait.tawajudpremiumplusnewfeatured.util

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.login.viewmodels.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppTerminateJobService: JobService() {
    private var jobRunning = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onStartJob(params: JobParameters?): Boolean {
        GlobalScope.launch(Dispatchers.IO) {
            jobRunning = true
            Log.d("JobScheduler", "Job started ")

/*
            // Schedule a task to finish the job after 2 seconds
            handler.postDelayed({
                if (jobRunning) {
                    Log.d("AppTerminateJob", "Job finished after 2 seconds")
                    jobFinished(params, false) // Notify the system that the job is complete
                }
            }, 2000) // Delay of 2 seconds*/
        }
        return true // Indicate that work is ongoing
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        // Called if the job is stopped before completion
        Log.d("AppLifecycleService", "Job stopped before completion")
        return false // Return false to drop the job
    }
}