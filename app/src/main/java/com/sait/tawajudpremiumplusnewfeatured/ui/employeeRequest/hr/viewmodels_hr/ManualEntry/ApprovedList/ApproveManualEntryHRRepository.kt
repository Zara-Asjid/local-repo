package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.ManualEntry.ApprovedList

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryResponse
import retrofit2.Response

class ApproveManualEntryHRRepository (var reject_leave_datasource: ApproveManualEntryHRDataSource) {
    suspend fun getApproveManualEntryHRData(

        mContext: Context,
        approve_manual_entry_request: ApproveManualEntryRequest

    ): Response<ApproveManualEntryResponse> {
        return reject_leave_datasource.getApproveManualEntryHRData(mContext,approve_manual_entry_request)
    }
}