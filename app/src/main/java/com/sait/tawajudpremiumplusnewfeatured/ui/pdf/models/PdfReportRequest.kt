package com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models

data class PdfReportRequest(
    val employeeId: Int,
    val fromDate: String,
    val lang: Int,
    val managerId: Int,
    val registeredDevice: String,
    val reportId: Int,
    val toDate: String,
    val username: Int,
    val pageNumber: Int,
    val pageSize: Int,

)