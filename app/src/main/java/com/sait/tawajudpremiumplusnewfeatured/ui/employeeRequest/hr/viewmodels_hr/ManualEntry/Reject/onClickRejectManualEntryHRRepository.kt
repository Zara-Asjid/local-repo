package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.Reject

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickReject.onClickRejectManualEntryRequest
import retrofit2.Response

class onClickRejectManualEntryHRRepository(var reject_manual_entry_datasource: onClickRejectManualEntryHRDataSource) {
    suspend fun getonclickrejectManualEntryHRData(

        mContext: Context,
        onclickremove_manual_entry_request: onClickRejectManualEntryRequest

    ): Response<onClickApproveManualEntryResponse> {
        return reject_manual_entry_datasource.getrejectmanualEntryHRData(mContext,onclickremove_manual_entry_request)
    }
}