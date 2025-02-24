package com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission

data class RequestSavePermissionRequest(
    val permissionId: Int,
    val FK_EmployeeIds: String,
    val permissionTypeId: Int,
    val permissionDate: String,
    val fromTime: String,
    val toTime: String,
    val isFullDay: Boolean,
    val remarks: String,
    val isForPeriod: Boolean,
    val permEndDate: String,
    val isFlexible: Boolean,
    val flexibilePermissionDuration: Int,
    val attachedFile: String,
    val fileType: String,
    val lang: Int,

    val leaveId: Int,
    val isValidInSchedule: Boolean,
    val isOff: Boolean,
    val isHoliday: Boolean,
    val allowBefore: Int,
    val previousPermDate: String,
    val userId: Int,
    val fK_UniversityId: Int,
    val emp_GPAType: Int,
    val emp_GPA: Int,
    val fK_MajorId: Int,
    val fK_SpecializationId: Int,
    val gpid: Int,
    val workGroupID: Int,
    val fK_EmployeeId:Int





)