package com.sait.tawajudpremiumplusnewfeatured.ui.manager.viewmodels

import android.content.Context
import android.util.Log
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.ManagerEmpRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.ManagerEmpResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class Manager_empDataSource {
    suspend fun getManager_empResponseData(

        mContext: Context,
        managerEmpRequest: ManagerEmpRequest
    ): Response<ManagerEmpResponse> {


        Log.e("commonRequest",managerEmpRequest.toString())
      //  val manager_empRequest = CommonRequest(UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,commonRequest.Lang)
       // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postEmpManagerData(managerEmpRequest)
    }
}