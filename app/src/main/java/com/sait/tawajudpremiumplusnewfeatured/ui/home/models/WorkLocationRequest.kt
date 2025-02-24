package com.sait.tawajudpremiumplusnewfeatured.ui.home.models

data class WorkLocationRequest(
    val fK_EmployeeId: Int,

    val lang: Int,
    val managerId: Int,
    val fromDate: String,
    val toDate: String
)