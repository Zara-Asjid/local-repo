package com.sait.tawajudpremiumplusnewfeatured.ui.register.viewmodels.EmailValidity

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.register.models.EmailValidityResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class EmailValidityDataSource {
    suspend fun getValidityData(
        email: String,
        mcontext:Context
    ): Response<EmailValidityResponse> {


        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mcontext).toString()).postEmailValidityData(email)

    }
}