package com.sait.tawajudpremiumplusnewfeatured.ui.manager.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class ManagerEmpResponse(
    val `data`: List<Manager_EmployeeData>,

    ): BaseResponse()


@Parcelize

data class Manager_EmployeeData(
    val empManagerId: Int,
    val employeeArabicName: String,
    val employeeName: String,
    val employeeNo: String,
    val fK_EmployeeId: Int,
    val fK_ManagerId: Int,
    val fromDate: String,
    val toDate: String
): Parcelable