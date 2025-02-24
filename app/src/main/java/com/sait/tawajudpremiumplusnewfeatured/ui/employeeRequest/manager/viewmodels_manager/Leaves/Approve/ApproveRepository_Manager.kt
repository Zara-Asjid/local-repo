package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Leaves.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve.Approved_ByManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve.Approved_leaveResponse
import retrofit2.Response

class ApproveRepository_Manager(var pending_leave_datasource: ApprovesDataSource_Manager) {
    suspend fun getApproveLeaveData(

        mContext: Context,
        approve_leaves_request: Approved_ByManagerRequest

    ): Response<Approved_leaveResponse> {
        return pending_leave_datasource.getApproveLeaveData(mContext,approve_leaves_request)
    }
}