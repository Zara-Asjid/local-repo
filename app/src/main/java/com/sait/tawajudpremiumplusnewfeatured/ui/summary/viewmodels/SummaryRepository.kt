package com.sait.tawajudpremiumplusnewfeatured.ui.violations.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.models.SummaryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.models.SummaryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.viewmodels.SummaryDataSource
import retrofit2.Response


class SummaryRepository(var summaryDataSource: SummaryDataSource) {
    suspend fun getSummaryData(

        mContext: Context,
        summaryRequest: SummaryRequest,

        ): Response<SummaryResponse> {
        return summaryDataSource.getSummaryData(mContext,summaryRequest)
    }
}