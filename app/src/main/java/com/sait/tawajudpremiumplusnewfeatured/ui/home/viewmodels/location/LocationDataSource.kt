package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.location

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.LocationResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class LocationDataSource {
    suspend fun getLocationData(

        mContext: Context,
        locationRequest: CommonRequest

    ): Response<LocationResponse> {

       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).
postLocationData(locationRequest)
    }
}