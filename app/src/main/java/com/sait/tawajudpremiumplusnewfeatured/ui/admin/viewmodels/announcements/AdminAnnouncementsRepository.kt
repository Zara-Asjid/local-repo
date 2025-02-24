package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.announcements

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminAnnouncementsRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminAnnouncementsResponse
import retrofit2.Response





class AdminAnnouncementsRepository(var transactionDataSource: AdminAnnouncementsDataSource) {
    suspend fun getAdminAnnouncementsData(

        mContext: Context,
        adminRequest: AdminAnnouncementsRequest

    ): Response<AdminAnnouncementsResponse> {
        return transactionDataSource.getAdminAnnouncementsData(mContext,adminRequest)
    }
}