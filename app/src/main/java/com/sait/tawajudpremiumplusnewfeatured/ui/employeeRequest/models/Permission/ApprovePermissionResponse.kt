package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse

data class ApprovePermissionResponse(
    val `data`: List<ApprovePermissionData>,

):BaseResponse()
data class ApprovePermissionData(
    val attachedFile: String,
    val employeeArabicName: String,
    val employeeName: String,
    val employeeNo: String,
    val fK_EmployeeId: Int,
    val fK_ManagerId: Int,
    val fK_PermId: Int,
    val fromTime: String,
    val isForPeriod: Boolean,
    val isFullDay: Boolean,
    val permArabicName: String,
    val permDate: String,
    val permEndDate: String,
    val permName: String,
    val permTypeId: Int,
    val permissionId: Int,
    val remark: String,
    val statusName: String,
    val statusNameArabic: String,
    val toTime: String



)