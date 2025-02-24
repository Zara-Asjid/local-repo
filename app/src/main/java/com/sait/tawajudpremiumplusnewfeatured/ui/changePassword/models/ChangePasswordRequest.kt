package com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.models

data class ChangePasswordRequest(
    val userId: Int,
    val username: String,
    val newPassword: String,
    val oldPassword: String,
    val lang: Int
)