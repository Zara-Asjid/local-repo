package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.RequestRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.RequestResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class SelfRequestDataSource {
    suspend fun getRequestData(

        mContext: Context,
        employeeId: Int,
        lang: String,
        type: String

    ): Response<RequestResponse> {

        val violationRequest = RequestRequest(employeeId.toString(),lang,type)
       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postRequestData(employeeId.toString(),lang,type)
    }
}