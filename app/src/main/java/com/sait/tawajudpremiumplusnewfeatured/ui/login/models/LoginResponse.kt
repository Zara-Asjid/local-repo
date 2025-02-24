package com.sait.tawajudpremiumplusnewfeatured.ui.login.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("data")
    val `data`: LoginData,
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int
) : Parcelable


@Parcelize
data class LoginData(
    @SerializedName("allowAutoPunch")
    val allowAutoPunch: Boolean,
    @SerializedName("defaultEmaiLang")
    val defaultEmaiLang: String,
    @SerializedName("defaultSMSLang")
    val defaultSMSLang: String,
    @SerializedName("employeeArabicName")
    val employeeArabicName: String,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("employeeNo")
    val employeeNo: String,
    @SerializedName("fK_EmployeeId")
    val fKEmployeeId: Int,
    @SerializedName("fK_EmployeeTypeId")
    val fKEmployeeTypeId: Int,
    @SerializedName("fK_LogicalGroup")
    val fKLogicalGroup: Int,
    @SerializedName("hasMobilePunch")
    val hasMobilePunch: Boolean,
    @SerializedName("hasPermissionTypes")
    val hasPermissionTypes: Boolean,
    @SerializedName("hasSelfServiceReports")
    val hasSelfServiceReports: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isHR")
    val isHR: Boolean,
    @SerializedName("isManager")
    val isManager: Boolean,
    @SerializedName("isSelfService")
    val isSelfService: Boolean,
    @SerializedName("role")
    val role: String,
    @SerializedName("scheduleName")
    val scheduleName: String,
    @SerializedName("showDashEmpViewer")
    val showDashEmpViewer: ShowDashEmpViewer,
    @SerializedName("token")
    val token: String,
    @SerializedName("userEmail")
    val userEmail: String,
    @SerializedName("user_FullName")
    val userFullName: String,
    @SerializedName("userID")
    val userID: String,
    @SerializedName("error")
    val error: String,
    @SerializedName("allowFingerPunch")
    val allowFingerPunch: Boolean,
    @SerializedName("allowFingerLogin")
    var allowFingerLogin: Boolean,
    @SerializedName("allowFacePunch")
    var allowFacePunch: Boolean,
    @SerializedName("allowFaceLogin")
    var allowFaceLogin: Boolean,
    @SerializedName("hasRequested_FaceEnrollment")
    var hasRequestedFaceEnrollment: Boolean,
    @SerializedName("userImage")
    var userImage: String,
    @SerializedName("userImage_Ext")
    var userImageExt: String,
    @SerializedName("requestedDate")
    var requestedDate: String,
    @SerializedName("validationDays")
    var validationDays: Int,
    @SerializedName("minutelyReminder")
    var minutelyReminder: Int,
    @SerializedName("customReminder")
    var customReminder: Int,
    @SerializedName("allowMultipleLocations")
    var allowMultipleLocations: Boolean

) : Parcelable


@Parcelize
class ShowDashEmpViewer(
) : Parcelable