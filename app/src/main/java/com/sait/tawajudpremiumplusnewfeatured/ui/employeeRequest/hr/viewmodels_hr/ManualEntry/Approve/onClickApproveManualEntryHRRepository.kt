package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryResponse
import retrofit2.Response

class onClickApproveManualEntryHRRepository(var approve_manual_entry_datasource: onClickApproveManualEntryHRDataSource) {
    suspend fun getonclickapproveManualEntryHRData(

        mContext: Context,
        onclickapprove_manual_entry_request: onClickApproveManualEntryRequest

    ): Response<onClickApproveManualEntryResponse> {
        return approve_manual_entry_datasource.getApprovemanualEntryHRData(mContext,onclickapprove_manual_entry_request)
    }
}