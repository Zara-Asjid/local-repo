package com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse


data class RequestSaveLeaveResponse(
    val `data`: SaveData
): BaseResponse()

data class SaveData(
    val msg: String
)