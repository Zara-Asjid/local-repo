package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickReject

data class onClickRejectManualEntryRequest(
    val employeeId: Int,
    val lang: Int,
    val moveRequestId: Int,
    val registeredDevice: String,
    val rejectedReason: String,
    val remarks: String
)