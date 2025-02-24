package com.sait.tawajudpremiumplusnewfeatured.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.util.Log

class LocationSettingsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (LocationManager.PROVIDERS_CHANGED_ACTION == intent?.action) {
            val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled && !isNetworkEnabled) {
                Log.e("LocationSettingsReceiver", "Location is disabled")
                // Handle location disabled scenario
            } else {
                Log.e("LocationSettingsReceiver", "Location is enabled")
                // Handle location enabled scenario
            }
        }
    }
}
