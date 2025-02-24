package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.leave

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave.RequestSaveLeaveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave.RequestSaveLeaveResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class RequestSaveLeaveDataSource {
    suspend fun getRequestSaveLeaveData(

        mContext: Context,
        requestSaveLeaveRequest: RequestSaveLeaveRequest

    ): Response<RequestSaveLeaveResponse> {

return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postSaveLeaveRequestData(requestSaveLeaveRequest)
    }
}