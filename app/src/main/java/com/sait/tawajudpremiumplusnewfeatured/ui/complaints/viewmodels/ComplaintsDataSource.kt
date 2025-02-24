package com.sait.tawajudpremiumplusnewfeatured.ui.complaints.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.complaints.models.ComplaintsRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.complaints.models.ComplaintsResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ComplaintsDataSource {
    suspend fun getContactData(
        mContext: Context,
        employeeId: String,
        lang: String
    ): Response<ComplaintsResponse> {

        val contact = ComplaintsRequest(EmployeeId = employeeId, Lang = lang)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).getComplaintsCompanyData(employeeId,lang)
     //   return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).getContactCompanyData(contact)

    }
}