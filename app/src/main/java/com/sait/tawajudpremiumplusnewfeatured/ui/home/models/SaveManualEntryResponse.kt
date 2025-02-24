package com.sait.tawajudpremiumplusnewfeatured.ui.home.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize


data class SaveManualEntryResponse(
    val `data`: EntryData
): BaseResponse()


@Parcelize
data class EntryData(
    val msg: String
): Parcelable