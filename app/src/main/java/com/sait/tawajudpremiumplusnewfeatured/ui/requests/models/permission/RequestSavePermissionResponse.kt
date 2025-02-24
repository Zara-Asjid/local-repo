package com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class RequestSavePermissionResponse(

    val `data`: Data,

    ) : BaseResponse()

@Parcelize
data class Data(
    val msg: String
): Parcelable