package com.sait.tawajudpremiumplusnewfeatured.ui.requests.models

import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse



data class RequestResponse(
    val `data`: RequestData
): BaseResponse()
data class RequestData(
    val leaveTypes1: List<LeaveTypes1>,
    val permissionsTypes1: List<PermissionsTypes1>
)



data class LeaveTypes1(
    val allowIfBalanceOver: Boolean,
    val allowedForSelfService: Boolean,
    val balance: Int,
    val createD_BY: Any,
    val createD_DATE: String,
    val excludeHolidays: Boolean,
    val excludeOffDays: Boolean,
    val expiredBalanceIsCashed: Boolean,
    val fK_ParentLeaveType: Int,
    val generalGuide: String,
    val generalGuideAr: String,
    val isAnnual: Boolean,
    val lasT_UPDATE_BY: Any,
    val lasT_UPDATE_DATE: String,
    val leaveApproval: Boolean,
    val maxDuration: Int,
    val maxOccurancePerPeriod: Int,
    val maxRoundBalance: Int,
    val minDuration: Int,
    val minServiceDays: Int,
    val monthlyBalancing: Boolean,
    val paymentConsidration: Int,
    val permArabicName: String,
    val permId: Int,
    val permName: String,
    val attachmentIsMandatory:Boolean,
    val remarksIsMandatory: Boolean

)

data class PermissionsTypes1(
    val allowForSpecificEmployeeType: Boolean,
    val allowWhenInSufficient: Boolean,
    val allowedAfter: Int,
    val allowedAfterDays: String,
    val allowedBefore: Int,
    val allowedBeforeDays: Any,
    val allowedDurationPerPeriod: Int,
    val allowedForManagers: Boolean,
    val allowedForSelfService: Boolean,
    val allowedOccurancePerPeriod: String,
    val annualLeaveId_ToDeductPermission: Int,
    val approvalRequired: Boolean,
    val attachmentIsMandatory: Boolean,
    val autoApproveAfter: Any,
    val autoApprovePolicy: Any,
    val considerRequestWithinBalance: Boolean,
    val convertToLeave_ExceedDuration: Int,
    val createD_BY: String,
    val createD_DATE: String,
    val deductBalanceFromOvertime: Boolean,
    val delayPermissionsValidation: Any,
    val durationAllowedwithleave: Int,
    val excludeManagers_FromAfterBefore: Boolean,
    val fK_EmployeeTypeId: Int,
    val fK_LeaveIdDeductBalance: Int,
    val fK_LeaveIdtoallowduration: Int,
    val fK_RelatedTAReason: Int,
    val generalGuide: String,
    val generalGuideAr: String,
    val hasFlexiblePermission: Boolean,
    val hasFullDayPermission: Boolean,
    val hasPermissionForPeriod: Boolean,
    val hasPermissionTimeControls: Boolean,
    val isAllowedBeforeTime: Boolean,
    val isAutoApprove: Boolean,
    val isConsiderInWork: Boolean,
    val isSpecificCompany: Boolean,
    val isSpecificEntity: Boolean,
    val isallowedaftertime: Boolean,
    val lasT_UPDATE_BY: String,
    val lasT_UPDATE_DATE: String,
    val maxDuration: Int,
    val minDuration: Int,
    val minDurationAllowedInSelfService: Any,
    val monthlyBalance: Int,
    val mustHaveTransaction: Boolean,
    val notAllowedWhenHasStudyOrNursing: Boolean,
    val overtimeBalanceDays: Int,
    val permArabicName: String,
    val permId: Int,
    val permName: String,
    val perm_NotificationException: Boolean,
    val permissionApproval: Int,
    val permissionRequestManagerLevelRequired: Int,
    val remarksIsMandatory: Boolean,
    val shouldComplete50WHRS: Boolean,
    val showRemainingBalance: Boolean,
    val validateDelayPermissions: Boolean
)