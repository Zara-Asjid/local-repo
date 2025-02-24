package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize


data class PendingHRManualEntryResponse(
    val `data`: List<PendingHRManualEntryData>,
    ) : BaseResponse()
@Parcelize
data class PendingHRManualEntryData(
    val attachedFile: String,
    val createD_BY: String,
    val createD_DATE: String,
    val employeeArabicName: String,
    val employeeName: String,
    val employeeNo: String,
    val fK_EmployeeId: Int,
    val fK_HREmployeeId: Int,
    val fK_ManagerId: Int,
    val fK_ReasonId: Int,
    val fromDate: String,
    val isFromMobile: Boolean,
    val isManual: Boolean,
    val isRejected: Boolean,
    val isRemoteWork: Boolean,
    val lasT_UPDATE_BY: String,
    val lasT_UPDATE_DATE: String,
    val m_DATE_NUM: String,
    val m_TIME_NUM: String,
    val mobileCoordinates: String,
    val moveDate: String,
    val moveRequestId: Int,
    val moveTime: String,
    val nextApprovalStatus: String,
    val reader: String,
    val reasonArabicName: String,
    val reasonName: String,
    val rejectedReason: String,
    val rejectionReason: String,
    val remarks: String,
    val status: String,
    val statusId: Int,
    val statusName: String,
    val statusNameArabic: String,
    val syS_Date: String,
    val toDate: String,
    val type: String
): Parcelable