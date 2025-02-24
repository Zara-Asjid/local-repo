package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves

data class PendingForManagerRequest(
    val employeeId: Int,
    val lang: Int,
    val statusId: Int,
    val fromDate:String,
    val toDate:String

)


