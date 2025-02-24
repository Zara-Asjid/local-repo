package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class Approve_LeavesResponse(
    val `data`: List<ApproveLeaveData>,

) :BaseResponse()
@Parcelize
data class ApproveLeaveData(
    val attachedFile: String,
    val days: Int,
    val employeeArabicName: String,
    val employeeName: String,
    val employeeNo: String,
    val fK_EmployeeId: Int,
    val fromDate: String,
    val hasAdvancedSalary: Boolean,
    val leaveArabicName: String,
    val leaveId: Int,
    val leaveName: String,
    val leaveTypeId: Int,
    val requestDate: String,
    val statusName: String,
    val statusNameArabic: String,
    val toDate: String,
    val totalBalance: Int
):Parcelable