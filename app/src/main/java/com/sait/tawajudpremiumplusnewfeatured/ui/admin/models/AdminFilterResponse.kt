package com.sait.tawajudpremiumplusnewfeatured.ui.admin.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize






data class AdminFilterResponse(
    val `data`: List<AdminFilterData>,

    ): BaseResponse()


@Parcelize

data class AdminFilterData(
    val allowPunchOutSideLocation: Boolean,
    val companyArabicName: String,
    val companyId: Int,
    val companyName: String,
    val companyShortName: String,
    val entityArabicName: String,
    val entityId: Int,
    val entityName: String,
    val fK_LevelId: Int,
    val fK_ParentId: Int,
    val fK_TAPolicyId: Int,
    val groupArabicName: String,
    val groupId: Int,
    val groupName: String,
    val workLocationArabicName: String,
    val workLocationCode: String,
    val workLocationId: Int,
    val workLocationName: String
): Parcelable