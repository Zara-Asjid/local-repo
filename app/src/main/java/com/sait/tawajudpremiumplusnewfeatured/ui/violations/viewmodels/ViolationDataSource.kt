package com.sait.tawajudpremiumplusnewfeatured.ui.violations.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ViolationDataSource {
    suspend fun getViolationData(

        mContext: Context,
        employeeId: Int,
        from_date: String,
        to_date: String,

        lang: String
    ): Response<ViolationResponse> {

        val violationRequest = ViolationRequest(employeeId,from_date,to_date,lang)
       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postViolationsData(violationRequest)
    }
}