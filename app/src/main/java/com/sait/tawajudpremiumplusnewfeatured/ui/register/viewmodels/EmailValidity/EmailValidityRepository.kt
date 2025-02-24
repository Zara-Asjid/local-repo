package com.sait.tawajudpremiumplusnewfeatured.ui.register.viewmodels.EmailValidity

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.register.models.EmailValidityResponse
import retrofit2.Response

class EmailValidityRepository(var registerDataSource: EmailValidityDataSource) {
    suspend fun getRegisterData(
        email: String,
        mcontext: Context
    ): Response<EmailValidityResponse> {
        return registerDataSource.getValidityData(email, mcontext)
    }
}