package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject

data class RejectLeaveByManagerRequest(
    val employeeId: Int,
    val lang: Int,
    val permissionId: Int,
    val remarks: String,
    val type: String
)