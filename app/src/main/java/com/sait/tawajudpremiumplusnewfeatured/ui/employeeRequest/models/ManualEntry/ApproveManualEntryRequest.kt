package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry

data class ApproveManualEntryRequest(
    val fK_EmployeeId: Int,
    val fromDate: String,
    val toDate: String,
    val lang: Int
)