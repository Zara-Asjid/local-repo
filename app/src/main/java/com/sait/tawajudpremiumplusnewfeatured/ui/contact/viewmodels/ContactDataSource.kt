package com.sait.tawajudpremiumplusnewfeatured.ui.contact.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.contact.models.ContactRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.contact.models.ContactResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class ContactDataSource {
    suspend fun getContactData(
        mContext: Context,
        employeeId: String,
        lang: String
    ): Response<ContactResponse> {

        val contact = ContactRequest(EmployeeId = employeeId, Lang = lang)
        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).getContactCompanyData(employeeId,lang)
     //   return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).getContactCompanyData(contact)

    }
}