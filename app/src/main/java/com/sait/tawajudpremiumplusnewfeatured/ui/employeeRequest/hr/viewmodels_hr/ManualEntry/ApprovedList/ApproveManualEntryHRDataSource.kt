
package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.ManualEntry.ApprovedList

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ApproveManualEntryHRDataSource{
    suspend fun getApproveManualEntryHRData(

        mContext: Context,
        approve_manual_entry_request : ApproveManualEntryRequest
    ): Response<ApproveManualEntryResponse> {

        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postApproveManualEntryHRData(approve_manual_entry_request)
    }
}