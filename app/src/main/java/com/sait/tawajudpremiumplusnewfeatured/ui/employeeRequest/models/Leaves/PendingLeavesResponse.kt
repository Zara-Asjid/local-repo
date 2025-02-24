package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse

data class PendingLeavesResponse(
    val `data`: List<pending_leaves_data>,

):BaseResponse()
data class pending_leaves_data(
    val attachedFile: String,
    val days: Int,
    val employeeArabicName: String,
    val employeeName: String,
    val employeeNo: String,
    val fK_EmployeeId: Int,
    val fromDate: String,
    val hasAdvancedSalary: Boolean,
    val leaveArabicName: String,
    val leaveId: Int,
    val leaveName: String,
    val leaveTypeId: Int,
    val nextApprovalStatus: Int,
    val remarks: String,
    val requestDate: String,
    val statusId: Int,
    val statusName: String,
    val statusNameArabic: String,
    val toDate: String,
    val totalBalance: Int
)