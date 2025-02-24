package com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.viewmodels

import android.content.Context
import com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.models.ChangePasswordResponse
import retrofit2.Response


class ChangePasswordRepository(var changePasswordDataSource: ChangePasswordDataSource) {
    suspend fun getChangePasswordData(



        mContext: Context,
        userId: Int,
        userName: String,
        newpassword: String,
        oldpassword: String,
        lang: Int

    ): Response<ChangePasswordResponse> {
        return changePasswordDataSource.getChangePasswordData(mContext,userId,userName,newpassword,oldpassword,lang)
    }


}