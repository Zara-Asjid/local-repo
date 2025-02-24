package com.sait.tawajudpremiumplusnewfeatured.ui.contact.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.contact.models.ContactResponse
import retrofit2.Response


class ContactRepository(var contactDataSource: ContactDataSource) {
    suspend fun getContactData(
        mContext: Context,
        employeeId: String,
        lang: String
    ): Response<ContactResponse> {
        return contactDataSource.getContactData(mContext,employeeId,lang)
    }
}