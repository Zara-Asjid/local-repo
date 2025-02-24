package com.sait.tawajudpremiumplusnewfeatured.ui.home.models


import com.sait.tawajudpremiumplusnewfeatured.data.model.BaseResponse

data class EmployeeDetailsResponse(
    val `data`: EmployeeData
):BaseResponse()




/*@Parcelize
data class EmployeeData(
    val employeeId: Int,
    val employeeNo: String,
    val employeeName: String,
    val employeeArabicName: String,
    val empImageURL: String,
    val empFilename: String,
    val employeeCardNo: String,
    val payRollNumber: String,
    val companyName: String,
    val companyArabicName: String,
    val entityName: String,
    val entityArabicName: String,
    val gradeName: String,
    val gradeArabicName: String,
    val workLocationName: String,
    val workLocationArabicName: String,
    val designationName: String,
    val designationArabicName: String,
    val gender: String,
    val dob: String,
    val email: String,
    val mobileNo: String,
    val nationalityName: String,
    val nationalityArabicName: String,
    val religionName: String,
    val religionArabicName: String,
    val matitalStatusName: String,
    val maritalStatusArabicName: String,
    val joinDate: String,
    val managerID: Long,
    val managerName: String ,
    val managerNo: Int,
    val expectOutValue: String ,
    val empWorkSchedulelist: String,
    val scheduleName: String,
    val role: String
)*/



data class EmployeeData(
    val error:String,
    val companyArabicName: String,
    val companyName: String,
    val designationArabicName: String,
    val designationName: String,
    val dob: String,
    val email: String,
    val empFilename: String,
    val empImageURL: String,
    val emp_WorkSchedulelist: Any,
    val employeeArabicName: String,
    val employeeCardNo: String,
    val employeeId: Int,
    val employeeName: String,
    val employeeNo: String,
    val entityArabicName: String,
    val entityName: String,
    val expectOutValue: Any,
    val gender: String,
    val gradeArabicName: String,
    val gradeName: String,
    val joinDate: String,
    val managerArabicName: String,
    val managerID: Int,
    val managerName: String,
    val managerNo: Any,
    val maritalStatusArabicName: String,
    val matitalStatusName: String,
    val mobileNo: String,
    val nationalityArabicName: String,
    val nationalityName: String,
    val payRollNumber: String,
    val registeredDeviceID: Any,
    val religionArabicName: String,
    val religionName: String,
    val role: String,
    val scheduleName: String,
    val userID: String,
    val workLocationArabicName: String,
    val workLocationName: String
)

