package com.sait.tawajudpremiumplusnewfeatured.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by cmehmood on 8/10/2015.
 */
public class EnumUtils {

    public static long AUTO_OUT_DURATION = 60 * 1000;//1 minute

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    public enum AppDomain {
        LIVE, QA
    }

    public enum SeasonType {
        AUTUMN(0), SUMMER(1), WINTER(2), SPRING(3), Other(4);

        private int numVal;

        SeasonType(int numVal) {
            this.numVal = numVal;
        }

        public static String getLabel(int position) {
            switch (position) {
                case 0:
                    return "Autumn";
                case 1:
                    return "Summer";
                case 2:
                    return "Winter";
                case 3:
                    return "Spring";
                case 4:
                    return "Other";
                default:
                    return "Other";
            }
        }

        public static SeasonType getSeason(int type) {

            if (type == 0) {
                return AUTUMN;
            } else if (type == 1) {
                return SUMMER;
            } else if (type == 2) {
                return WINTER;
            } else if (type == 3) {
                return SPRING;
            } else if (type == 4) {
                return Other;
            }
            return Other;
        }

        public static int getSeasonIndex(String strTomatch) {

            if (strTomatch.equals("Autumn")) {
                return 0;
            } else if (strTomatch.equals("Summer")) {
                return 1;
            } else if (strTomatch.equals("Winter")) {
                return 2;
            } else if (strTomatch.equals("Spring")) {
                return 3;
            } else if (strTomatch.equals("Other")) {
                return 4;
            }

            return 4;
        }

        public int getNumVal() {
            return numVal;
        }
    }

    public enum ServiceName {
        ExternalService,
        AuthenticateUser,
        Login,
        Message,
        Mobile_GetReasons,
        SaveManualEntry,
        WorkLocations,
        WorkLocation,
        EmployeeDetails,
        GetReasons,
        GetLastTransiction,
        GetAnnouncement,
        SelfService,
        GetEmployeeCurrentInfo,
        GetEmployeePermissionForManager,
        Get_RandomQuestions,
        Get_RandomQuestions_Timer,
        Save_FeedBack_Answer,
        Mobile_GetApprovedPermissionsForManager,
        Mobile_GetApprovedLeavesForManager,
        Mobile_GetPendingLeavesForManager,
        Mobile_GetPendingPermissionsForManager,
        Mobile_GetPendingManualEntryForManager,
        Mobile_GetApprovedManualEntryForManager,
        ApproveByManagerPermissionRequest,
        ApproveByManagerLeaveRequest,
        Mobile_ApproveManualEntryRequest,
        Mobile_RejectByManagerManualEntryRequest,
        Mobile_ApproveLeaveRequest,
        Mobile_ApprovePermissionRequest,
        RejectByManagerPermissionRequest,
        EmployeeViolations,
        DashBoardAttendance,
        GetEmployeeManagerLists,
        GetPermissionTypesByEmployee,
        GetEmployeeDetailsUnderManager,
        GetSelfServiceReportLists,
        GeneratePdfReport,
        PermissionRequest,
        SaveLeaveRequest,
        GetWebServiceLink,
        /*callGetAllowedByEmployeeID*/
        ResetDevice,
        Settings,
        Mobile_ManualEntryRequest,
        Mobile_AllowedGPSCoordinatesID,
        Mobile_AllowedGPSCoordinates_ByEmployeeId,
        Mobile_GetLastTransaction_Temp,
        Mobile_GetHeartBeat_Timer,
        Mobile_SaveHeartBeat,
        Mobile_GetEmployeeAttendanceSummary,
        Mobile_GetEmployee_Calendar,
        Mobile_GetCompanyInfo_EmployeeID,
        Mobile_ChangePassword;

        public static String getServicePath(ServiceName serviceName) {
            switch (serviceName) {
                case AuthenticateUser:
                    return "User/Register";
                case Mobile_GetCompanyInfo_EmployeeID:
                    return "Mobile_GetCompanyInfo_EmployeeID";
                case Mobile_GetLastTransaction_Temp:
                    return "Mobile_GetLastTransaction_Temp";
                case Mobile_ApproveManualEntryRequest:
                    return "Mobile_ApproveManualEntryRequest";
                case Mobile_RejectByManagerManualEntryRequest:
                    return "Mobile_RejectByManagerManualEntryRequest";
                case Settings:
                    return "Settings/GetStatesByCountry_New_Id";
                case Mobile_ManualEntryRequest:
                    return "Mobile_ManualEntryRequest";
                case Login:
                    return "Mobile_UserLogin";
                case Get_RandomQuestions:
                    return "Mobile_FeedBack_Get_RandomQuestions";
                case Get_RandomQuestions_Timer:
                    return "Mobile_FeedBack_Get_RandomQuestions_Timer";
                case Save_FeedBack_Answer:
                    return "Mobile_Save_FeedBack_UserAnswers";
                case Message:
                    return "Mobile_SendMessage";
                case WorkLocations:
                    return "Mobile_GetWorkLocations";
                case WorkLocation:
                    return "Mobile_GetUserLocation";
                case Mobile_GetApprovedPermissionsForManager:
                    return "Mobile_GetApprovedPermissionsForManager";
                case Mobile_GetApprovedLeavesForManager:
                    return "Mobile_GetApprovedLeavesForManager";
                case Mobile_GetPendingLeavesForManager:
                    return "Mobile_GetPendingLeavesForManager";
                case Mobile_GetPendingManualEntryForManager:
                    return "Mobile_GetPendingManualEntryForManager";
                case Mobile_GetApprovedManualEntryForManager:
                    return "Mobile_GetApprovedManualEntryForManager";
                case Mobile_GetPendingPermissionsForManager:
                    return "Mobile_GetPendingPermissionsForManager";
                case Mobile_GetReasons:
                    return "Mobile_GetReasons";
                case SaveManualEntry:
                    return "Mobile_SaveManualEntry";
                case EmployeeDetails:
                    return "Mobile_GetEmployeeDetails";
                case GetAnnouncement:
                    return "Mobile_GetAnnouncement";
             /*   case SelfService:
                    return "Mobile_GetSelfServiceReportLists";*/
                case EmployeeViolations:
                    return "Mobile_GetEmployeeViolations";
                case GetEmployeeCurrentInfo:
                    return "Mobile_GetEmployeeCurrentInfo";
                case GetReasons:
                    return "Mobile_GetReasons";
                case GetLastTransiction:
                    return "Mobile_GetLastTransaction";
                case GetEmployeePermissionForManager:
                    return "Mobile_GetEmployeePermissionForManager";
                case ApproveByManagerLeaveRequest:
                    return "ApproveByManagerLeaveRequest";
                case Mobile_ApproveLeaveRequest:
                    return "Mobile_ApproveLeaveRequest";
                case Mobile_ApprovePermissionRequest:
                    return "Mobile_ApprovePermissionRequest";
                case ApproveByManagerPermissionRequest:
                    return "Mobile_ApprovePermissionRequest";
                case RejectByManagerPermissionRequest:
                    return "Mobile_RejectByManagerPermissionRequest";
                case GetEmployeeManagerLists:
                    return "Mobile_GetEmployeeManagerLists";
                case GetEmployeeDetailsUnderManager:
                    return "Mobile_GetEmployeeDetailsUnderManager";
                case GetSelfServiceReportLists:
                    return "Mobile_GetSelfServiceReportLists";
                case GeneratePdfReport:
                    return "Mobile_GeneratePdfReport";
                case PermissionRequest:
                    return "Mobile_PermissionRequest";
                case GetPermissionTypesByEmployee:
                    return "Mobile_GetPermissionTypesByEmployee";
                case SaveLeaveRequest:
                    return "Mobile_SaveLeaveRequest";
                case GetWebServiceLink:
                    return "GetWebServiceLink";
                case ResetDevice:
                    return "Mobile_ResetDevice";
                case DashBoardAttendance:
                    return "Mobile_GetDashBoardAttendance";
                case Mobile_GetEmployeeAttendanceSummary:
                    return "Mobile_GetEmployeeAttendanceSummary";
                case Mobile_GetEmployee_Calendar:
                    return "Mobile_GetEmployee_Calendar";
                case Mobile_ChangePassword:
                    return "Mobile_ChangePassword";
                case Mobile_AllowedGPSCoordinatesID:
                    return "Mobile_AllowedGPSCoordinatesID";
                case Mobile_AllowedGPSCoordinates_ByEmployeeId:
                    return "Mobile_AllowedGPSCoordinates_ByEmployeeId";
                case Mobile_GetHeartBeat_Timer:
                    return "Mobile_GetHeartBeat_Timer";
                case Mobile_SaveHeartBeat:
                    return "Mobile_SaveHeartBeat";


                default:
                    return "Other";
            }
        }
    }

    public enum RequestMethod {
        GET, POST
    }

    public enum ServiceResponseMessage {
        InvalidResponse, NetworkError, ServerNotReachable, ConnectionTimeOut
    }

    public enum ReasonType {
        IN(0), Out(1), OfficialOut(2), PersonalOut(3), SickOut(5), BreakOut(6);

        private int numVal;

        ReasonType(int numVal) {
            this.numVal = numVal;
        }

        public static int getReasonIndex(ReasonType strTomatch) {

            if (strTomatch == IN) {
                return 0;
            } else if (strTomatch == Out) {
                return 1;
            } else if (strTomatch == OfficialOut) {
                return 2;
            } else if (strTomatch == PersonalOut) {
                return 3;
            } else if (strTomatch == SickOut) {
                return 5;
            } else if (strTomatch == BreakOut) {
                return 6;
            }

            return 0;
        }

        public static String getLabel(int position) {
            switch (position) {
                case 0:
                    return "IN";
                case 1:
                    return "Out";
                case 2:
                    return "Official Out";
                case 3:
                    return "Personal Out";
                case 5:
                    return "Sick Out";
                case 6:
                    return "Break Out";
                default:
                    return "IN";
            }
        }

        public int getNumVal() {
            return numVal;
        }
    }

}// main
