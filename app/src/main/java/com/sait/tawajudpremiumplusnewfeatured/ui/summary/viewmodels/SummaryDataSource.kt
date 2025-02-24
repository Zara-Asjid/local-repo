package com.sait.tawajudpremiumplusnewfeatured.ui.summary.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.models.SummaryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.models.SummaryResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class SummaryDataSource {
    suspend fun getSummaryData(

        mContext: Context,
        summaryRequest: SummaryRequest
    ): Response<SummaryResponse> {

       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postSummaryData(summaryRequest)
    }
}