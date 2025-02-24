package com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.models


import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse

data class ChangePasswordResponse(
    val `data`: ChangePasswordData
):BaseResponse()

data class ChangePasswordData(
    val msg: String
)