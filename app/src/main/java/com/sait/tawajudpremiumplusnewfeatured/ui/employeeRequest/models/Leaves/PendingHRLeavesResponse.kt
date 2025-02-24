package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse




data class PendingHRLeavesResponse(
    val `data`: List<pending_HR_leaves_data>,

    ): BaseResponse()


data class pending_HR_leaves_data(
    val attachedFile: Any,
    val createD_BY: Any,
    val createD_DATE: String,
    val days: Int,
    val employeeArabicName: String,
    val employeeName: String,
    val employeeNo: String,
    val fK_CompanyId: Int,
    val fK_EmployeeId: Int,
    val fK_HREmployeeId: Int,
    val fK_LeaveTypeId: Int,
    val fK_ManagerId: Int,
    val fK_StatusId: Int,
    val fromDate: String,
    val hasAdvancedSalary: Boolean,
    val isHalfDay: Boolean,
    val lang: Int,
    val lasT_UPDATE_BY: Any,
    val lasT_UPDATE_DATE: String,
    val leaveArabicName: String,
    val leaveId: Int,
    val leaveName: String,
    val leaveTypeId: Int,
    val nextApprovalStatus: Int,
    val rejectedReason: Any,
    val remarks: String,
    val requestDate: String,
    val requestedByCoordinator: Boolean,
    val statusId: Int,
    val statusName: String,
    val statusNameArabic: String,
    val toDate: String,
    val totalBalance: Int,
    val userId: Int,
    val withAdvancedSalary: Boolean
)