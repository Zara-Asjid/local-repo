package com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models


import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse


data class PdfReportResponse(
    val `data`: PdfReportData
):BaseResponse()

data class PdfReportData(
    val pdfFile_base: String,
    val msg: String

)



/*
data class PdfReportResponse(
    val `data`: PdfReportData,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)data class PdfReportData(
    val pdfFile_base: String
)*/
