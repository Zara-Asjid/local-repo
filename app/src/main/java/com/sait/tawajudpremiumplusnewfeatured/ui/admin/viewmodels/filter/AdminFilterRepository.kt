package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.filter

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterResponse
import retrofit2.Response





class AdminFilterRepository(var transactionDataSource: AdminFilterDataSource) {
    suspend fun getAdminFilterData(

        mContext: Context,
        adminRequest: AdminFilterRequest

    ): Response<AdminFilterResponse> {
        return transactionDataSource.getAdminFilterData(mContext,adminRequest)
    }
}