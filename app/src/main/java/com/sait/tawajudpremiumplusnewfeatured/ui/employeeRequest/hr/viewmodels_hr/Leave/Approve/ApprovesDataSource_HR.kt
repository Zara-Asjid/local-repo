package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Leave.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve_HRLeavesRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ApprovesDataSource_HR{
    suspend fun getApproveHRLeaveData(

        mContext: Context,
        approve_request : Approve_HRLeavesRequest
    ): Response<onClickApproveResponse> {

        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postHRApproveData(approve_request)
    }
}