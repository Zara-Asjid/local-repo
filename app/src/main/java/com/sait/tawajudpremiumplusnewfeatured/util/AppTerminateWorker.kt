package com.sait.tawajudpremiumplusnewfeatured.util

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.scheduleAppTerminateJob

class AppTerminateWorker (context: Context, params: WorkerParameters) : Worker(context, params) {
    /*  override fun doWork(): Result {
           return try {
               val jobInfo = JobInfo.Builder(
                   123,
                   ComponentName(applicationContext, AppTerminateJobService::class.java)
               )
                   .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                   .setPersisted(true)
                   .build()

               val jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
               jobScheduler.schedule(jobInfo)

               Log.d("AppLifecycleService", "Worker starts")

               Result.success()
           } catch (e: Exception) {
               Result.retry()
           }
       }*/

  override fun doWork(): Result {
      return try {
          // Replace with your actual API call
          Log.d("WorkManager", "Worker starts")
          Result.success()
      } catch (e: Exception) {
          Result.retry()
      }
  }
}