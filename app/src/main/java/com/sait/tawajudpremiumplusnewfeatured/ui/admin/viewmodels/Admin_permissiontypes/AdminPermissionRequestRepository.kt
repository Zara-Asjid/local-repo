package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.Admin_permissiontypes

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminPermissionRequestResponse
import retrofit2.Response


class AdminPermissionRequestRepository(var violationDataSource: AdminPermissionRequestDataSource) {
    suspend fun getAdminPermissionRequestData(

        mContext: Context,
        employeeId: Int,
        companyId: Int,
        entityId: Int,
        lang: String,
        type: String,


    ): Response<AdminPermissionRequestResponse> {
        return violationDataSource.getAdminPermissionRequestData(mContext,employeeId,companyId,entityId,lang,type)
    }
}