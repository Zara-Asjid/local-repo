package com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.requestType

data class RequestTypeResponse(
    val `data`: List<RequestTypeData>,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)

data class RequestTypeData(
    val desc_Ar: String,
    val desc_En: String,
    val formID: Int,
    val requestType: String
)