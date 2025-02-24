package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry

data class PendingManualEntryRequest(
    val employeeId: Int,
    val lang: Int,
    val statusId: Int,
    val fromDate:String,
    val toDate:String
)