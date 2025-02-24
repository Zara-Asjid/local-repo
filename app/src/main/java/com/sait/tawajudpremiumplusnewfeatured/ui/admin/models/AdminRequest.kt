package com.sait.tawajudpremiumplusnewfeatured.ui.admin.models

data class AdminRequest(
    val companyId: Int,
    val entityId: Int,
    val filterType: Int,
    val pageNo: Int,
    val pageSize: Int,
    val userId: Int,
    val workGroupID: Int,
    val workLocationsID: Int
)