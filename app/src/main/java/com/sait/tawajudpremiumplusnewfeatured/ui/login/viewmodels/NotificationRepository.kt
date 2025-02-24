package com.sait.tawajudpremiumplusnewfeatured.ui.login.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.LoginResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.NoficationTypesResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.NotificationRequest
import retrofit2.Response

class NotificationRepository(var notifcationDataSource: NotificationDataSource) {


    suspend fun getNotificationData(
        context: Context,
        notificationRequest: NotificationRequest
    ): Response<NoficationTypesResponse> {
        return notifcationDataSource.getNoficationData(context,notificationRequest)
    }
}