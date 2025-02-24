package com.sait.tawajudpremiumplusnewfeatured.ui.pdf.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models.PdfReportRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models.PdfReportResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class PdfReportDataSource {
    suspend fun getPdfReportData(


        mContext: Context,
        pdfReportRequest: PdfReportRequest,

    ): Response<PdfReportResponse> {


return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postPdfGenerateRequestData(pdfReportRequest)
    }
}