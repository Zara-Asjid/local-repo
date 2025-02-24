package com.sait.tawajudpremiumplusnewfeatured.ui.login.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.NoficationTypesResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.NotificationRequest

import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class NotificationDataSource {
    suspend fun getNoficationData(
        context: Context,
        notificationRequest: NotificationRequest,
        ): Response<NoficationTypesResponse> {
        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(context).toString()).postNotificationData(notificationRequest.Lang)
    }
}