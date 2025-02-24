package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.WorkLocations

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.WorkLocationRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.WorkLocationResponse
import retrofit2.Response


class WorkLocationRepository(var currentDelayDataSource: WorkLocationDataSource) {
    suspend fun getWorkLocationData(

        mContext: Context,
        currentDelayRequest: WorkLocationRequest

    ): Response<WorkLocationResponse> {
        return currentDelayDataSource.getWorkLocationData(mContext,currentDelayRequest )
    }
}