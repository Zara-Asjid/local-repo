package com.sait.tawajudpremiumplusnewfeatured.ui.calendar.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models.CalendarRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models.CalendarResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class CalendarDataSource {
    suspend fun getCalendarData(

        mContext: Context,
        calendarRequest: CalendarRequest

    ): Response<CalendarResponse> {

       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postCalendarData(calendarRequest)
    }
}