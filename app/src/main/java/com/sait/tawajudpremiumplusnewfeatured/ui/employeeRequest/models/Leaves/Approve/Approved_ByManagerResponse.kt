package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class Approved_leaveResponse(
    val `data`: ApprovesLeaveData,

    ):BaseResponse()
@Parcelize
data class ApprovesLeaveData(
    val error: String,
    val success: String


) : Parcelable