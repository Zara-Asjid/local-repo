package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class DashboardResponse(
    val `data`: List<DashboardData>,

    ): BaseResponse()


@Parcelize

data class DashboardData(
    val descrip: String,
    val number: Long,
    val name: String,
    val percentage: Float,
    val colorCode : String
): Parcelable