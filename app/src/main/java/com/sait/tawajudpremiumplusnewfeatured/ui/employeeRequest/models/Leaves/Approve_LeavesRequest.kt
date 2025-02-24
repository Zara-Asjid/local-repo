package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves

data class Approve_LeavesRequest(
    val fK_EmployeeId: Int,
    val fromDate: String,
    val toDate: String,
    val lang: Int
)