package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class PendingPermissionResponse(
    val `data`: List<PendingPermissionData>,

):BaseResponse()
@Parcelize
data class PendingPermissionData(
    val attachedFile: String,
    val days: String,
    val employeeArabicName: String,
    val employeeName: String,
    val employeeNo: String,
    val expr1: String,
    val expr2: String,
    val fK_EmployeeId: Int,
    val fK_PermId: Int,
    val flexiblePermissionDuration: Boolean,
    val fromTime: String,
    val isFlexible: Boolean,
    val isForPeriod: Int,
    val isFullDay: Boolean,
    val nextApprovalStatus: Int,
    val permArabicName: String,
    val permDate: String,
    val permEndDate: String,
    val permName: String,
    val permTypeId: Int,
    val remark: String,
    val statusName: String,
    val statusNameArabic: String,
    val toTime: String,
    val totalTime: String
) : Parcelable