package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardResponse
import retrofit2.Response


class DashboardRepository(var dashbaordDataSource: DashboardDataSource) {
    suspend fun getDashboardData(

        mContext: Context,
        dashboardRequest: DashboardRequest,

        ): Response<DashboardResponse> {
        return dashbaordDataSource.getDashboardData(mContext,dashboardRequest)
    }
}