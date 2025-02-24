package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Permissions.Pending

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingPermissionResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingPermissonRequest
import retrofit2.Response

class PendingPermissionRepository(var reject_leave_datasource: PendingPermissionDataSource) {
    suspend fun getPendingPermissionData(

        mContext: Context,
        pending_permission_request: PendingPermissonRequest

    ): Response<PendingPermissionResponse> {
        return reject_leave_datasource.getPendingPermissionData(mContext,pending_permission_request)
    }
}