package com.sait.tawajudpremiumplusnewfeatured.ui.complaints.models

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.contact.models.ContactData


data class ComplaintsResponse(
    val `data`: ContactData
):BaseResponse()
data class ComplaintsData(
    val companyArabicName: Any,
    val companyName: Any
)