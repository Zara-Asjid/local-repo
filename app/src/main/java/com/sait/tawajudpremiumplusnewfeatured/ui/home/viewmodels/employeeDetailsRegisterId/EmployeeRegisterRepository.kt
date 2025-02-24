package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.employeeDetailsRegisterId

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmpDetailsRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmployeeDetailsResponse
import retrofit2.Response


class EmployeeRegisterRepository(var employeeDataSource: EmployeeRegisterDataSource) {
    suspend fun getEmployeeRegisterData(

        mContext: Context,
        employeeRequest: EmpDetailsRequest

    ): Response<EmployeeDetailsResponse> {
        return employeeDataSource.getEmployeeRegisterData(mContext,employeeRequest)
    }
}