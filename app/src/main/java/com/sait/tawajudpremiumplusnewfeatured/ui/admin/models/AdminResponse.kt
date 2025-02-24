package com.sait.tawajudpremiumplusnewfeatured.ui.manager.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class AdminResponse(
    val `data`: List<AdminData>,

    ): BaseResponse()


@Parcelize

data class AdminData(
    val employeeArabicName: String,
    val employeeId: Int,
    val employeeName: String,
    val employeeNo: String,
    val entityHierarchy: String,
    val fK_CompanyId: Int
): Parcelable