package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.viewmodels.Leaves.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve_LeavesRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve_LeavesResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ApprovedHRLeaveDataSource {
    suspend fun getApprovedHRLeaveData(
        mContext: Context,
        approve_leaves_request : Approve_LeavesRequest
    ): Response<Approve_LeavesResponse> {

        // return ApiClient.createService().postLoginData(login)


        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postApproveHRLeaveData(approve_leaves_request)

    }
}