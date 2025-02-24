package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class onClickApproveManualEntryHRDataSource {
    suspend fun getApprovemanualEntryHRData(

        mContext: Context,
        approve_manual_entry_request : onClickApproveManualEntryRequest
    ): Response<onClickApproveManualEntryResponse> {

        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postapproveManualEntryHRData(approve_manual_entry_request)
    }
}