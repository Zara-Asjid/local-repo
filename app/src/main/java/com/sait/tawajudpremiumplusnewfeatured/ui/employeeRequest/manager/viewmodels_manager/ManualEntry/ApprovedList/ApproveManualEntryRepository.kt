package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.ApprovedList

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryResponse
import retrofit2.Response

class ApproveManualEntryRepository (var reject_leave_datasource: ApproveManualEntryDataSource) {
    suspend fun getApproveManualEntryHRData(

        mContext: Context,
        approve_manual_entry_request: ApproveManualEntryRequest

    ): Response<ApproveManualEntryResponse> {
        return reject_leave_datasource.getApproveManualEntryData(mContext,approve_manual_entry_request)
    }
}