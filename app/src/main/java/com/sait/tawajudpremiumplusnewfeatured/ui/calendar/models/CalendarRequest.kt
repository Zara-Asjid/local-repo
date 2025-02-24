package com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models

data class CalendarRequest(
    val fK_EmployeeId: Int,
    val fromDate: String,
    val toDate: String,
    val lang: String,

    )