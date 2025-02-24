package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Leave.Pending

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingForManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingHRLeavesResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class PendingHRLeavesDataSource {
    suspend fun getPendingHRLeaveData(

        mContext: Context,
        pending_leaves_request : PendingForManagerRequest
    ): Response<PendingHRLeavesResponse> {

       // return ApiClient.createService().postLoginData(log in)

return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postPendingHRLeaveData(pending_leaves_request)
    }
}