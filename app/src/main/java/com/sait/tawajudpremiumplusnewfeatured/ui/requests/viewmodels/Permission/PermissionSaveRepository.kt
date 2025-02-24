package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.Permission

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionResponse
import retrofit2.Response

class PermissionSaveRepository(var requestSaveLeaveDataSource: RequestSavePermissionDataSource) {
    suspend fun getRequestSavePermissionData(

        mContext: Context,
        requestSavePermissionRequest: RequestSavePermissionRequest

    ): Response<RequestSavePermissionResponse> {
        return requestSaveLeaveDataSource.getRequestSavePermissionData(mContext,requestSavePermissionRequest)
    }
}