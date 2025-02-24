package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve

data class Approved_ByManagerRequest(
    val employeeId: String,
    val lang: Int,
    val leaveId: Int,
    val nextApprovalStatus: Int,
    val remarks: String
)