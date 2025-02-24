package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse

data class onClickApproveResponse(
    val `data`: Data,

    ):BaseResponse()
data class Data(
    val error: String,
    val success:String

)