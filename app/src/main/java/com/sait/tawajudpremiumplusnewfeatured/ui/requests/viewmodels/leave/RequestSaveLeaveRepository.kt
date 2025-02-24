package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.leave

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave.RequestSaveLeaveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave.RequestSaveLeaveResponse
import retrofit2.Response


class RequestSaveLeaveRepository(var requestSaveLeaveDataSource: RequestSaveLeaveDataSource) {
    suspend fun getRequestSaveLeaveData(

        mContext: Context,
        requestSaveLeaveRequest: RequestSaveLeaveRequest

    ): Response<RequestSaveLeaveResponse> {
        return requestSaveLeaveDataSource.getRequestSaveLeaveData(mContext,requestSaveLeaveRequest)
    }
}