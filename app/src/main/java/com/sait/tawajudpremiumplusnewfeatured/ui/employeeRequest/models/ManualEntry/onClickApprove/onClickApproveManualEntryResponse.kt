package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse

data class onClickApproveManualEntryResponse(
    val `data`: Data,

    ): BaseResponse()
data class Data(
    val success: String,
    val error: String

)
