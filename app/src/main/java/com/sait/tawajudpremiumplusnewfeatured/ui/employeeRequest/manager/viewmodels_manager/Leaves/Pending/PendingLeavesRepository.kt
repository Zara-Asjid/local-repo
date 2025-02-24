package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.viewmodels.Leaves.Pending

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingForManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingLeavesResponse
import retrofit2.Response


class PendingLeavesRepository(var pending_leave_datasource: PendingLeavesDataSource) {
    suspend fun getPendingLeavesData(

        mContext: Context,
        pending_leaves_request: PendingForManagerRequest

    ): Response<PendingLeavesResponse> {

        return pending_leave_datasource.getPendingLeaveData(mContext,pending_leaves_request)
    }
}