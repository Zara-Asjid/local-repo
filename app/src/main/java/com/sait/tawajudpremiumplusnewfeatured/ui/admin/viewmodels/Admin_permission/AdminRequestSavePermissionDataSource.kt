package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.Admin_permission

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class AdminRequestSavePermissionDataSource {
    suspend fun getRequestSavePermissionData(

        mContext: Context,
        requestSavePermissionRequest: RequestSavePermissionRequest

    ): Response<RequestSavePermissionResponse> {

        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postSaveAdminPermissionRequestData(requestSavePermissionRequest)
    }
}