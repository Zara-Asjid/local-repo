package com.sait.tawajudpremiumplusnewfeatured.ui.pdf.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models.PdfReportRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models.PdfReportResponse
import retrofit2.Response


class PdfReportRepository(var pdfReportDataSource: PdfReportDataSource) {
    suspend fun getPdfReportData(



        mContext: Context,
        pdfReportRequest: PdfReportRequest,


    ): Response<PdfReportResponse> {
        return pdfReportDataSource.getPdfReportData(mContext,pdfReportRequest)
    }


}