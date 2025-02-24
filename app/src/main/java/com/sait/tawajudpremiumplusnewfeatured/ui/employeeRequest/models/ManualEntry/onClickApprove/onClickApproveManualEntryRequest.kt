package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove

data class onClickApproveManualEntryRequest(
    val fk_EmployeeId: Int,
    val lang: Int,
    val moveRequestId: Int,
    val nextApprovalStatus: Int,
    val remarks: String
)