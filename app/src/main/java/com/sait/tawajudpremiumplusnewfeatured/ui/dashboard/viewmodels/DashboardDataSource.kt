package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class DashboardDataSource {
    suspend fun getDashboardData(

        mContext: Context,
        dashboardRequest: DashboardRequest,
       ): Response<DashboardResponse> {

       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postDashboardData(dashboardRequest)
    }
}