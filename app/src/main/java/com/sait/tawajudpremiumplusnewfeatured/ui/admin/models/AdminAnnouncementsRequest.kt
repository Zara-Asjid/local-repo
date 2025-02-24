package com.sait.tawajudpremiumplusnewfeatured.ui.admin.models

data class AdminAnnouncementsRequest(
    val id: Int,
    val isSpecificDate: Boolean,
    val isYearlyFixed: Boolean,
    val announcementDate: String,
    val fromDate: String,
    val toDate: String,
    val title_Ar: String,
    val title_En: String,
    val content_Ar: String,
    val content_En: String,
    val created_By: String,
    val fK_CompanyId: Int,
    val fK_EmployeeId: Int,
    val fK_EntityId: Int,
    val fK_LogicalGroupId: Int,
    val fK_workLocationId: Int,
    val languageSelection: Int


)