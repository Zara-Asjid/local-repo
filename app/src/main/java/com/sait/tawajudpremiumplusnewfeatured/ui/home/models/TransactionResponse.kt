package com.sait.tawajudpremiumplusnewfeatured.ui.home.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class TransactionResponse(
    val `data`: List<TransactionData>,
): BaseResponse()


@Parcelize
data class TransactionData(
    val dMoveTime: String,
    val moveDate: String,
    val moveId: Int,
    val moveTime: String,
    val reasonAr: String,
    val reasonEn: String,
    val workLocationArabicName: String,
    val workLocationName: String,
    val reasonId :Int,
    val expectOutValue:String,
    val type:String,
    val firstIN:String,
    val lastOUT:String
): Parcelable





