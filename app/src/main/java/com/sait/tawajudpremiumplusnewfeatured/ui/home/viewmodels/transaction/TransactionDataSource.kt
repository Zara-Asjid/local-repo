package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.transaction

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.TransactionResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class TransactionDataSource {
    suspend fun getTransactionData(

        mContext: Context,
        transactionRequest: CommonRequest

    ): Response<TransactionResponse> {




        // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postTransactionData(
    transactionRequest.FK_EmployeeId,
    UserShardPrefrences.getLanguage(mContext))
    }
}