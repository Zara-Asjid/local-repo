package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions.ApprovedList

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ApprovedHRPermissionDataSource {
    suspend fun getApprovedHRPermissionData(
        mContext: Context,
        approve_leaves_request : ApprovePermissionRequest
    ): Response<ApprovePermissionResponse> {

        // return ApiClient.createService().postLoginData(login)


        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postApprovedHRPermissionData(approve_leaves_request)

    }
}