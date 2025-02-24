package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.requestType

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.requestType.RequestTypeResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class RequestTypeDataSource {
    suspend fun getRequestTypeData(

        mContext: Context,
        commonRequest: CommonRequest
    ): Response<RequestTypeResponse> {

return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postRequestTypeData(commonRequest)
    }
}