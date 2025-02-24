package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.Pending

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingManualEntryResponse
import retrofit2.Response

class PendingManualEntryRepository(var reject_leave_datasource: PendingManualEntryDataSource) {
    suspend fun getPendingManualEntryData(

        mContext: Context,
        pending_manual_entry_request: PendingManualEntryRequest

    ): Response<PendingManualEntryResponse> {
        return reject_leave_datasource.getPendingManualEntryData(mContext,pending_manual_entry_request)
    }
}