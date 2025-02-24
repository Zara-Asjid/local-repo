package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.employeeDetailsRegisterId

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmpDetailsRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmployeeDetailsResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class EmployeeRegisterDataSource {
    suspend fun getEmployeeRegisterData(
        mContext: Context,
        employeeRequest: EmpDetailsRequest
    ): Response<EmployeeDetailsResponse> {
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString())
            .postEmployeeDataRegisterId(
                employeeRequest.FK_EmployeeId,
                UserShardPrefrences.getLanguage(mContext),
                employeeRequest.registeredDeviceID
            )
    }
}