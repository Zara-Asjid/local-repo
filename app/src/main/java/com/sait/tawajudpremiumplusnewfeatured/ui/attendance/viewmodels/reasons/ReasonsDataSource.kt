package com.sait.tawajudpremiumplusnewfeatured.ui.attendance.viewmodels.reasons

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.models.ReasonsResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ReasonsDataSource {
    suspend fun getReasonsData(

        mContext: Context

    ): Response<ReasonsResponse> {

       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postReasonsData()
    }
}