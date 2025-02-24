package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Leave.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve_HRLeavesRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveResponse
import retrofit2.Response

class ApproveRepository_HR(var pending_leave_datasource: ApprovesDataSource_HR) {
    suspend fun getApproveHRLeaveData(

        mContext: Context,
        approve_leaves_request: Approve_HRLeavesRequest

    ): Response<onClickApproveResponse> {
        return pending_leave_datasource.getApproveHRLeaveData(mContext,approve_leaves_request)
    }
}