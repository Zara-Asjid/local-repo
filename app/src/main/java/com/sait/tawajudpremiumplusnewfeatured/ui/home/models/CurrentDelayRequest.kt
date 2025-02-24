package com.sait.tawajudpremiumplusnewfeatured.ui.home.models

data class CurrentDelayRequest(
    val fK_EmployeeId: Int,
    val fromDate: String,
    val toDate: String,
    val lang: Int
   )