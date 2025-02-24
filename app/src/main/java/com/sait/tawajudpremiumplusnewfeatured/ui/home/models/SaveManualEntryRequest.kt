package com.sait.tawajudpremiumplusnewfeatured.ui.home.models

data class SaveManualEntryRequest(


    val employeeId: Int,
    val requestDate: String,
    val reasonId: Int,
    val requestEntryTime: String,
    val remarks: String,
    val gpsCoordinate: String,
    val workLocation: Int,
    val allowedGPSType: Int,

    val mustPunchPhysical: Boolean,
    val registeredDevice: String,


    val lang: Int

    )



