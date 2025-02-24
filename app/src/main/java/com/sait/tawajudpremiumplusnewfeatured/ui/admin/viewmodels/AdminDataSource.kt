package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.AdminResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class AdminDataSource {
    suspend fun getAdminData(

        mContext: Context,
        adminRequest: AdminRequest

    ): Response<AdminResponse> {

        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postAdmin(adminRequest)


    }
}