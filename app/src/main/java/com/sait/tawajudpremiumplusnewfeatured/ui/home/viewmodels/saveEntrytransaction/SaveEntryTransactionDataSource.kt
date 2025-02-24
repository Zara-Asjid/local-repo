package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.saveEntrytransaction

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.data.remote.ApiClient
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.SaveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.SaveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import retrofit2.Response

class SaveEntryTransactionDataSource {
    suspend fun getSaveEntryTransactionData(

        mContext: Context,
        transactionRequest: SaveManualEntryRequest

    ): Response<SaveManualEntryResponse> {




        // return ApiClient.createService().postLoginData(login)
return ApiClient.createService(UserShardPrefrences.getBaseUrl(mContext).toString()).postSaveEntryTransactionData(
    transactionRequest)
    }
}