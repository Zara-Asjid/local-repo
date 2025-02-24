package com.sait.tawajudpremiumplusnewfeatured.data.model


import com.google.gson.annotations.SerializedName

open class BaseResponse(

    @SerializedName("statusCode")
    var statusCode: Int? = null,


    @SerializedName("isSuccess")
    var isSuccess: Boolean = false,

    @SerializedName("message")
    var message: String? = null
)


