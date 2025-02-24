package com.sait.tawajudpremiumplusnewfeatured.ui.admin.models

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse



data class AdminAnnouncementsResponse(
    val `data`: Mobile_announcementsData
): BaseResponse()

data class Mobile_announcementsData(
    val success: String
)