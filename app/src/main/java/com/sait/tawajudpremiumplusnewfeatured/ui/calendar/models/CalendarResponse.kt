package com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class CalendarResponse(
    val `data`: List<CalendarData>,
  ): BaseResponse()

@Parcelize

data class CalendarData(
    val apptType: String,
    val days: @RawValue Any,
    val duration: @RawValue Any,
    val excludeHolidays: Boolean,
    val excludeOffDays: Boolean,
    val flexibilePermissionDuration: @RawValue Any,
    var fromDate: String,
    val fromTime: String,
    val id: Int,
    val isFlexible: @RawValue Any,
    val textAr: String,
    val textEn: String,
    val toDate: String,
    val toTime: String
): Parcelable