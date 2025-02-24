package com.sait.tawajudpremiumplusnewfeatured.ui.login.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize

data class NoficationTypesResponse(
    @SerializedName("data")
    val `data`: List<NotificationData>,
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: Int
) :  Parcelable
@Parcelize
data class NotificationData(
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("notificationArabicTitle")
    val notificationArabicTitle: String,
    @SerializedName("notificationEnglishTitle")
    val notificationEnglishTitle: String,
    @SerializedName("tawajud_NiotificationsTypesId")
    val tawajud_NiotificationsTypesId: Int,
    @SerializedName("templateArabic")
    val templateArabic: String,
    @SerializedName("templateEnglish")
    val templateEnglish: String
) : Parcelable