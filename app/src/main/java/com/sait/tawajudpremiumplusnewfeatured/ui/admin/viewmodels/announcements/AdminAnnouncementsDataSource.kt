package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.announcements

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminAnnouncementsRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminAnnouncementsResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class AdminAnnouncementsDataSource {
    suspend fun getAdminAnnouncementsData(

        mContext: Context,
        adminRequest: AdminAnnouncementsRequest

    ): Response<AdminAnnouncementsResponse> {

       // return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postAdmin(adminRequest)


        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postAdminAnnouncementsData(
            adminRequest

            )
    }
}