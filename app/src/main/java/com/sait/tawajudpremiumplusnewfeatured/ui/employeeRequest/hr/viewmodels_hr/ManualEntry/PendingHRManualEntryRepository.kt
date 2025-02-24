package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.ManualEntry

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingHRManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingManualEntryRequest
import retrofit2.Response

class PendingHRManualEntryRepository(var reject_leave_datasource: PendingHRManualEntryDataSource) {
    suspend fun getPendingHRManualEntryData(

        mContext: Context,
        pending_manual_entry_request: PendingManualEntryRequest

    ): Response<PendingHRManualEntryResponse> {
        return reject_leave_datasource.getPendingHRManualEntryData(mContext,pending_manual_entry_request)
    }
}