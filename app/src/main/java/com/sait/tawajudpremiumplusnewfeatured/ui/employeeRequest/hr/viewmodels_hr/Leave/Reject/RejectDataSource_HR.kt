package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Leave.Reject

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class RejectDataSource_HR {
    suspend fun getRejectHRLeaveData(

        mContext: Context,
        reject_leave_request : RejectLeaveByManagerRequest
    ): Response<RejectLeaveByManagerResponse> {

        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postRejectHRLeaveData(reject_leave_request)
    }
}