package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Permissions.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class onClickApprovePermDataSource {
    suspend fun getApproveData(

        mContext: Context,
        approve_permission_request : onClickApproveRequest
    ): Response<onClickApproveResponse> {

        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postApprovePermissionData(approve_permission_request)
    }
}