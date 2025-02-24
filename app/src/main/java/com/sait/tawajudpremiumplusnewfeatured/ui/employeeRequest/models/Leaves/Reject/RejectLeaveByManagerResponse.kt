package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class RejectLeaveByManagerResponse(
    val `data`: Data,
):BaseResponse()
@Parcelize
data class Data(
    val error: String,
    val success: String

): Parcelable