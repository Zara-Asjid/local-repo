package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Leaves.Reject

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class RejectDataSource_Manager {
    suspend fun getRejectLeaveData(

        mContext: Context,
        reject_leave_request : RejectLeaveByManagerRequest
    ): Response<RejectLeaveByManagerResponse> {

        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postRejectLeaveData(reject_leave_request)
    }
}