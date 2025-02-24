package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.WorkLocations

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.WorkLocationRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.WorkLocationResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class WorkLocationDataSource {
    suspend fun getWorkLocationData(
        mContext: Context,
        currentDelayRequest: WorkLocationRequest
    ): Response<WorkLocationResponse> {
        // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postWorkLocations(currentDelayRequest)

    }
}