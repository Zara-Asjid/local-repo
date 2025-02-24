package com.sait.tawajudpremiumplusnewfeatured.ui.summary.models

data class SummaryRequest(
    val fK_EmployeeId: Int,
    val fromDate: String,
    val toDate: String,
    val lang: Int
    )