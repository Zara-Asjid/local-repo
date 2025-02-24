package com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.models.ChangePasswordRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.models.ChangePasswordResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ChangePasswordDataSource {
    suspend fun getChangePasswordData(


        mContext: Context,
        userId: Int,
        userName: String,
        newpassword: String,
        oldpassword: String,
        lang: Int
    ): Response<ChangePasswordResponse> {

        val changePassword = ChangePasswordRequest(
            userId =userId,
            username = userName,
            newPassword = newpassword,
            oldPassword = oldpassword,
            lang = lang)
       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postChangePasswordData(changePassword)
    }
}