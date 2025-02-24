package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse

data class ApproveManualEntryResponse(
    val `data`: List<ApproveManualEntryData>,

):BaseResponse()
data class ApproveManualEntryData(
    val attachedFile: Any,
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
)