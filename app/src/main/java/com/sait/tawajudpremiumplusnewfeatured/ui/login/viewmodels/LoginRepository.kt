package com.sait.tawajudpremiumplusnewfeatured.ui.login.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.LoginResponse
import retrofit2.Response


class LoginRepository(var loginDataSource: LoginDataSource) {
    suspend fun getLoginData(
        context: Context,
        userName: String,
        password: String,
        usetawajud_LdapPassWord: Int,
        lang: Int,
        deviceId: String,
        version: String,
        type:String,
        totalAllowedUsers:Int
    ): Response<LoginResponse> {
        return loginDataSource.getLoginData(context,userName,password,usetawajud_LdapPassWord,lang,deviceId,version,type,totalAllowedUsers)
    }
}