package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveResponse
import retrofit2.Response

class ApprovePermissionRepository_HR(var pending_permission_datasource: ApprovesPermissionDataSource_HR) {
    suspend fun getApproveHRPermissionData(

        mContext: Context,
        approve_permission_request: onClickApproveRequest

    ): Response<onClickApproveResponse> {
        return pending_permission_datasource.getApproveHRPermissionData(mContext,approve_permission_request)
    }
}