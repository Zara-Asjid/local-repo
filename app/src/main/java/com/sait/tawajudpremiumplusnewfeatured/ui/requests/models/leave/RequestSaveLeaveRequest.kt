package com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave

data class RequestSaveLeaveRequest(
    val fK_EmployeeId: Int,
    val fromDate: String,
    val lang: Int,
    val leaveTypeId: Int,
    val leaveid: Int,
    val registeredDevice: String,
    val remarks: String,
    val toDate: String,
    val attachedFile: String,
    val fileType: String

)