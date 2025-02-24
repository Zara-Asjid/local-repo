package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class PendingHRPermissionResponse(
    val `data`: List<PendingHRPermissionData>,

):BaseResponse()
@Parcelize

data class PendingHRPermissionData(
    val allowAfter: Int,
    val allowBefore: Int,
    val attachedFile: String,
    val createD_BY: String,
    val createD_DATE: String,
    val days: String,
    val employeeArabicName: String,
    val employeeName: String,
    val employeeNo: String,
    val expr1: String,
    val expr2: String,
    val fK_CompanyId: Int,
    val fK_EmployeeId: Int,
    val fK_HREmployeeId: Int,
    val fK_ManagerId: Int,
    val fK_PermId: Int,
    val fK_StatusId: Int,
    val flexibilePermissionDuration: Int,
    val flexiblePermissionDuration: Boolean,
    val fromDate: String,
    val fromTime: String,
    val isDividable: Boolean,
    val isFlexible: Boolean,
    val isForPeriod: Boolean,
    val isFullDay: Boolean,
    val isSpecificDays: Boolean,
    val lang: Int,
    val lasT_UPDATE_BY: String,
    val lasT_UPDATE_DATE: String,
    val m_Date: Int,
    val meetingPerson: String,
    val nextApprovalStatus: String,
    val permArabicName: String,
    val permDate: String,
    val permEndDate: String,
    val permName: String,
    val permTypeId: Int,
    val permissionId: Int,
    val rejectedReason: String,
    val remark: String,
    val requestedByCoordinator: Boolean,
    val scH_START_TIME: Int,
    val statusName: String,
    val statusNameArabic: String,
    val toDate: String,
    val toT_WORK_HRS: Int,
    val toTime: String,
    val totalTime: String,
    val venue: String
): Parcelable