package com.sait.tawajudpremiumplusnewfeatured.ui.register.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.Data
import kotlinx.parcelize.Parcelize

data class EmailValidityResponse(
    val `data`: Data,

    ):BaseResponse()

@Parcelize
data class Data(
    val msg: String
): Parcelable