package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.filter

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class AdminFilterDataSource {
    suspend fun getAdminFilterData(

        mContext: Context,
        adminRequest: AdminFilterRequest

    ): Response<AdminFilterResponse> {



        return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postMobileOrgLevelFilterData(
            adminRequest.FK_CompanyId,adminRequest.FilterType

            )
    }
}