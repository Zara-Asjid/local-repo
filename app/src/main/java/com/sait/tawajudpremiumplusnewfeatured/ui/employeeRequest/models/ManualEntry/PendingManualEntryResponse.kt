package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class PendingManualEntryResponse(
    val `data`: List<PendingManualEntryData>,

    ) :BaseResponse()
@Parcelize
data class PendingManualEntryData(
    val attachedFile: String,
    val createD_BY: String,
    val createD_DATE: String,
    val employeeArabicName: String,
    val employeeName: String,
    val employeeNo: String,
    val fK_EmployeeId: Int,
    val fK_ReasonId: Int,
    val moveDate: String,
    val moveRequestId: Int,
    val moveTime: String,
    val nextApprovalStatus: String,
    val reasonArabicName: String,
    val reasonName: String,
    val rejectionReason: String,
    val remarks: String,
    val statusId: Int,
    val statusName: String,
    val statusNameArabic: String,
    val type: String
) : Parcelable