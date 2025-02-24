package com.sait.tawajudpremiumplusnewfeatured.ui.login.models

import com.google.gson.annotations.SerializedName


    data class LoginRequest(
        @SerializedName("userName") val userName: String?,
        @SerializedName("password") val password: String?,
        @SerializedName("usetawajudLdappassword") val  usetawajud_LdapPassWord:Int?,
        @SerializedName("lang") val lang: Int?,
        @SerializedName("registeredDeviceID") val  registeredDeviceID:String?,
@SerializedName("version") val  version:String?,
@SerializedName("type") val  type:String?,
        @SerializedName("totalAllowedUsers") val  totalAllowedUsers:Int?

)