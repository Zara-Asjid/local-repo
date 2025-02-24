package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.viewmodels.Leaves.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve_LeavesRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve_LeavesResponse
import retrofit2.Response

class ApprovedHRLeavesRepository (var pending_leave_datasource: ApprovedHRLeaveDataSource) {
    suspend fun getApprovedHRLeavesData(
        mContext: Context,
        approve_leaves_request: Approve_LeavesRequest

    ): Response<Approve_LeavesResponse> {
        return pending_leave_datasource.getApprovedHRLeaveData(mContext,approve_leaves_request)
    }
}