package com.sait.tawajudpremiumplusnewfeatured.ui.register.models

data class RegisterResponse(
    val ClientEmail: String,
    val MobileNo: String,
    val NoOfUsers: String,
    val PackageName: String,
    val StartDate: String,
    val Success_Code: Int,
    val Success_Msg: String,
    val SupportEndDate: String,
    val URL: String,
    val VersionName: String,
    val ClientName: String,
    val ClientShortName: String
)