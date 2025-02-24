package com.sait.tawajudpremiumplusnewfeatured.ui.reports.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.models.Reports_Self_ServiceResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.models.Reports_Self_Service_Request
import retrofit2.Response


class ReportsRepository(var reportsDataSource: ReportsDataSource) {
    suspend fun getReportsData(

        mContext: Context,
        reports_Self_Service_Request:Reports_Self_Service_Request

    ): Response<Reports_Self_ServiceResponse> {
        return reportsDataSource.getReportsData(mContext,reports_Self_Service_Request)
    }
}