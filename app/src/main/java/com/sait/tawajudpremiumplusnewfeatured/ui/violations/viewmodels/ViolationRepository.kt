package com.sait.tawajudpremiumplusnewfeatured.ui.violations.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationResponse
import retrofit2.Response


class ViolationRepository(var violationDataSource: ViolationDataSource) {
    suspend fun getViolationData(

        mContext: Context,
        employeeId: Int,
        from_date: String,
        to_date: String,
        lang: String

    ): Response<ViolationResponse> {
        return violationDataSource.getViolationData(mContext,employeeId,from_date,to_date,lang)
    }
}