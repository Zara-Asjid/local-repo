package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.transaction

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.TransactionResponse
import retrofit2.Response


class TransactionRepository(var transactionDataSource: TransactionDataSource) {
    suspend fun getTransactionData(

        mContext: Context,
        transactionRequest: CommonRequest

    ): Response<TransactionResponse> {
        return transactionDataSource.getTransactionData(mContext,transactionRequest)
    }
}