package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.RequestResponse
import retrofit2.Response


class SelfRequestRepository(var violationDataSource: SelfRequestDataSource) {
    suspend fun getRequestData(

        mContext: Context,
        employeeId: Int,
        lang: String,
        type: String,


    ): Response<RequestResponse> {
        return violationDataSource.getRequestData(mContext,employeeId,lang,type)
    }
}