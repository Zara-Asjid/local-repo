package com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual

data class ManualEntryResponse(
    val `data`: Data,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)

data class Data(
    val msg: String
)