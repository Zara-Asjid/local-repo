package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.AdminResponse
import retrofit2.Response





class AdminRepository(var transactionDataSource: AdminDataSource) {
    suspend fun getAdminData(

        mContext: Context,
        adminRequest: AdminRequest

    ): Response<AdminResponse> {
        return transactionDataSource.getAdminData(mContext,adminRequest)
    }
}