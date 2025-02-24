package com.sait.tawajudpremiumplusnewfeatured.ui.calendar.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models.CalendarRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models.CalendarResponse
import retrofit2.Response


class CalendarRepository(var calendarDataSource: CalendarDataSource) {
    suspend fun getCalendarData(

        mContext: Context,
        calendarRequest :CalendarRequest

    ): Response<CalendarResponse> {
        return calendarDataSource.getCalendarData(mContext,calendarRequest)
    }
}