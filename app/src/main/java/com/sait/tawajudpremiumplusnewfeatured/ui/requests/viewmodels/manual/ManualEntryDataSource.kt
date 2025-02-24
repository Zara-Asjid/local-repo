package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.manual

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual.ManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual.ManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ManualEntryDataSource {
    suspend fun getManualEntryData(

        mContext: Context,
        manualEntryRequest: ManualEntryRequest
    ): Response<ManualEntryResponse> {

  return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postManulaEntryData(manualEntryRequest)
    }
}