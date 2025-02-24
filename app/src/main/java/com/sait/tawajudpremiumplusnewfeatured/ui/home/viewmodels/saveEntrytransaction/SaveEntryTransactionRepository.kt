package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.transaction

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.SaveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.SaveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.saveEntrytransaction.SaveEntryTransactionDataSource
import retrofit2.Response


class SaveEntryTransactionRepository(private var saveEntrytransactionDataSource: SaveEntryTransactionDataSource) {
    suspend fun getSaveEntryTransactionData(

        mContext: Context,
        transactionRequest: SaveManualEntryRequest

    ): Response<SaveManualEntryResponse> {
        return saveEntrytransactionDataSource.getSaveEntryTransactionData(mContext,transactionRequest)
    }
}