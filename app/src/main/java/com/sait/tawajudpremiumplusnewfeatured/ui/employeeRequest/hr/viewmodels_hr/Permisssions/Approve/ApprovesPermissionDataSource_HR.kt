package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ApprovesPermissionDataSource_HR{
    suspend fun getApproveHRPermissionData(

        mContext: Context,
        approve_request : onClickApproveRequest
    ): Response<onClickApproveResponse> {

        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postHRApprovePermissionData(approve_request)
    }
}