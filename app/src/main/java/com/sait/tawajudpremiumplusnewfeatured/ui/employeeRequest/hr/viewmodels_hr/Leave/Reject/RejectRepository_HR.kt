package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Leave.Reject

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerRequest
import retrofit2.Response

class RejectRepository_HR(var reject_leave_datasource: RejectDataSource_HR) {
    suspend fun getRejectLeaveData(

        mContext: Context,
        reject_leave_request: RejectLeaveByManagerRequest

    ): Response<RejectLeaveByManagerResponse> {
        return reject_leave_datasource.getRejectHRLeaveData(mContext,reject_leave_request)
    }
}