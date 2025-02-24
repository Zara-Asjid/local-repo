package com.sait.tawajudpremiumplusnewfeatured.ui.complaints.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.complaints.models.ComplaintsResponse
import retrofit2.Response


class ComplaintsRepository(var contactDataSource: ComplaintsDataSource) {
    suspend fun getContactData(
        mContext: Context,
        employeeId: String,
        lang: String
    ): Response<ComplaintsResponse> {
        return contactDataSource.getContactData(mContext,employeeId,lang)
    }
}