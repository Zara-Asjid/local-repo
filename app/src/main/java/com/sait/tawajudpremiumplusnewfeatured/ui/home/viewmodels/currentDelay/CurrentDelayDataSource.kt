package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.currentDelay

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class CurrentDelayDataSource {
    suspend fun getCurrentDelayData(
        mContext: Context,
        currentDelayRequest: CurrentDelayRequest
    ): Response<CurrentDelayResponse> {
        // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).
postCurrentDelayData(currentDelayRequest)

    }
}