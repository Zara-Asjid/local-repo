package com.sait.tawajudpremiumplusnewfeatured.ui.attendance.viewmodels.reasons

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.models.ReasonsResponse
import retrofit2.Response


class ReasonRepository(var reasonsDataSource: ReasonsDataSource) {
    suspend fun getReasonsData(

        mContext: Context
    ): Response<ReasonsResponse> {
        return reasonsDataSource.getReasonsData(mContext)
    }
}