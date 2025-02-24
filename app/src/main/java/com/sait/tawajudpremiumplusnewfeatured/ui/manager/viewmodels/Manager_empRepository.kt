package com.sait.tawajudpremiumplusnewfeatured.ui.manager.viewmodels

import android.content.Context
import android.util.Log
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.ManagerEmpRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.ManagerEmpResponse
import retrofit2.Response


class Manager_empRepository(var manager_empDataSource: Manager_empDataSource) {
    suspend fun getManager_empData(

        mContext: Context,

        managerEmpRequest: ManagerEmpRequest

    ): Response<ManagerEmpResponse> {
        Log.e("commonRequest",managerEmpRequest.toString())

        return manager_empDataSource.getManager_empResponseData(mContext,managerEmpRequest)
    }
}