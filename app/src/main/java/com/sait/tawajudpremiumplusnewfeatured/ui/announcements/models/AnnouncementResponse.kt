package com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize


data class AnnouncementResponse(
    val `data`: List<AnnouncementData>

): BaseResponse()
@Parcelize
data class AnnouncementData(
    val altered_By: String,
    val altered_Date: String,
    val announcementDate: String,
    val content_Ar: String,
    val content_En: String,
    val created_By: String,
    val created_Date: String,
    val dayNo: Int,
    val employeeId: Int,
    val id: Int,
    val monthArabic: String,
    val monthEnglish: String,
    val monthNo: Int,
    val title_Ar: String,
    val title_En: String,
    var isExpand:Boolean = false,
    var isRead: Boolean = false

) : Parcelable