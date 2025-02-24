package com.sait.tawajudpremiumplusnewfeatured.ui.register.viewmodels

import com.sait.tawajudpremiumplusnewfeatured.ui.register.models.RegisterResponse
import retrofit2.Response


class RegisterRepository(var registerDataSource: RegisterDataSource) {
    suspend fun getRegisterData(

        email: String,
        appname: String,
        packageid: Int

    ): Response<RegisterResponse> {
        return registerDataSource.getRegisterData(email,appname,packageid)
    }
}