package com.sait.tawajudpremiumplusnewfeatured.ui.reports.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class Reports_Self_ServiceResponse(
    val `data`: List<Reports_Self_Sevice_Data>,

    ): BaseResponse()


@Parcelize

data class Reports_Self_Sevice_Data(
    val formID: Int,
    val desc_En: String,
    val desc_Ar: String,
    val reportType: String,
    val personReportType: Int,
    val reportimage: String
): Parcelable