package com.sait.tawajudpremiumplusnewfeatured.ui.home.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationData
import kotlinx.parcelize.Parcelize

data class CurrentDelayResponse(
    val `data`: List<ViolationData>

    ):BaseResponse()
@Parcelize


data class CurrentDelaysData(
    val attachedFile: String?,
    val delay: String,
    val description: String,
    val descriptionAr: String,
    val duration: String,
    val earlyOut: Int,
    val employeeArabicName: String,
    val employeeId: Int,
    val employeeName: String,
    val employeeNo: String,
    val fK_CompanyId: Int,
    val fK_EntityId: Int,
    val fK_PermId: Int,
    val fK_StatusId: Int,
    val inTime: String,
    val moveDate: String,
    val outTime: String,
    val permEnd: Int,
    val permStart: String,
    val permType: String,
    val permTypeAr: String,
    val permissionId: Int,
    val remark: String,
    val status: String,
    val type: String
): Parcelable
