package com.sait.tawajudpremiumplusnewfeatured.ui.announcements.viewmodels

import android.content.Context
import android.util.Log
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementResponse
import retrofit2.Response


class AnnouncementRepository(var announcementDataSource: AnnouncementDataSource) {
    suspend fun getAnnouncementData(

        mContext: Context,

        managerEmpRequest: AnnouncementRequest

    ): Response<AnnouncementResponse> {
        Log.e("commonRequest",managerEmpRequest.toString())

        return announcementDataSource.getAnnouncementResponseData(mContext,managerEmpRequest)
    }
}