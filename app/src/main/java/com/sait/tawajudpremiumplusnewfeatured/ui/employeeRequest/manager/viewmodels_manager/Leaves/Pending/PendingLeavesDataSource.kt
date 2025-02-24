package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.viewmodels.Leaves.Pending

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingForManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingLeavesResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class PendingLeavesDataSource {
    suspend fun getPendingLeaveData(

        mContext: Context,
        pending_leaves_request : PendingForManagerRequest
    ): Response<PendingLeavesResponse> {

       // return ApiClient.createService().postLoginData(log in)

return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postPendingLeaveData(pending_leaves_request)
    }
}