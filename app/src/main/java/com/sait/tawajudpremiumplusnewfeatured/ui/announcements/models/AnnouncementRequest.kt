package com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models

data class AnnouncementRequest(
    val fK_EmployeeId: Int,
    val lang: Int,
    val managerId: Int,
 val toDate: String,
    val fromDate: String,
)