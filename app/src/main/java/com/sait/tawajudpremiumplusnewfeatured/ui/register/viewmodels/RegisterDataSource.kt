package com.sait.tawajudpremiumplusnewfeatured.ui.register.viewmodels

import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.register.models.RegisterRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.register.models.RegisterResponse
import retrofit2.Response

class RegisterDataSource {
    suspend fun getRegisterData(
        email: String,
        appname: String,
        packageid: Int
    ): Response<RegisterResponse> {

        val register = RegisterRequest(Email = email, AppName = appname, PackageID = packageid)

        return ApiClient.createService().postRegisterData(register)

    }
}