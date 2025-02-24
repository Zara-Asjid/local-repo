package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.employeeDetails

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmployeeDetailsResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmpDetailsRequest
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class EmployeeDataSource {
    suspend fun getEmployeeData(
        mContext: Context,
        employeeRequest: EmpDetailsRequest
    ): Response<EmployeeDetailsResponse> {
        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString())
            .postEmployeeData(
                employeeRequest.FK_EmployeeId,
                UserShardPrefrences.getLanguage(mContext)
            )
    }
}