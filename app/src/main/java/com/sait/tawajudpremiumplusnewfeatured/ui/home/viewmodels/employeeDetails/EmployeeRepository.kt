package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.employeeDetails

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmployeeDetailsResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmpDetailsRequest
import retrofit2.Response


class EmployeeRepository(var employeeDataSource: EmployeeDataSource) {
    suspend fun getEmployeeData(

        mContext: Context,
        employeeRequest: EmpDetailsRequest

    ): Response<EmployeeDetailsResponse> {
        return employeeDataSource.getEmployeeData(mContext,employeeRequest)
    }
}