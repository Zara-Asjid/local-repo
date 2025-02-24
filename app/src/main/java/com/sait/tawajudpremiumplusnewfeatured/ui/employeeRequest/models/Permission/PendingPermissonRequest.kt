package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission

data class PendingPermissonRequest(
    val employeeId: Int,
    val lang: Int,
    val statusId: Int,
    val fromDate:String,
    val toDate:String
)