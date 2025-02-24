package com.sait.tawajudpremiumplusnewfeatured.ui.login.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.LoginRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.LoginResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response
class LoginDataSource {
    suspend fun getLoginData(
        context: Context,
        userName: String,
        password: String,
        usetawajudLdappassword: Int,
        lang: Int,
        registeredDeviceID: String,
        version: String,
        type: String,
        totalAllowedUsers:Int

        ): Response<LoginResponse> {
        val login = LoginRequest(userName,password,usetawajudLdappassword,lang,registeredDeviceID,version, type,totalAllowedUsers)
        // return ApiClient.createService().postLoginData(login)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(context).toString()).postLoginData(login)
    }


}