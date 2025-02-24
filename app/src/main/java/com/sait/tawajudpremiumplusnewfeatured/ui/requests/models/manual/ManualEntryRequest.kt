package com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual

data class ManualEntryRequest(
    val fK_EmployeeId: Int,
    val lang: Int,
    val moveDate: String,
    val moveRequestId: Int,
    val moveTime: String,
    val reasonId: Int,
    val registeredDevice: String,
    val remarks: String,
    val attachedFile: String,
    val fileType: String,
)