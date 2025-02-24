package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.Admin_permissiontypes

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminPermissionRequestResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.RequestRequest
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class AdminPermissionRequestDataSource {
    suspend fun getAdminPermissionRequestData(

        mContext: Context,
        employeeId: Int,
        companyId: Int,
        entityId: Int,
        lang: String,
        type: String,

    ): Response<AdminPermissionRequestResponse> {

        val violationRequest = RequestRequest(employeeId.toString(),lang,type)
       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postAdminPermissionRequestData(employeeId.toString(),companyId,entityId,lang,type)
    }
}