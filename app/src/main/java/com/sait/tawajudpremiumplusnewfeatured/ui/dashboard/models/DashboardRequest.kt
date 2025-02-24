package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models

data class DashboardRequest(
    val companyId: Int,
    val dashboardid: Int,
    val entityId: Int,
    val fromDate: String,
    val lang: Int,
    val managerId: Int,
    val toDate: String,
    val adminId: Int,
    val employeeId: Int
    )


