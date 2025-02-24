package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.Admin_permission

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionResponse
import retrofit2.Response

class AdminPermissionSaveRepository(var requestSaveLeaveDataSource: AdminRequestSavePermissionDataSource) {
    suspend fun getRequestSavePermissionData(

        mContext: Context,
        requestSavePermissionRequest: RequestSavePermissionRequest

    ): Response<RequestSavePermissionResponse> {
        return requestSaveLeaveDataSource.getRequestSavePermissionData(mContext,requestSavePermissionRequest)
    }
}