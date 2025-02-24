package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.location

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.LocationResponse
import retrofit2.Response


class LocationRepository(var locationDataSource: LocationDataSource) {
    suspend fun getLocationData(

        mContext: Context,
        locationRequest: CommonRequest

    ): Response<LocationResponse> {
        return locationDataSource.getLocationData(mContext,locationRequest)
    }
}