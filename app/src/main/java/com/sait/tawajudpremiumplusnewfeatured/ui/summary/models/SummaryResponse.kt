package com.sait.tawajudpremiumplusnewfeatured.ui.summary.models

data class SummaryResponse(
    val `data`: SummaryData,

    )
data class SummaryData(
    val absent: Int,
    val delay: String,
    val delay_Early_Out: String,
    val early_Out: String,
    val lostTime: String,
    val missingIn: Int,
    val missingOut: Int,
    val notCompletionHalfDay: Int,
    val remainingPermissionBalance: String,
    val remainingTimesPersonalPermission: Int,
    val remainingYearlyLeaveBalance: Int
)