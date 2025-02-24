package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission

data class ApprovePermissionRequest(
    val fK_EmployeeId: Int,
    val fromDate: String,
    val toDate: String,
    val lang: Int
)