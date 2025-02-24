package com.sait.tawajudpremiumplusnewfeatured.data.remote

import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.*
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve.Approved_ByManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve.Approved_leaveResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingHRManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickReject.onClickRejectManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.*
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminAnnouncementsRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminAnnouncementsResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminPermissionRequestResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.models.ReasonsResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models.CalendarRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models.CalendarResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.models.ChangePasswordRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.models.ChangePasswordResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.complaints.models.ComplaintsResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.contact.models.ContactResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.LoginRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.LoginResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.register.models.RegisterRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.register.models.RegisterResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.*
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.NoficationTypesResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.AdminResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.ManagerEmpRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.ManagerEmpResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models.PdfReportRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models.PdfReportResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.register.models.EmailValidityResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.models.Reports_Self_Service_Request
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.models.Reports_Self_ServiceResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.RequestResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave.RequestSaveLeaveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave.RequestSaveLeaveResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual.ManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual.ManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.requestType.RequestTypeResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.models.SummaryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.models.SummaryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * All api calls are defined in this interface
 */
interface ApiService {

   /* @Headers("Content-Type:application/json")
    @Headers("Host:proxy.example.com")
         "Host:proxy.example.com"*/

   /* @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
          "Accept: application/json;charset=utf-8",
        "Cache-Control: max-age=640000"
    )*/

/*

    @Headers(
        "Content-Type: application/json;charset=utf-8",
    )
    //@Headers("Accept:application/json")
    @POST("Security/Mobile_UserLogin")
    suspend fun getLoginData(
        @Query("userName") userName: String?,
        @Query("password") password: String?,
        @Query("lang") lang: String?
    ): Response<LoginResponse>

*/



    @Headers(
        "Content-Type: application/json;charset=utf-8",
    )
    @POST("Common/Mobile_GetWorkLocations")
    suspend fun postWorkLocations(@Body workLocationRequest: WorkLocationRequest):Response<WorkLocationResponse>
    @POST("Security/Mobile_UserLogin")
    suspend fun postLoginData(@Body loginRequest: LoginRequest):Response<LoginResponse>

   /* @POST("Registration/PostWebServiceLink")
    suspend fun postRegisterData(@Body registerRequest: RegisterRequest):Response<RegisterResponse>*/
    @POST("PostWebServiceLink")
    suspend fun postRegisterData(@Body registerRequest: RegisterRequest):Response<RegisterResponse>


    @POST("Security/ChangePassword")
    suspend fun postChangePasswordData(@Body changePasswordRequest: ChangePasswordRequest):Response<ChangePasswordResponse>

    @POST("Employee/Mobile_GetEmployeeViolations")
    suspend fun postViolationsData(@Body violationRequest: ViolationRequest):Response<ViolationResponse>



    @POST("Employee/Mobile_GetEmployeeManagerLists")
    suspend fun postEmpManagerData(@Body empManagerRequest: ManagerEmpRequest):Response<ManagerEmpResponse>

    @POST("Common/Mobile_GetAnnouncement")
    suspend fun postAnnoucementsData(@Body announcementRequest: AnnouncementRequest):Response<AnnouncementResponse>




    @POST("Report/Mobile_GetSelfServiceReportLists")
    suspend fun postReportsData(@Body reportsRequest: Reports_Self_Service_Request):Response<Reports_Self_ServiceResponse>

    @POST("EmpTransactions/Mobile_GetPendingLeavesForManager")
    suspend fun postPendingLeaveData(@Body pending_leaves_request: PendingForManagerRequest) :Response<PendingLeavesResponse>


    @POST("HR/Mobile_GetEmployee_LeavesRequest_ByHR")
    suspend fun postPendingHRLeaveData(@Body pending_leaves_request: PendingForManagerRequest) :Response<PendingHRLeavesResponse>


    @POST("Report/Mobile_GetApprovedLeavesForManager")
    suspend fun postApproveLeaveData(@Body approve_leaves_request: Approve_LeavesRequest) :Response<Approve_LeavesResponse>



    @POST("HR/Mobile_GetApprovedLeavesByHR")
    suspend fun postApproveHRLeaveData(@Body approve_leaves_request: Approve_LeavesRequest) :Response<Approve_LeavesResponse>



    @POST("HR/Mobile_GetApprovedPermissions_ByHR")
    suspend fun postApprovedHRPermissionData(@Body approve_leaves_request: ApprovePermissionRequest) :Response<ApprovePermissionResponse>


    @POST("WorkFlow/Mobile_ApproveLeaveRequest")
    suspend fun postApproveData(@Body approve_leaves_request: Approved_ByManagerRequest) :Response<Approved_leaveResponse>


    @POST("HR/Mobile_UpdateLeavesRequest_ByHR")
    suspend fun postHRApproveData(@Body approve_leaves_request: Approve_HRLeavesRequest) :Response<onClickApproveResponse>


    @POST("HR/Mobile_RejectPermissionRequest_ByHR")
    suspend fun postRejectHRLeaveData(@Body reject_leaves_request: RejectLeaveByManagerRequest) :Response<RejectLeaveByManagerResponse>

    @POST("WorkFlow/Mobile_RejectByManagerPermissionRequest")
    suspend fun postRejectLeaveData(@Body reject_leaves_request: RejectLeaveByManagerRequest) :Response<RejectLeaveByManagerResponse>

    @POST("WorkFlow/Mobile_ApprovePermissionRequest")
    suspend fun postApprovePermissionData(@Body approve_permission_request: onClickApproveRequest) :Response<onClickApproveResponse>


    @POST("HR/Mobile_UpdatePermission_ByHR")
    suspend fun postHRApprovePermissionData(@Body approve_permission_request: onClickApproveRequest) :Response<onClickApproveResponse>



    @POST("EmpTransactions/Mobile_GetPendingPermissionsForManager")
    suspend fun postPendingPermissionData(@Body pending_permission_request: PendingPermissonRequest) :Response<PendingPermissionResponse>


    @POST("HR/Mobile_GetEmployeePermission_ByHR")
    suspend fun postPendingHRPermissionData(@Body pending_permission_request: PendingPermissonRequest) :Response<PendingHRPermissionResponse>



    @POST("Mobile/Mobile_GetApprovedPermissionsForManager")
    suspend fun postApprovePermissionData(@Body pending_permission_request: ApprovePermissionRequest) :Response<ApprovePermissionResponse>
    @POST("HR/Mobile_UpdateManualEntry_ByHR")
    suspend fun postapproveManualEntryHRData(@Body approve_manual_entry_request: onClickApproveManualEntryRequest) :Response<onClickApproveManualEntryResponse>


    @POST("WorkFlow/Mobile_ApproveManualEntryRequest")
    suspend fun postapproveManualEntryData(@Body approve_manual_entry_request: onClickApproveManualEntryRequest) :Response<onClickApproveManualEntryResponse>


    @POST("HR/Mobile_RejectManualEntryRequest_ByHR")
    suspend fun postrejecteManualEntryHRData(@Body reject_manual_entry_request: onClickRejectManualEntryRequest) :Response<onClickApproveManualEntryResponse>


    @POST("WorkFlow/Mobile_RejectByManagerManualEntryRequest")
    suspend fun postrejecteManualEntryData(@Body reject_manual_entry_request: onClickRejectManualEntryRequest) :Response<onClickApproveManualEntryResponse>


    @POST("Employee/Mobile_GetPendingManualEntryForManager")
    suspend fun postPendingManualEntryData(@Body pending_manual_entry_request: PendingManualEntryRequest) :Response<PendingManualEntryResponse>
    @POST("HR/Mobile_GetEmployeeManualEntryRequest_ByHR")
    suspend fun postPendingHRManualEntryData(@Body pending_manual_entry_request: PendingManualEntryRequest) :Response<PendingHRManualEntryResponse>



    @POST("HR/Mobile_GetApprovedManualEntryForHR")
    suspend fun postApproveManualEntryHRData(@Body approve_manual_entry_request: ApproveManualEntryRequest) :Response<ApproveManualEntryResponse>



    @POST("Mobile/Mobile_GetApprovedManualEntryForManager")
    suspend fun postApproveManualEntryData(@Body approve_manual_entry_request: ApproveManualEntryRequest) :Response<ApproveManualEntryResponse>

    @POST("Employee/Mobile_GetEmployee_Calendar")
    suspend fun postCalendarData(@Body calendarRequest: CalendarRequest):Response<CalendarResponse>


    @POST("Report/Mobile_GetDashBoardAll")
    suspend fun postDashboardData(@Body dashboardRequest: DashboardRequest):Response<DashboardResponse>



    @POST("Employee/Mobile_GetEmployeeAttendanceSummary")
    suspend fun postSummaryData(@Body summaryRequest: SummaryRequest):Response<SummaryResponse>

    @POST("Employee/Mobile_Get_Emp_Violations_Mobile_currentDay")
    suspend fun postCurrentDelayData(@Body currenDelayRequest: CurrentDelayRequest):Response<CurrentDelayResponse>




  //  @POST("Mobile/Mobile_SaveManualEntry")
  @POST("Mobile/Mobile_SaveTWJTransactions")

  suspend fun postSaveEntryTransactionData(@Body saveManualEntryRequest: SaveManualEntryRequest):
            Response<SaveManualEntryResponse>


    @POST("Employee/Mobile_Annoucement")
    suspend fun postAdminAnnouncementsData(@Body adminAnnouncementsRequest: AdminAnnouncementsRequest):Response<AdminAnnouncementsResponse>




    @POST("Mobile/Mobile_TWJEmp_AllowedGPSCoordinates")
    suspend fun postLocationData(@Body locationRequest: CommonRequest):Response<LocationResponse>


    @GET("EmpTransactions/Mobile_GetLastTransaction")
    suspend fun postTransactionData(
        @Query("FK_EmployeeId") FK_EmployeeId: Int?,
        @Query("Lang") Lang: String?)
    :Response<TransactionResponse>

    @GET("Security/CheckEmailValidity")
    suspend fun postEmailValidityData(
        @Query("Email") email: String?)
            :Response<EmailValidityResponse>

    @GET("EmpTransactions/Mobile_GetLastTransaction_Temp")
    suspend fun postTransactionTempData(
        @Query("FK_EmployeeId") FK_EmployeeId: Int?,
        @Query("Lang") Lang: String?):Response<TransactionResponse>






  @POST("Employee/Mobile_EmployeeFilter")
  suspend fun postAdmin(@Body adminRequest: AdminRequest)
          :Response<AdminResponse>



    @GET("Employee/Mobile_GetEmployeeDetails")
    suspend fun postEmployeeData(
        @Query("FK_EmployeeId") FK_EmployeeId: Int?,
        @Query("Lang") Lang: String?)

    :Response<EmployeeDetailsResponse>



    @GET("Employee/Mobile_GetEmployeeDetails_withregisteredDeviceID")
    suspend fun postEmployeeDataRegisterId(
        @Query("FK_EmployeeId") FK_EmployeeId: Int?,
        @Query("Lang") Lang: String?,
        @Query("registeredDeviceID") registeredDeviceID: String?)

            :Response<EmployeeDetailsResponse>


    @GET("Common/Mobile_GetReasons")
    suspend fun postReasonsData():Response<ReasonsResponse>


    @POST("Report/Mobile_GetSelfServiceRequestList")
    suspend fun postRequestTypeData(@Body request: CommonRequest):Response<RequestTypeResponse>

    @POST("WorkFlow/Mobile_ManualEntryRequest")
    suspend fun postManulaEntryData(@Body request: ManualEntryRequest):Response<ManualEntryResponse>



    @GET("Employee/Mobile_GetPermissionTypesByEmployee")
    suspend fun postRequestData(
        @Query("EmployeeId") employeeId: String?,
        @Query("Lang") lang: String?,
        @Query("Type") type:String
    ):Response<RequestResponse>

    @GET("Common/Mobile_GetTawajudNotificationsTypes")
    suspend fun postNotificationData(
        @Query("Lang") Lang: Int?
    )
    :Response<NoficationTypesResponse>


    @GET("Employee/Mobile_GetPermissionTypesbyAdmin")
    suspend fun postAdminPermissionRequestData(
        @Query("EmployeeId") employeeId: String?,
        @Query("FK_CompanyId") comp_id: Int?,
        @Query("FK_EntityId") entity_id: Int?,
        @Query("Lang") lang: String?,
        @Query("Type") type:String
    ):Response<AdminPermissionRequestResponse>



    @POST("WorkFlow/Mobile_SaveLeaveRequest")
    suspend fun postSaveLeaveRequestData(@Body request: RequestSaveLeaveRequest):Response<RequestSaveLeaveResponse>

    @POST("WorkFlow/Mobile_PermissionRequest")
    suspend fun postSavePermissionRequestData(@Body request: RequestSavePermissionRequest):Response<RequestSavePermissionResponse>




    /* @Headers(
         "Content-Type: application/json;charset=utf-8",
     )*/



    /*@GET("Employee/Mobile_GetCompanyInfo_EmployeeID")
    suspend fun getContactCompanyData(@Body contactRequest: ContactRequest)
    :Response<ContactResponse>*/


    @GET("Employee/Mobile_GetCompanyInfo_EmployeeID?")
    suspend fun getContactCompanyData(
        @Query("EmployeeId") employeeId: String?,
        @Query("Lang") lang: String?
    )
            :Response<ContactResponse>



    @GET("Employee/Mobile_GetCompanyInfo_EmployeeID?")
    suspend fun getComplaintsCompanyData(
        @Query("EmployeeId") employeeId: String?,
        @Query("Lang") lang: String?
    )
            :Response<ComplaintsResponse>



    @POST("Report/Mobile_GeneratePdfReport")
    suspend fun postPdfGenerateRequestData(@Body request: PdfReportRequest):Response<PdfReportResponse>



    @GET("Employee/Mobile_OrgLevelFilter")
    suspend fun postMobileOrgLevelFilterData(
        @Query("FK_CompanyId") FK_EmployeeId: Int?,
        @Query("FilterType") Lang: Int?)
            :Response<AdminFilterResponse>



    @POST("WorkFlow/Mobile_MultiplePermissions")
    suspend fun postSaveAdminPermissionRequestData(@Body request: RequestSavePermissionRequest):Response<RequestSavePermissionResponse>

}