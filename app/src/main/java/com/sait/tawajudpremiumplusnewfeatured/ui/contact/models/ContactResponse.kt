package com.sait.tawajudpremiumplusnewfeatured.ui.contact.models

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse


data class ContactResponse(
    val `data`: ContactData
):BaseResponse()
data class ContactData(
    val companyArabicName: String,
    val companyName: String
)