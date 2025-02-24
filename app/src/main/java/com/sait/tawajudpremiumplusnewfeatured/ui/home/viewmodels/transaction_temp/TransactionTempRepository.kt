package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.transaction_temp

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.TransactionResponse
import retrofit2.Response


class TransactionTempRepository(var transactionDataSource: TransactionTempDataSource) {
    suspend fun getTransactionTempData(

        mContext: Context,
        transactionRequest: CommonRequest

    ): Response<TransactionResponse> {
        return transactionDataSource.getTransactionTempData(mContext,transactionRequest)
    }
}