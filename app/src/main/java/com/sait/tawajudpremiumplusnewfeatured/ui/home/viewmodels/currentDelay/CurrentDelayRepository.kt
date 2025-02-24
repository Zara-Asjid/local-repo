package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.currentDelay

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayResponse
import retrofit2.Response


class CurrentDelayRepository(var currentDelayDataSource: CurrentDelayDataSource) {
    suspend fun getCurrentDelayData(

        mContext: Context,
        currentDelayRequest: CurrentDelayRequest

    ): Response<CurrentDelayResponse> {
        return currentDelayDataSource.getCurrentDelayData(mContext,currentDelayRequest )
    }
}