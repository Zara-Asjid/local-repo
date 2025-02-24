package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Permissions.ApprovedList

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionResponse
import retrofit2.Response

class ApprovePermissionRepository (var reject_leave_datasource: ApprovePermissionDataSource) {
    suspend fun getApprovePermissionData(

        mContext: Context,
        approve_permission_request: ApprovePermissionRequest

    ): Response<ApprovePermissionResponse> {
        return reject_leave_datasource.getApprovePermission(mContext,approve_permission_request)
    }
}