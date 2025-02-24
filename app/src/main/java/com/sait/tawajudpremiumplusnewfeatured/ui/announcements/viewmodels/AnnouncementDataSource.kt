package com.sait.tawajudpremiumplusnewfeatured.ui.announcements.viewmodels

import android.content.Context
import android.util.Log
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class AnnouncementDataSource {
    suspend fun getAnnouncementResponseData(

        mContext: Context,
        managerEmpRequest: AnnouncementRequest
    ): Response<AnnouncementResponse> {


        Log.e("commonRequest",managerEmpRequest.toString())

return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postAnnoucementsData(managerEmpRequest)
    }
}