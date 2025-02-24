package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Leaves.Reject

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerRequest
import retrofit2.Response

class RejectRepository_Manager(var reject_leave_datasource: RejectDataSource_Manager) {
    suspend fun getRejectLeaveData(

        mContext: Context,
        reject_leave_request: RejectLeaveByManagerRequest

    ): Response<RejectLeaveByManagerResponse> {
        return reject_leave_datasource.getRejectLeaveData(mContext,reject_leave_request)
    }
}