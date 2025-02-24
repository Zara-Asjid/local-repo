package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions.ApprovedList

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionResponse
import retrofit2.Response

class ApprovedHRPermissionRepository (var pending_leave_datasource: ApprovedHRPermissionDataSource) {
    suspend fun getApprovedHRPermissionData(
        mContext: Context,
        approve_leaves_request: ApprovePermissionRequest

    ): Response<ApprovePermissionResponse> {
        return pending_leave_datasource.getApprovedHRPermissionData(mContext,approve_leaves_request)
    }
}