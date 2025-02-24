package com.sait.tawajudpremiumplusnewfeatured.ui.reports.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.models.Reports_Self_Service_Request
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.models.Reports_Self_ServiceResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ReportsDataSource {
    suspend fun getReportsData(

        mContext: Context,
        reports_Self_Service_Request :Reports_Self_Service_Request
    ): Response<Reports_Self_ServiceResponse> {

       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postReportsData(reports_Self_Service_Request)
    }
}