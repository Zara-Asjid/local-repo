package com.sait.tawajudpremiumplusnewfeatured.util

import android.content.Context
import android.util.Log
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication.Companion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContinuousTaskManager(private val context: Context) {
    private var taskScope: CoroutineScope? = null


    // Start the continuous task
    fun startContinuousTask() {
        if (taskScope != null) return // Avoid restarting if already running

        taskScope = CoroutineScope(Dispatchers.Default)
        taskScope?.launch {
            while (isActive) {
                try {
                    // Perform the desired task (e.g., API call)
                    performTask()
                } catch (e: Exception) {
                    Log.e("ContinuousTaskManager", "Error in task: ${e.message}")
                }

                // Wait for 30 minutes before the next iteration
                delay(1 * 60 * 1000L) // 30 minutes
            }
        }
    }

    // Stop the continuous task
    fun stopContinuousTask() {
        taskScope?.cancel()
        taskScope = null
    }

    private suspend fun performTask() {
        withContext(Dispatchers.IO) {
            Log.d("ContinuousTaskManager", "Performing the task...")

            // Example: Call an API
            // Replace this with your API call or logic
            // For example:
            // val response = apiService.callApi()
            // Log.d("API Response", response.toString())

            Log.d("ContinuousTaskManager", "Task completed.")
        }
    }
}