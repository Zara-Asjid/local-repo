package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.Permission

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class RequestSavePermissionDataSource {
    suspend fun getRequestSavePermissionData(

        mContext: Context,
        requestSavePermissionRequest: RequestSavePermissionRequest

    ): Response<RequestSavePermissionResponse> {

        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postSavePermissionRequestData(requestSavePermissionRequest)
    }
}