package com.sait.tawajudpremiumplusnewfeatured.ui.home.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize



data class WorkLocationResponse(
    val `data`: List<WorkLocationData>
): BaseResponse()
@Parcelize


data class WorkLocationData(
    val active: Boolean,
    val allowedDistance: Int,
    val allowedGPSFlag: Int,
    val createD_BY: String,
    val createD_DATE: String,
    val fK_TAPolicyId: Int,
    val gpsCoordinates: String,
    val hasMobilePunch: Boolean,
    val isBeaconEnabled: Boolean,
    val lasT_UPDATE_BY: String,
    val lasT_UPDATE_DATE: String,
    val mobilePunchConsiderDuration: Int,
    val mustPunchPhysical: Boolean,
    val workLocationArabicName: String,
    val workLocationCode: String,
    val workLocationId: Int,
    val workLocationName: String
): Parcelable