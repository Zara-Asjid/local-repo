package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.Reject

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickReject.onClickRejectManualEntryRequest
import retrofit2.Response

class onClickRejectManualEntryRepository(var reject_manual_entry_datasource: onClickRejectManualEntryDataSource) {
    suspend fun getonclickrejectManualEntryData(

        mContext: Context,
        onclickremove_manual_entry_request: onClickRejectManualEntryRequest

    ): Response<onClickApproveManualEntryResponse> {
        return reject_manual_entry_datasource.getrejectmanualEntryData(mContext,onclickremove_manual_entry_request)
    }
}