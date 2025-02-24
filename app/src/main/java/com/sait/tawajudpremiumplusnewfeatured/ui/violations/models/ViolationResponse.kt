package com.sait.tawajudpremiumplusnewfeatured.ui.violations.models

import android.os.Parcelable
import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse
import kotlinx.parcelize.Parcelize

data class ViolationResponse(
    val `data`: List<ViolationData>,

): BaseResponse()


@Parcelize

data class ViolationData(
   /* val attachedFile: String?,
    val delay: String,
    val description: String,
    val descriptionAr: String,
    val duration: Int,
    val earlyOut: Int,
    val employeeArabicName: String,
    val employeeId: Int,
    val employeeName: String,
    val employeeNo: String,
    val fK_CompanyId: Int,
    val fK_EntityId: Int,
    val fK_PermId: Int,
    val fK_StatusId: Int,
    val inTime: String,
    val moveDate: String,
    val outTime: String,
    val permEnd: String,
    val permStart: String,
    val permType: String,
    val permTypeAr: String,
    val permissionId: Int,
    val remark: String,
    val status: String,
    val type: String*/


   val attachedFile: String?,
   val delay: String,
   val description: String,
   val descriptionAr: String,
   val duration: String,
   val earlyOut: Int,
   val employeeArabicName: String,
   val employeeId: Int,
   val employeeName: String,
   val employeeNo: String,
   val fK_CompanyId: Int,
   val fK_EntityId: Int,
   val fK_PermId: Int,
   val fK_StatusId: Int,
   val inTime: String,
   val moveDate: String,
   val outTime: String,
   val permEnd: String,
   val permStart: String,
   val permType: String,
   val permStartDate: String,
   val permEndDate: String,
   val permTypeAr: String,
   val permissionId: Int,
   val remark: String,
   val status: String,
   val type: String,
   val isfullDay:Boolean,
   val isForPeriod:Boolean,
   val isFlexible:Boolean,



): Parcelable