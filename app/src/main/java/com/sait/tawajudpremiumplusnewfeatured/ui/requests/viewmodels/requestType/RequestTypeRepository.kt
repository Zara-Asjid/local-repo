package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.requestType

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.requestType.RequestTypeResponse
import retrofit2.Response


class RequestTypeRepository(var requestTypeDataSource: RequestTypeDataSource) {
    suspend fun getRequestTypeData(

        mContext: Context,
        commonRequest: CommonRequest


    ): Response<RequestTypeResponse> {
        return requestTypeDataSource.getRequestTypeData(mContext,commonRequest)
    }
}