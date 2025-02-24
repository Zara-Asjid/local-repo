package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Leave.Pending

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingForManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingHRLeavesResponse
import retrofit2.Response


class PendingHRLeavesRepository(var pending_leave_datasource: PendingHRLeavesDataSource) {
    suspend fun getPendingHRLeavesData(

        mContext: Context,
        pending_leaves_request: PendingForManagerRequest

    ): Response<PendingHRLeavesResponse> {

        return pending_leave_datasource.getPendingHRLeaveData(mContext,pending_leaves_request)
    }
}