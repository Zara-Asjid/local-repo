package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.ManualEntry

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingHRManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class PendingHRManualEntryDataSource{
    suspend fun getPendingHRManualEntryData(

        mContext: Context,
        pending_manual_entry_request : PendingManualEntryRequest
    ): Response<PendingHRManualEntryResponse> {

        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postPendingHRManualEntryData(pending_manual_entry_request)
    }
}