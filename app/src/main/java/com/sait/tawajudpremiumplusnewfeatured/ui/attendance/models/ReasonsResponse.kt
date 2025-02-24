package com.sait.tawajudpremiumplusnewfeatured.ui.attendance.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize




data class ReasonsResponse(
    val `data`: List<ReasonsData>,

    ): BaseResponse()

@Parcelize

data class ReasonsData(
    val isConsiderInside: Boolean,
    val isDisplayType: Boolean,
    val isFirstIn: Boolean,
    val isLastOut: Boolean,
    val isScheduleTiming: Boolean,
    val reasonArabicName: String,
    val reasonCode: Int,
    val reasonName: String,
    val type: String,
    val colorCode: String,
    val attachmentIsMandatory: Boolean,
    val remarksIsMandatory: Boolean
): Parcelable
