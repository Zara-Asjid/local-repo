package com.sait.tawajudpremiumplusnewfeatured.ui.home.models

data class LocationResponse(
    val active: Boolean,
    val allowMobileOutpunch: Boolean,
    val allowOnlyFirstINForCarPark: Boolean,
    val allowedDistance: Int,
    val allowedGPSFlag: Int,
    val gpsCoordinates: String,
    val hasMobilePunch: Boolean,
    val mobilePunchConsiderDuration: Int,
    val mustPunchPhysical: Boolean,
    val validateSecondIN: Boolean,
    val workLocationArabicName: String,
    val workLocationId: Int,
    val workLocationName: String

    )

