package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions.Pending

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingHRPermissionResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingPermissonRequest
import retrofit2.Response

class PendingHRPermissionRepository(var reject_leave_datasource: PendingHRPermissionDataSource) {
    suspend fun getPendingHRPermissionData(

        mContext: Context,
        pending_permission_request: PendingPermissonRequest

    ): Response<PendingHRPermissionResponse> {
        return reject_leave_datasource.getPendingHRPermissionData(mContext,pending_permission_request)
    }
}