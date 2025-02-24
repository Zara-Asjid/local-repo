package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.manual

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual.ManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual.ManualEntryResponse
import retrofit2.Response


class ManualEntryRepository(var manualEntryDataSource: ManualEntryDataSource) {
    suspend fun getManualEntryData(

        mContext: Context,
        manualEntryRequest: ManualEntryRequest


    ): Response<ManualEntryResponse> {
        return manualEntryDataSource.getManualEntryData(mContext,manualEntryRequest)
    }
}