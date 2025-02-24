package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Permissions.Approve

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveResponse
import retrofit2.Response

class onClickApproveRepository(var reject_leave_datasource: onClickApprovePermDataSource) {
    suspend fun getonclickApprovePermissionData(

        mContext: Context,
        onclickapprove_permission_request: onClickApproveRequest

    ): Response<onClickApproveResponse> {
        return reject_leave_datasource.getApproveData(mContext,onclickapprove_permission_request)
    }
}