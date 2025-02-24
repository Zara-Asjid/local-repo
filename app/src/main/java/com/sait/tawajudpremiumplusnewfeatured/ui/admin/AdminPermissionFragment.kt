package com.sait.tawajudpremiumplusnewfeatured.ui.admin


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import com.google.common.io.Files.getFileExtension
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.adapters.Spinner_Adapter
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentAdminPermissionBinding
import com.sait.tawajudpremiumplusnewfeatured.items.SpinnerItem
import com.sait.tawajudpremiumplusnewfeatured.items.SpinnerItem_BO
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.LeaveTypes1
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.PermissionsTypes1
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.AdminViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.Admin_permission.AdminPermissionSaveViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.Admin_permissiontypes.AdminPermissionRequestViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.filter.AdminFilterViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.util.EnumUtils
import com.sait.tawajudpremiumplusnewfeatured.util.FileUtils
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AdminPermissionFragment : BaseFragment(), View.OnClickListener, AdapterView.OnItemSelectedListener,
    CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {
    private lateinit var binding: FragmentAdminPermissionBinding
    private var mContext: Context? = null
    private lateinit var viewModel: AdminPermissionRequestViewModel
    private lateinit var viewModel_Permissions: AdminPermissionSaveViewModel
    var permissionSaveRequest: RequestSavePermissionRequest? = null

    private lateinit var viewModel_adminFilter: AdminFilterViewModel
    private lateinit var viewModel_admin: AdminViewModel

    val arrayList_type_leave_permission = ArrayList<SpinnerItem_BO>()
    private val FILE_PICK_REQUEST_CODE = 1
    var permission_all_id: Int? = 0
    var base64String: String? = ""
    var fileTypeStr: String? = ""
    var uploadPermissionStr: String? = ""
    var attachedFileStr: String? = ""

    val arrayList_Company = ArrayList<SpinnerItem>()
    val arrayList_All = ArrayList<SpinnerItem>()
    val arrayList_Employees = ArrayList<SpinnerItem>()
    var handler: Handler = Handler()

    var type_id: Int? = 0
    var duration: Int = 0
    var isFlexible: Boolean? = null
    var isFileMandatory :Boolean =false

    var isFullDay: Boolean? = false
      var parts: List<String>?=null

    /*    val arrayList_Entity = ArrayList<SpinnerItem>()
        val arrayList_WorkLocations = ArrayList<SpinnerItem>()
        val arrayList_WorkGroup = ArrayList<SpinnerItem>()*/


    var str_company_id: String? = "0"
    var str_entity_id: String? = "0"
    var str_workLocation_id: String? = "0"
    var str_workGroup_id: String? = "0"
    var filter_type:Int=0
    val employeeIdsString = StringBuilder()
    var str_select_entity_name :String?=" "
    var str_select_workLoc_name :String?=" "
    var str_select_workGroup_name :String?=" "
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminPermissionBinding.inflate(inflater, container, false)
        mContext = inflater.context
        viewModel = ViewModelProvider(this)[AdminPermissionRequestViewModel::class.java]

        viewModel_adminFilter = ViewModelProvider(this)[AdminFilterViewModel::class.java]
        viewModel_Permissions = ViewModelProvider(this)[AdminPermissionSaveViewModel::class.java]

        viewModel_admin = ViewModelProvider(this)[AdminViewModel::class.java]

        val activity = this.activity as MainActivity?
        setPermissionRequestLayoutsVisible()
        setClickListeners(activity)

        callCompanyAPI(0,0)
        /*  callEntityAPI(0,1)
          callWorkLocationsAPI(0,2)

          callWorkGroupAPI(0,3)*/

        return binding.root
    }

    private fun callCompanyAPI(company_id: Int, filter_type: Int) {
        addObserver_Company()
        getCompanyDetails(company_id,filter_type)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getCompanyDetails(company_id: Int, filter_type: Int) {

        val adminFilterRequest = AdminFilterRequest(company_id,filter_type)

        viewModel_adminFilter.getAdminFilterData(
            mContext!!,
            adminFilterRequest


        )


    }


    private fun addObserver_Company() {
        viewModel_adminFilter.adminFilterResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse
                            // val arrayList = ArrayList<SpinnerItem_BO>()

                            if (requestResponse.data != null) {

                                if (requestResponse.data.isNotEmpty()) {
                                   // arrayList_Company.clear()
                                    /*  arrayList_Entity.clear()
                                      arrayList_WorkLocations.clear()
                                      arrayList_WorkGroup.clear()*/
                                    arrayList_All.clear()
                                    val company_adapter = binding.spCompany.adapter
                                    val emp_adapter =binding.spEmployees.adapter
                                    if ( company_adapter!= null && company_adapter is ArrayAdapter<*>) {
                                        if (company_adapter.count == 1){
                                            arrayList_Company.clear()
                                        }

                                    }
                                        if ( emp_adapter!= null ) {
                                            if (emp_adapter.count > 1){
                                                binding.spEmployees.adapter = null
                                            }

                                        }
                                    /*    if (binding.rbEntity.isChecked&&arrayList_Company.isEmpty()){

                                         }*/
                                    for (i in 0 until requestResponse.data.size) {

                                        if(requestResponse.data[i].companyName!=null && requestResponse.data[i].companyName.isNotEmpty()){
                                            filter_type = 0
                                            if(UserShardPrefrences.getLanguage(mContext).equals("0")){
                                                arrayList_Company.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].companyName,
                                                        requestResponse.data[i].companyId,
                                                        ""
                                                    )
                                                )
                                            }
                                            else{
                                                arrayList_Company.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].companyArabicName,
                                                        requestResponse.data[i].companyId,
                                                        ""


                                                    )
                                                )
                                            }
                                        }
                                        else  if(requestResponse.data[i].entityName!=null && requestResponse.data[i].entityName.isNotEmpty()){
                                            filter_type = 1
                                            if(UserShardPrefrences.getLanguage(mContext).equals("0")){
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].entityName,
                                                        requestResponse.data[i].entityId,
                                                        ""
                                                    )
                                                )
                                            }
                                            else{
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].entityName,
                                                        requestResponse.data[i].entityId,
                                                        ""


                                                    )
                                                )
                                            }
                                        }
                                        else  if(requestResponse.data[i].workLocationName!=null && requestResponse.data[i].workLocationName.isNotEmpty()){
                                            filter_type = 2
                                            if(UserShardPrefrences.getLanguage(mContext).equals("0")){
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].workLocationName,
                                                        requestResponse.data[i].workLocationId,
                                                        ""
                                                    )
                                                )
                                            }
                                            else{
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].workLocationName,
                                                        requestResponse.data[i].workLocationId,
                                                        ""


                                                    )
                                                )
                                            }
                                        }
                                        else  if(requestResponse.data[i].groupName!=null && requestResponse.data[i].groupName.isNotEmpty()){
                                            filter_type = 3
                                            if(UserShardPrefrences.getLanguage(mContext).equals("0")){
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].groupName,
                                                        requestResponse.data[i].groupId,
                                                        ""
                                                    )
                                                )
                                            }
                                            else{
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].groupArabicName,
                                                        requestResponse.data[i].groupId,
                                                        ""


                                                    )
                                                )
                                            }
                                        }

                                    }
                                }



                                if(filter_type==0){
                                    setUpDropDown_Comapny(arrayList_Company)
                                }
                                else if(filter_type==1){
                                    setUpDropDown(arrayList_All,binding.spAll)
                                }

                                else if(filter_type==2){
                                    setUpDropDown(arrayList_All,binding.spAll)
                                }
                                else if(filter_type==3){
                                    setUpDropDown(arrayList_All,binding.spAll)
                                }






                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.caution,resources.getColor(R.color.red))

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun callPermissionRequestsAPI(id: Int) {
// Need to call Save Permission API
        addObserverPermissionRequest()
        getPermissionRequestsDetails(id)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPermissionRequestsDetails(typeId: Int) {

        if (binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
            /*  from date ,to date,remark*/
            permissionSaveRequest = RequestSavePermissionRequest(
                permission_all_id!!,
                /* UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId*/
                employeeIdsString.toString(),
                typeId,
                binding.txtFromDate.text.toString(),
                convertToDateTime(binding.txtFromDate.text.toString(), "00:00"),
                convertToDateTime(binding.txtToDate.text.toString(), "00:00"),
                binding.isForFullDayCheckBox.isChecked,
                binding.edtRemarks.text.toString(),
                binding.isForPeriodSwitch.isChecked,
                convertToDateTime(binding.txtToDate.text.toString(), "00:00"),
                binding.isForFullDayCheckBox.isChecked!!,
                if(binding.isFlexibleSwitch.isChecked){
                    convertToMinutes( binding.txtDuration.text.toString())
                }
                else{
                    0
                },

                removeSpaces(base64String.toString()),
                fileTypeStr.toString(),
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                0,true,false,false,0,
                convertToDateTime(binding.txtToDate.text.toString(), "00:00"),0,0,0,0,0,0,0,0,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId
            )


        } else
            if (!binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
// from date,to date,to time,from time

            permissionSaveRequest = RequestSavePermissionRequest(
                permission_all_id!!,
                employeeIdsString.toString(),
                typeId,
                binding.txtFromDate.text.toString(),
                if(binding.isFlexibleSwitch.isChecked){
                    convertToDateTime(
                        binding.txtFromDate.text.toString(),
                        "00:00"
                    )
                }
                        else{
                    convertToDateTime(
                        binding.txtFromDate.text.toString(),
                        binding.txtFromTime.text.toString()
                    )
                }
                ,

                if(binding.isFlexibleSwitch.isChecked){
                    convertToDateTime(
                        binding.txtToDate.text.toString(),
                        "00:00"
                    )
                }
                else{
                    convertToDateTime(
                        binding.txtToDate.text.toString(),
                        binding.txtToTime.text.toString()
                    )                }

                       ,
                binding.isForFullDayCheckBox.isChecked,
                binding.edtRemarks.text.toString(),
                binding.isForPeriodSwitch.isChecked,
               /* convertToDateTime(
                    binding.txtToDate.text.toString(),
                    binding.txtToTime.text.toString()
                ),*/

                if(binding.isFlexibleSwitch.isChecked){
                    convertToDateTime(
                        binding.txtToDate.text.toString(),
                        "00:00"
                    )
                }
                else{
                    convertToDateTime(
                        binding.txtToDate.text.toString(),
                        binding.txtToTime.text.toString()
                    )                },

                binding.isFlexibleSwitch.isChecked!!,
                if(binding.isFlexibleSwitch.isChecked){
                    convertToMinutes( binding.txtDuration.text.toString())
                }
                else{
                    0
                },
                removeSpaces(base64String.toString()),
                fileTypeStr.toString(),
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                0,true,false,false,0,
                convertToDateTime(binding.txtToDate.text.toString(), "00:00"),0,0,0,0,0,0,0,0,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId
            )


        } else
            if (binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
            /*
            * Date , Remarks
            * */
            permissionSaveRequest = RequestSavePermissionRequest(
                permission_all_id!!,
                employeeIdsString.toString(),
                typeId,
                binding.txtDate.text.toString(),
                convertToDateTime(binding.txtDate.text.toString(), "00:00"),
                convertToDateTime(binding.txtDate.text.toString(), "00:00"),
                binding.isForFullDayCheckBox.isChecked,
                binding.edtRemarks.text.toString(),
                binding.isForPeriodSwitch.isChecked,
                convertToDateTime(binding.txtDate.text.toString(), "00:00"),
                binding.isForFullDayCheckBox.isChecked,

                //  binding.isFlexibleSwitch.isChecked!!,
                if(binding.isFlexibleSwitch.isChecked){
                    convertToMinutes( binding.txtDuration.text.toString())
                }
                else{
                    0
                },
                removeSpaces(base64String.toString()),
                fileTypeStr.toString(),
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                0,true,false,false,0,
                convertToDateTime(binding.txtDate.text.toString(), "00:00"),0,0,0,0,0,0,0,0,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId
            )

        } else
            if (!binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
            /* Date,   From Time, To Time , Remarks */
            permissionSaveRequest = RequestSavePermissionRequest(
                permission_all_id!!,
                employeeIdsString.toString(),
                typeId,
                binding.txtDate.text.toString(),

                if(binding.isFlexibleSwitch.isChecked){
                    convertToDateTime(
                        binding.txtDate.text.toString(),
                        "00:00"
                    )
                }
                else{


                    convertToDateTime(
                        binding.txtDate.text.toString(),
                        binding.txtFromTime.text.toString()
                    )


                },

                if(binding.isFlexibleSwitch.isChecked){

                    convertToDateTime(
                        binding.txtDate.text.toString(),
                        "00:00"
                    )


                }
                else{

                    convertToDateTime(
                        binding.txtDate.text.toString(),
                        binding.txtToTime.text.toString()
                    )

                },

                /* convertToDateTime(
                      binding.txtDate.text.toString(),
                      binding.txtFromTime.text.toString()
                  ),
                  convertToDateTime(
                      binding.txtDate.text.toString(),
                      binding.txtToTime.text.toString()
                  ),*/
                binding.isForFullDayCheckBox.isChecked,
                binding.edtRemarks.text.toString(),
                binding.isForPeriodSwitch.isChecked,
                convertToDateTime(
                    binding.txtDate.text.toString(),
                    "00:00"
                ),
                binding.isFlexibleSwitch.isChecked!!,
                if(binding.isFlexibleSwitch.isChecked){
                    convertToMinutes(binding.txtDuration.text.toString())
                }
                else{
                    0
                },
                removeSpaces(base64String.toString()),
                fileTypeStr.toString(),
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                0,true,false,false,0,
                convertToDateTime(binding.txtDate.text.toString(), "00:00"),0,0,0,0,0,0,0,0,
                UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId
            )


        }





        viewModel_Permissions.getRequestSavePermissionData(
            mContext!!,
            permissionSaveRequest!!
        )
    }
    fun convertToMinutes(timeStr: String): Int {
        // Split the time string into hours and minutes
        val (hours, minutes) = timeStr.split(":").map { it.toInt() }

        // Calculate total minutes
        val totalMinutes = hours * 60 + minutes

        return totalMinutes
    }

    private fun removeSpaces(input: String): String {
        return input.replace("\\s".toRegex(), "")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDateTime(date: String, time: String): String {

        // Combine the date and time to create a LocalDateTime object
        try {
            val localDateTime =
                LocalDateTime.parse(
                    "$date $time".trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                )

            // Format the LocalDateTime object in the desired format
            val formattedDateTime =
                localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

            return formattedDateTime
        } catch (e:Exception){
            return getString(R.string.invalid_datetime_text)
        }
    }


    private fun addObserverPermissionRequest() {
        viewModel_Permissions.requestSavePermissionResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse
                            // val arrayList = ArrayList<SpinnerItem_BO>()

                            if (requestResponse.data != null) {
                                if (requestResponse.message.equals(mContext!!.resources.getString(R.string.success_new_txt))) {
                                 //   SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,requestResponse.data.msg,R.drawable.caution,resources.getColor(R.color.red))


                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,
                                        requestResponse.data.msg,R.drawable.app_icon,colorPrimary,
                                        SnackBarUtil.OnClickListenerNew {

                                            (activity as MainActivity).onBackPressed()

                                        })




                                } else {
                                    if (requestResponse.message.equals(mContext!!.resources.getString(R.string.response_error_txt))) {
                                        // SnackBarUtil.showSnackbar(mContext,requestResponse.data.msg,false)

                                        if(requestResponse.data.msg.contains("$$$")){


                                             parts = requestResponse.data.msg.split("$$$")


                                            showSnackbar_Drawable_backroundcolor_with_more(mContext!!,
                                                parts!![0],R.drawable.caution,colorPrimary,
                                                parts!![1])
                                        }
                                        else{
/*
                                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                mContext!!,
                                                requestResponse.data.msg,
                                                R.drawable.caution,
                                                resources.getColor(R.color.red)
                                            )
*/

                                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context, requestResponse.data.msg,R.drawable.caution,resources.getColor(R.color.red),
                                                SnackBarUtil.OnClickListenerNew {
                                                   // (activity as MainActivity).onBackPressed()
                                                })


                                            /*SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                mContext!!,
                                                mContext!!.resources.getString(R.string.saved_successfully_txt),
                                                R.drawable.app_icon,
                                                colorPrimary
                                            )*/
                                        }



/*
                                        AlertDialogUtils.showAlertDialog(
                                            mContext!!,
                                            requestResponse.data.msg,
                                            object : AlertDialogUtils.OnClickListener {
                                                override fun onPositiveClick() {
                                                    // (activity as MainActivity).onBackPressed()
                                                    //  replaceFragment(SelfServiceFragment(), R.id.flFragment, false)
                                                }
                                            }
                                        )
*/


                                    }


                                }


                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.caution,resources.getColor(R.color.red))

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }
    private fun setUpDropDown(empList: ArrayList<SpinnerItem>, spinner: Spinner) {
        val arrayList = java.util.ArrayList<String>()
        arrayList.add(resources.getString(R.string.select))
        for (i in empList.indices) {
           // arrayList.add(empList[i].emp_id.toString()+"  "+empList[i].title)
            arrayList.add(empList[i].title)
        }

        val customAdapter = Spinner_Adapter(context, arrayList)

        //  val  spinner_Custom_Adapter = Spinner_Custom_Adapter(context, arrayList,this)
        spinner.adapter = customAdapter
        spinner.setSelection(0)

    }


    private fun setUpDropDownEmployees(empList: ArrayList<SpinnerItem>) {
        val arrayList = java.util.ArrayList<String>()
        arrayList.add(resources.getString(R.string.select))
        for (i in empList.indices) {
             arrayList.add(empList[i].emp_id.toString()+"  "+empList[i].title)
            //arrayList.add(empList[i].title)
        }

        val customAdapter = Spinner_Adapter(context, arrayList)

        //  val  spinner_Custom_Adapter = Spinner_Custom_Adapter(context, arrayList,this)
        binding.spEmployees.adapter = customAdapter
        binding.spEmployees.setSelection(0)

    }


    @SuppressLint("SuspiciousIndentation")
    private fun setUpDropDown_Comapny(empList: ArrayList<SpinnerItem>) {
        val arrayList = java.util.ArrayList<String>()
        arrayList.add(resources.getString(R.string.select))
        for (i in empList.indices) {
          //  arrayList.add(empList[i].emp_id.toString()+"  "+empList[i].title)
            arrayList.add(empList[i].title)
        }

        val customAdapter = Spinner_Adapter(context, arrayList)

        //  val  spinner_Custom_Adapter = Spinner_Custom_Adapter(context, arrayList,this)
        binding.spCompany.adapter = customAdapter
        binding.spCompany.setSelection(0)

     //   str_company_id = arrayList_Company[0].emp_id.toString()

    }


    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        activity?.binding!!.layout.imgBack.setOnClickListener(this)

        binding.selectAllSwitch.setOnCheckedChangeListener(this)

        binding.txtUploadfile.setOnClickListener(this)
        binding.txtSave.setOnClickListener(this)
        binding.txtToDate.setOnClickListener(this)
        binding.txtFromDate.setOnClickListener(this)
        binding.txtDate.setOnClickListener(this)
        binding.txtToTime.setOnClickListener(this)
        binding.txtFromTime.setOnClickListener(this)

        binding.txtCancel.setOnClickListener(this)

        binding.txtReasonsTime.setOnClickListener(this)
        binding.selectedFileImageView.setOnClickListener(this)
        binding.selectedFilecancelbtn.setOnClickListener(this)
        binding.txtDuration.setOnClickListener(this)

        binding.isForPeriodSwitch.setOnCheckedChangeListener(this)
        binding.isForFullDayCheckBox.setOnCheckedChangeListener(this)
        binding.isFlexibleSwitch.setOnCheckedChangeListener(this)
        binding.spSelectRequest.onItemSelectedListener = this
        binding.spCompany.onItemSelectedListener = this

        binding.spAll.onItemSelectedListener = this

        binding.spEmployees.onItemSelectedListener = this

        binding.rg.setOnCheckedChangeListener(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun onResume() {
        super.onResume()


        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.fragment_title_emp_perm))

        if (GlobalVariables.from_background) {
            handler.post(checkInternetRunnable)
        }


    }

    private val checkInternetRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun run() {
            val application = requireActivity().application as TawajudApplication

            if (application.
                hasInternetConnection()) {
                onRefresh()
            } else {
                // Repeat the check after 1 second if there is no internet connection
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun onRefresh() {

    }

    private fun callRequestsAPI(strPermissionTypeLetter: String?) {
        addObserver()
        getRequestsDetails(strPermissionTypeLetter)

    }

    private fun addObserver() {
        viewModel.requestResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse
                            // val arrayList = ArrayList<SpinnerItem_BO>()

                            if (requestResponse.data != null) {

                                arrayList_type_leave_permission.clear()
                                if (requestResponse.data.permissionsTypes1.isNotEmpty()) {
                                    val permissionTypes: List<PermissionsTypes1> =
                                        requestResponse.data.permissionsTypes1

                                    for (i in 0 until requestResponse.data.permissionsTypes1.size) {
                                        val permissionType: PermissionsTypes1 = permissionTypes[i]
                                        arrayList_type_leave_permission.add(
                                            SpinnerItem_BO(
                                                permissionType.permId,
                                                permissionType.permId,
                                                permissionType.permName,
                                                permissionType.hasFullDayPermission,
                                                permissionType.hasPermissionForPeriod,
                                                permissionType.hasFlexiblePermission,
                                                permissionType.maxDuration,
                                                permissionType.attachmentIsMandatory,
                                                permissionType.remarksIsMandatory
                                            )
                                        )


                                    }
                                } else {
                                    val leaveTypes: List<LeaveTypes1> =
                                        requestResponse.data.leaveTypes1
                                    for (i in 0 until requestResponse.data.leaveTypes1.size) {
                                        val leaveType: LeaveTypes1 = leaveTypes[i]
                                        arrayList_type_leave_permission.add(
                                            SpinnerItem_BO(
                                                leaveType.permId,
                                                leaveType.permId,
                                                leaveType.permName,
                                                false,
                                                false, false, 0, leaveType.attachmentIsMandatory, leaveType.remarksIsMandatory
                                            )
                                        )


                                    }


                                }
                                setUpDropDown(arrayList_type_leave_permission)


                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.caution,resources.getColor(R.color.red))

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }
    private fun getRequestsDetails(strPermissionTypeLetter: String?) {
        if (strPermissionTypeLetter != null) {
            viewModel.getAdminPermissionRequestData(
                mContext!!,
                UserShardPrefrences.getUserInfo(context).fKEmployeeId,
                str_company_id!!.toInt(),str_entity_id!!.toInt(),
                UserShardPrefrences.getLanguage(mContext)!!,
                strPermissionTypeLetter,
            )
        }

    }

    private fun setUpDropDown(empList: ArrayList<SpinnerItem_BO>) {
        val arrayList = ArrayList<String>()

        val arrayListFulldayPermission = ArrayList<String>()
        val arrayListPeriodPermission = ArrayList<String>()
         arrayList.add(resources.getString(R.string.select))
        for (i in empList.indices) {
            arrayList.add(empList[i].title)

            arrayListFulldayPermission.add(empList[i].hasFullDayPermission.toString())
            arrayListPeriodPermission.add(empList[i].hasPermissionForPeriod.toString())
        }
        val customAdapter = Spinner_Adapter(context, arrayList)
        binding.spSelectRequest.adapter = customAdapter
        binding.spSelectRequest.setSelection(0)
        /*  binding.spSelectRequest.onItemSelectedListener =
              object : AdapterView.OnItemSelectedListener {
                  override fun onItemSelected(
                      parent: AdapterView<*>?,
                      view: View,
                      position: Int,
                      id: Long
                  ) {
                      //spinnerItemBO = empList[position]
                      //callgetReportList(empList.get(position).getId());
                      if (strPermissionTypeLetter.equals("P")) {

                          if (empList[position].hasPermissionForPeriod) {
                              binding.isForPeriodSwitch.isChecked = true
                              binding.isForPeriodSwitch.isClickable = true
                              binding.isForPeriodSwitch.background =
                                  resources.getDrawable(R.drawable.edit_text)
                          } else {
                              binding.isForPeriodSwitch.isChecked = false
                              binding.isForPeriodSwitch.isClickable = false
                              binding.isForPeriodSwitch.background =
                                  resources.getDrawable(R.drawable.edit_disable_text)
                          }

                          if (empList[position].hasFullDayPermission) {
                              binding.isForFullDayCheckBox.isChecked = true
                              binding.isForFullDayCheckBox.isClickable = true
                              binding.isForFullDayCheckBox.background =
                                  resources.getDrawable(R.drawable.edit_text)
                          } else {
                              binding.isForFullDayCheckBox.isChecked = false
                              binding.isForFullDayCheckBox.isClickable = false
                              binding.isForFullDayCheckBox.background =
                                  resources.getDrawable(R.drawable.edit_disable_text)
                          }
                      }
                      customAdapter.notifyDataSetChanged()

                  }

                  override fun onNothingSelected(parent: AdapterView<*>?) {


                  }
              }
  */

        /*
                for (i in empList.indices) {
                    val perm: String = employeeViolations.get(UserShardPrefrences.getSelfServicePos(context)).getPermType()
                    val permList = empList[i].title
                   if (perm.equals(permList, ignoreCase = true)) {
                        binding.tvSelectRequest.setSelection(i)
                    }
                }
        */


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkValidations(isFileMandatory: Boolean) {
        binding.txtToDate.error = null
        binding.txtFromDate.error = null
        binding.edtRemarks.error = null
        binding.txtDate.error = null
        binding.txtFromTime.error = null
        binding.txtToTime.error = null
        binding.edtRemarks.error = null
        binding.txtDuration.error = null

        val fromDate: String = binding.txtFromDate.text.toString()
        val toDate: String = binding.txtToDate.text.toString()
        val remarks: String = binding.edtRemarks.text.toString()
        val duration: String = binding.txtDuration.text.toString()
        val date: String = binding.txtDate.text.toString()
        val fromTime: String = binding.txtFromTime.text.toString()
        val toTime: String = binding.txtToTime.text.toString()
        var cancel = false
        var focusView: View? = null

        if (!binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
            if (binding.isFlexibleSwitch.isChecked) {
                if (TextUtils.isEmpty(duration)) {
                    binding.txtDuration.error = getString(R.string.error_field_required)
                    focusView = binding.txtDuration
                    cancel = true

                }
                if (TextUtils.isEmpty(date)) {
                    binding.txtDate.error = getString(R.string.error_field_required)
                    focusView = binding.txtDate
                    cancel = true
                }
                if (TextUtils.isEmpty(remarks)) {
                    binding.edtRemarks.error = getString(R.string.error_field_required)
                    focusView = binding.edtRemarks
                    cancel = true
                }

            } else {
                /*  if (TextUtils.isEmpty(duration)) {
                      binding.txtDuration.error = getString(R.string.error_field_required)
                      focusView = binding.txtDuration
                      cancel = true
                  }*/
                if (TextUtils.isEmpty(date)) {
                    binding.txtDate.error = getString(R.string.error_field_required)
                    focusView = binding.txtDate
                    cancel = true
                }
                if (TextUtils.isEmpty(fromTime)) {
                    binding.txtFromTime.error = getString(R.string.error_field_required)
                    focusView = binding.txtFromTime
                    cancel = true
                }
                if (TextUtils.isEmpty(toTime)) {
                    binding.txtToTime.error = getString(R.string.error_field_required)
                    focusView = binding.txtToTime
                    cancel = true
                }
                if (TextUtils.isEmpty(remarks)) {
                    binding.edtRemarks.error = getString(R.string.error_field_required)
                    focusView = binding.edtRemarks
                    cancel = true
                }

            }

        }
        //||( !binding.isForFullDayCheckBox.isChecked&&binding.isForPeriodSwitch.isChecked)
        else
            if (!binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
                if (binding.isFlexibleSwitch.isChecked) {

                    if (TextUtils.isEmpty(fromDate)) {
                        binding.txtFromDate.error = getString(R.string.error_field_required)
                        focusView = binding.txtFromDate
                        cancel = true
                    }
                    if (TextUtils.isEmpty(toDate)) {
                        binding.txtToDate.error = getString(R.string.error_field_required)
                        focusView = binding.txtToDate
                        cancel = true
                    }
                    if (TextUtils.isEmpty(duration)) {
                        binding.txtDuration.error = getString(R.string.error_field_required)
                        focusView = binding.txtDuration
                        cancel = true
                    }
                    if (TextUtils.isEmpty(remarks)) {
                        binding.edtRemarks.error = getString(R.string.error_field_required)
                        focusView = binding.edtRemarks
                        cancel = true
                    }

                } else {
                    if (TextUtils.isEmpty(fromDate)) {
                        binding.txtFromDate.error = getString(R.string.error_field_required)
                        focusView = binding.txtFromDate
                        cancel = true
                    }
                    if (TextUtils.isEmpty(toDate)) {
                        binding.txtToDate.error = getString(R.string.error_field_required)
                        focusView = binding.txtToDate
                        cancel = true
                    }
                    if (TextUtils.isEmpty(fromTime)) {
                        binding.txtFromTime.error = getString(R.string.error_field_required)
                        focusView = binding.txtFromTime
                        cancel = true
                    }
                    if (TextUtils.isEmpty(toTime)) {
                        binding.txtToTime.error = getString(R.string.error_field_required)
                        focusView = binding.txtToTime
                        cancel = true
                    }
                    if (TextUtils.isEmpty(remarks)) {
                        binding.edtRemarks.error = getString(R.string.error_field_required)
                        focusView = binding.edtRemarks
                        cancel = true
                    }

                }

            }
        else
            if (!binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
                    if (binding.isFlexibleSwitch.isChecked) {
                        if (TextUtils.isEmpty(date)) {
                            binding.txtDate.error = getString(R.string.error_field_required)
                            focusView = binding.txtDate
                            cancel = true
                        }
                        if (TextUtils.isEmpty(fromTime)) {
                            binding.txtFromTime.error = getString(R.string.error_field_required)
                            focusView = binding.txtFromTime
                            cancel = true
                        }
                        if (TextUtils.isEmpty(toTime)) {
                            binding.txtToTime.error = getString(R.string.error_field_required)
                            focusView = binding.txtToTime
                            cancel = true
                        }
                        if (TextUtils.isEmpty(remarks)) {
                            binding.edtRemarks.error = getString(R.string.error_field_required)
                            focusView = binding.edtRemarks
                            cancel = true
                        }
                    } else {
                        if (TextUtils.isEmpty(date)) {
                            binding.txtDate.error = getString(R.string.error_field_required)
                            focusView = binding.txtDate
                            cancel = true
                        }
                        if (TextUtils.isEmpty(duration)) {
                            binding.txtDuration.error = getString(R.string.error_field_required)
                            focusView = binding.txtDuration
                            cancel = true
                        }
                        if (TextUtils.isEmpty(remarks)) {
                            binding.edtRemarks.error = getString(R.string.error_field_required)
                            focusView = binding.edtRemarks
                            cancel = true
                        }
                    }

                }
        else
            if (!binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
                        if (!binding.isFlexibleSwitch.isChecked) {
                            if (TextUtils.isEmpty(duration)) {
                                binding.txtDuration.error = getString(R.string.error_field_required)
                                focusView = binding.txtDuration
                                cancel = true
                            }
                            if (TextUtils.isEmpty(date)) {
                                binding.txtDate.error = getString(R.string.error_field_required)
                                focusView = binding.txtDate
                                cancel = true
                            }
                            if (TextUtils.isEmpty(remarks)) {
                                binding.edtRemarks.error = getString(R.string.error_field_required)
                                focusView = binding.edtRemarks
                                cancel = true
                            }

                        } else {
                            if (TextUtils.isEmpty(date)) {
                                binding.txtDate.error = getString(R.string.error_field_required)
                                focusView = binding.txtDate
                                cancel = true
                            }
                            if (TextUtils.isEmpty(fromTime)) {
                                binding.txtFromTime.error = getString(R.string.error_field_required)
                                focusView = binding.txtFromTime
                                cancel = true
                            }
                            if (TextUtils.isEmpty(toTime)) {
                                binding.txtToTime.error = getString(R.string.error_field_required)
                                focusView = binding.txtToTime
                                cancel = true
                            }
                            if (TextUtils.isEmpty(remarks)) {
                                binding.edtRemarks.error = getString(R.string.error_field_required)
                                focusView = binding.edtRemarks
                                cancel = true
                            }

                        }


                    }
        else
            if (!binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked) {
                            if (binding.isFlexibleSwitch.isChecked) {
                                if (TextUtils.isEmpty(fromDate)) {
                                    binding.txtFromDate.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtFromDate
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(toDate)) {
                                    binding.txtToDate.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtToDate
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(duration)) {
                                    binding.txtDuration.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtDuration
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(remarks)) {
                                    binding.edtRemarks.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.edtRemarks
                                    cancel = true
                                }

                            } else {
                                if (TextUtils.isEmpty(fromDate)) {
                                    binding.txtFromDate.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtFromDate
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(toDate)) {
                                    binding.txtToDate.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtToDate
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(fromTime)) {
                                    binding.txtFromTime.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtFromTime
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(toTime)) {
                                    binding.txtToTime.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.txtToTime
                                    cancel = true
                                }
                                if (TextUtils.isEmpty(remarks)) {
                                    binding.edtRemarks.error =
                                        getString(R.string.error_field_required)
                                    focusView = binding.edtRemarks
                                    cancel = true
                                }

                            }

                        }
        else
            if (!binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked) {
                                if (binding.isFlexibleSwitch.isChecked) {
                                    if (TextUtils.isEmpty(date)) {
                                        binding.txtDate.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtDate
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(duration)) {
                                        binding.txtDuration.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtDuration
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(remarks)) {
                                        binding.edtRemarks.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.edtRemarks
                                        cancel = true
                                    }
                                } else {
                                    if (TextUtils.isEmpty(date)) {
                                        binding.txtDate.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtDate
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(fromTime)) {
                                        binding.txtFromTime.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtFromTime
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(toTime)) {
                                        binding.txtToTime.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtToTime
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(remarks)) {
                                        binding.edtRemarks.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.edtRemarks
                                        cancel = true
                                    }
                                }

                            }
        else
            if (binding.isForFullDayCheckBox.isChecked && binding.isForPeriodSwitch.isChecked && !binding.isFlexibleSwitch.isChecked) {
                                    if (TextUtils.isEmpty(fromDate)) {
                                        binding.txtFromDate.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtFromDate
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(toDate)) {
                                        binding.txtToDate.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.txtToDate
                                        cancel = true
                                    }
                                    if (TextUtils.isEmpty(remarks)) {
                                        binding.edtRemarks.error =
                                            getString(R.string.error_field_required)
                                        focusView = binding.edtRemarks
                                        cancel = true
                                    }
                                }
        else
            if (binding.isForFullDayCheckBox.isChecked && !binding.isForPeriodSwitch.isChecked && !binding.isFlexibleSwitch.isChecked) {
                                        if (TextUtils.isEmpty(date)) {
                                            binding.txtDate.error =
                                                getString(R.string.error_field_required)
                                            focusView = binding.txtDate
                                            cancel = true
                                        }
                                        if (TextUtils.isEmpty(remarks)) {
                                            binding.edtRemarks.error =
                                                getString(R.string.error_field_required)
                                            focusView = binding.edtRemarks
                                            cancel = true
                                        }
            }

        if(!binding.rbEntity.isChecked &&!binding.rbWorkLocations.isChecked &&!binding.rbWorkGroup.isChecked
            && !binding.selectAllSwitch.isChecked && EnumUtils.isNetworkConnected(context) && binding.spCompany.selectedItemPosition!=0)
        {
            focusView=binding.rbEntity
            cancel = true
            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                context,
               resources.getString(R.string.entity_type_txt),
                R.drawable.caution,
                resources.getColor(R.color.red)
            )
        }

        if (cancel) {
            focusView!!.requestFocus()
            if (EnumUtils.isNetworkConnected(context) && str_company_id == "0" || str_company_id == null) {

                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                    context,
                    resources.getString(R.string.select_company),
                    R.drawable.caution,
                    resources.getColor(R.color.red)
                )
            } else
                if (EnumUtils.isNetworkConnected(context) && binding.spAll.selectedItemPosition == 0
                    || EnumUtils.isNetworkConnected(context) && binding.spAll.selectedItemPosition > 1 && binding.spEmployees.selectedItemPosition == 0
                ) {

                    when (filter_type) {
                        1 -> {
                            if (binding.spAll.selectedItemPosition > 0 && binding.spEmployees.size == 0 ||
                                binding.spAll.selectedItemPosition > 0 && binding.spEmployees.selectedItemPosition == 0
                            ) {
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context,
                                    "${resources.getString(R.string.select_emp_txt)} ${str_select_entity_name.toString()}",
                                    R.drawable.caution,
                                    resources.getColor(R.color.red)
                                )
                            } else
                                if (binding.spAll.selectedItemPosition == 0 && binding.spEmployees.size == 0) {
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        resources.getString(R.string.select_entity_subtype_txt),
                                        R.drawable.caution,
                                        resources.getColor(R.color.red)
                                    )
                                }

                        }
                        2 -> {
                            if (binding.spEmployees.size == 0 && binding.spEmployees.selectedItemPosition == 0 ||
                                binding.spAll.selectedItemPosition > 0 && binding.spEmployees.selectedItemPosition == 0
                            ) {
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context,
                                    "${resources.getString(R.string.select_emp_txt)} ${str_select_workLoc_name.toString()}",
                                    R.drawable.caution,
                                    resources.getColor(R.color.red)
                                )
                            } else
                                if (binding.spAll.selectedItemPosition == 0 && binding.spEmployees.size == 0) {
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        resources.getString(R.string.select_workloc_txt),
                                        R.drawable.caution,
                                        resources.getColor(R.color.red)
                                    )

                                }
                        }
                        3 -> {
                            if (binding.spEmployees.size == 0 && binding.spEmployees.selectedItemPosition == 0 ||
                                binding.spAll.selectedItemPosition > 0 && binding.spEmployees.selectedItemPosition == 0
                            ) {
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context,
                                    "${resources.getString(R.string.select_emp_txt)} ${str_select_workGroup_name.toString()}",
                                    R.drawable.caution,
                                    resources.getColor(R.color.red)
                                )
                            } else
                                if (binding.spAll.selectedItemPosition == 0 && binding.spEmployees.size == 0) {
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        resources.getString(R.string.select_workgroup_txt),
                                        R.drawable.caution,
                                        resources.getColor(R.color.red)
                                    )

                                }

                        }
                    }

                }
                else
                    if(EnumUtils.isNetworkConnected(context) && type_id == 0){
                        if (binding.selectAllSwitch.isChecked && binding.rbWorkLocations.isChecked && !binding.rbEntity.isChecked && !binding.rbWorkGroup.isChecked ||
                            binding.selectAllSwitch.isChecked && binding.rbEntity.isChecked && !binding.rbWorkLocations.isChecked && !binding.rbWorkGroup.isChecked ||
                            binding.selectAllSwitch.isChecked && binding.rbWorkGroup.isChecked && !binding.rbWorkLocations.isChecked && !binding.rbEntity.isChecked||
                            binding.selectAllSwitch.isChecked && !binding.rbEntity.isChecked && !binding.rbWorkLocations.isChecked && !binding.rbWorkGroup.isChecked ){
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
                                resources.getString(R.string.select_permission),
                                R.drawable.caution,
                                resources.getColor(R.color.red))
                        }
                        else
                            if (!binding.selectAllSwitch.isChecked && binding.rbWorkLocations.isChecked && !binding.rbEntity.isChecked && !binding.rbWorkGroup.isChecked ||
                                !binding.selectAllSwitch.isChecked && binding.rbEntity.isChecked && !binding.rbWorkLocations.isChecked && !binding.rbWorkGroup.isChecked ||
                                !binding.selectAllSwitch.isChecked && binding.rbWorkGroup.isChecked && !binding.rbWorkLocations.isChecked && !binding.rbEntity.isChecked )
                            {
                                if (binding.spAll.selectedItemPosition ==0 || binding.spAll.selectedItemPosition > 0 && binding.spEmployees.selectedItemPosition ==0)
                                {
                                    when(filter_type){
                                        1 ->{
                                            if (binding.spAll.selectedItemPosition >0 && binding.spEmployees.size == 0 ||
                                                binding.spAll.selectedItemPosition >0 &&  binding.spEmployees.selectedItemPosition ==0) {
                                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                    context,
                                                    "${resources.getString(R.string.select_emp_txt)} ${str_select_entity_name.toString()}",
                                                    R.drawable.caution,
                                                    resources.getColor(R.color.red))
                                            } else
                                                if (binding.spAll.selectedItemPosition ==0 && binding.spEmployees.size == 0 && !binding.selectAllSwitch.isChecked)
                                                {
                                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                        context,
                                                        resources.getString(R.string.select_entity_subtype_txt),
                                                        R.drawable.caution,
                                                        resources.getColor(R.color.red))
                                                }

                                        }
                                        2->{
                                            if (binding.spEmployees.size == 0 && binding.spEmployees.selectedItemPosition ==0 ||
                                                binding.spAll.selectedItemPosition >0 &&  binding.spEmployees.selectedItemPosition ==0) {
                                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                    context,
                                                    "${resources.getString(R.string.select_emp_txt)} ${str_select_workLoc_name.toString()}",
                                                    R.drawable.caution,
                                                    resources.getColor(R.color.red))
                                            }
                                            else
                                                if (binding.spAll.selectedItemPosition ==0 && binding.spEmployees.size == 0 && !binding.selectAllSwitch.isChecked )
                                                {
                                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                        context,
                                                        resources.getString(R.string.select_workloc_txt),
                                                        R.drawable.caution,
                                                        resources.getColor(R.color.red))

                                                }
                                        }
                                        3->{
                                            if (binding.spEmployees.size == 0 && binding.spEmployees.selectedItemPosition ==0 ||
                                                binding.spAll.selectedItemPosition >0 &&  binding.spEmployees.selectedItemPosition ==0) {
                                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                    context,
                                                    "${resources.getString(R.string.select_emp_txt)} ${str_select_workGroup_name.toString()}",
                                                    R.drawable.caution,
                                                    resources.getColor(R.color.red))
                                            }
                                            else
                                                if (binding.spAll.selectedItemPosition ==0 && binding.spEmployees.size == 0 && !binding.selectAllSwitch.isChecked )
                                                {
                                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                        context,
                                                        resources.getString(R.string.select_workgroup_txt),
                                                        R.drawable.caution,
                                                        resources.getColor(R.color.red))

                                                }

                                        }
                                    }

                                } else
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        resources.getString(R.string.select_permission),
                                        R.drawable.caution,
                                        resources.getColor(R.color.red))

                            }
                    }
                /*if (EnumUtils.isNetworkConnected(context) && type_id == 0 && binding.rbEntity.isChecked &&!binding.rbWorkLocations.isChecked &&!binding.rbWorkGroup.isChecked
                 && !binding.selectAllSwitch.isChecked ) {

                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                            context,
                            resources.getString(R.string.select_permission),
                            R.drawable.caution,
                            resources.getColor(R.color.red)
                        )

                    }*/
        }else {
            try {
                if (EnumUtils.isNetworkConnected(context)) {
                    if (!isFileMandatory ){
                        if (str_company_id == "0" || str_company_id == null) {
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
                                resources.getString(R.string.select_company),
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )
                        }
                        else if (type_id == 0){
                            if (binding.selectAllSwitch.isChecked && binding.rbWorkLocations.isChecked && !binding.rbEntity.isChecked && !binding.rbWorkGroup.isChecked ||
                                binding.selectAllSwitch.isChecked && binding.rbEntity.isChecked && !binding.rbWorkLocations.isChecked && !binding.rbWorkGroup.isChecked ||
                                binding.selectAllSwitch.isChecked && binding.rbWorkGroup.isChecked && !binding.rbWorkLocations.isChecked && !binding.rbEntity.isChecked ||
                                binding.selectAllSwitch.isChecked && !binding.rbEntity.isChecked && !binding.rbWorkLocations.isChecked && !binding.rbWorkGroup.isChecked){
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context,
                                    resources.getString(R.string.select_permission),
                                    R.drawable.caution,
                                    resources.getColor(R.color.red))
                            } else
                            if (!binding.selectAllSwitch.isChecked && binding.rbWorkLocations.isChecked && !binding.rbEntity.isChecked && !binding.rbWorkGroup.isChecked ||
                                !binding.selectAllSwitch.isChecked && binding.rbEntity.isChecked && !binding.rbWorkLocations.isChecked && !binding.rbWorkGroup.isChecked ||
                                !binding.selectAllSwitch.isChecked && binding.rbWorkGroup.isChecked && !binding.rbWorkLocations.isChecked && !binding.rbEntity.isChecked )
                            {
                                if (binding.spAll.selectedItemPosition ==0 || binding.spAll.selectedItemPosition > 0 && binding.spEmployees.selectedItemPosition ==0)
                                {
                                    when(filter_type){
                                        1 ->{
                                            if (binding.spAll.selectedItemPosition >0 && binding.spEmployees.size == 0 ||
                                                binding.spAll.selectedItemPosition >0 &&  binding.spEmployees.selectedItemPosition ==0) {
                                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                    context,
                                                    "${resources.getString(R.string.select_emp_txt)} ${str_select_entity_name.toString()}",
                                                    R.drawable.caution,
                                                    resources.getColor(R.color.red))
                                            } else
                                                if (binding.spAll.selectedItemPosition ==0 && binding.spEmployees.size == 0 && !binding.selectAllSwitch.isChecked)
                                                {
                                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                        context,
                                                        resources.getString(R.string.select_entity_subtype_txt),
                                                        R.drawable.caution,
                                                        resources.getColor(R.color.red))
                                                }

                                        }
                                        2->{
                                            if (binding.spEmployees.size == 0 && binding.spEmployees.selectedItemPosition ==0 ||
                                                binding.spAll.selectedItemPosition >0 &&  binding.spEmployees.selectedItemPosition ==0) {
                                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                    context,
                                                    "${resources.getString(R.string.select_emp_txt)} ${str_select_workLoc_name.toString()}",
                                                    R.drawable.caution,
                                                    resources.getColor(R.color.red))
                                            }
                                            else
                                                if (binding.spAll.selectedItemPosition ==0 && binding.spEmployees.size == 0 && !binding.selectAllSwitch.isChecked )
                                                {
                                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                        context,
                                                        resources.getString(R.string.select_workloc_txt),
                                                        R.drawable.caution,
                                                        resources.getColor(R.color.red))

                                                }
                                        }
                                        3->{
                                            if (binding.spEmployees.size == 0 && binding.spEmployees.selectedItemPosition ==0 ||
                                                binding.spAll.selectedItemPosition >0 &&  binding.spEmployees.selectedItemPosition ==0) {
                                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                    context,
                                                    "${resources.getString(R.string.select_emp_txt)} ${str_select_workGroup_name.toString()}",
                                                    R.drawable.caution,
                                                    resources.getColor(R.color.red))
                                            }
                                            else
                                                if (binding.spAll.selectedItemPosition ==0 && binding.spEmployees.size == 0 && !binding.selectAllSwitch.isChecked )
                                                {
                                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                                        context,
                                                        resources.getString(R.string.select_workgroup_txt),
                                                        R.drawable.caution,
                                                        resources.getColor(R.color.red))

                                                }

                                        }
                                    }

                                } else
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context,
                                    resources.getString(R.string.select_permission),
                                    R.drawable.caution,
                                    resources.getColor(R.color.red))

                            }else{
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context,
                                    resources.getString(R.string.entity_type_txt),
                                    R.drawable.caution,
                                    resources.getColor(R.color.red))
                            }


                        }
                        else if (binding.spAll.selectedItemPosition ==0 && !binding.selectAllSwitch.isChecked
                            || binding.spAll.selectedItemPosition > 0 && binding.spEmployees.selectedItemPosition ==0 && !binding.selectAllSwitch.isChecked
                            )
                        {
                            when(filter_type){
                                1 ->{
                                    if (binding.spAll.selectedItemPosition >0 && binding.spEmployees.size == 0 ||
                                        binding.spAll.selectedItemPosition >0 &&  binding.spEmployees.selectedItemPosition ==0) {
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                            context,
                                            "${resources.getString(R.string.select_emp_txt)} ${str_select_entity_name.toString()}",
                                            R.drawable.caution,
                                            resources.getColor(R.color.red))
                                    } else
                                    if (binding.spAll.selectedItemPosition ==0 && binding.spEmployees.size == 0 && !binding.selectAllSwitch.isChecked)
                                    {
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                            context,
                                            "Please select the Entity Sub Type",
                                            R.drawable.caution,
                                            resources.getColor(R.color.red))
                                    }

                                }
                                2->{
                                    if (binding.spEmployees.size == 0 && binding.spEmployees.selectedItemPosition ==0 ||
                                        binding.spAll.selectedItemPosition >0 &&  binding.spEmployees.selectedItemPosition ==0) {
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                            context,
                                            "${resources.getString(R.string.select_emp_txt)} ${str_select_workLoc_name.toString()}",
                                            R.drawable.caution,
                                            resources.getColor(R.color.red))
                                    }
                                    else
                                        if (binding.spAll.selectedItemPosition ==0 && binding.spEmployees.size == 0 && !binding.selectAllSwitch.isChecked )
                                        {
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                            context,
                                            "Please select the WorkLocation",
                                            R.drawable.caution,
                                            resources.getColor(R.color.red))

                                    }
                                }
                                3->{
                                    if (binding.spEmployees.size == 0 && binding.spEmployees.selectedItemPosition ==0 ||
                                        binding.spAll.selectedItemPosition >0 &&  binding.spEmployees.selectedItemPosition ==0) {
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                            context,
                                            "${resources.getString(R.string.select_emp_txt)} ${str_select_workGroup_name.toString()}",
                                            R.drawable.caution,
                                            resources.getColor(R.color.red))
                                    }
                                    else
                                        if (binding.spAll.selectedItemPosition ==0 && binding.spEmployees.size == 0 && !binding.selectAllSwitch.isChecked )
                                        {
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                            context,
                                            "Please select the WorkGroup",
                                            R.drawable.caution,
                                            resources.getColor(R.color.red))

                                    }

                                }
                            }

                        }
                        else
                        {
                            callPermissionRequestsAPI(type_id!!)
                        }
                    }else
                    {
                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                            context,
                            getString(R.string.file_mandatory_txt),
                            R.drawable.caution,
                            resources.getColor(R.color.red)
                        )
                    }


                }
                else {

                        if(!EnumUtils.isNetworkConnected(context)){
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
                                getString(R.string.no_internet_connection),
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )
                        }

                }
            } catch (e: Exception) {
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.selectedFileImageView -> {
                //     uploadedFileClicked()
            }

            R.id.txt_uploadfile -> openFilePicker()


            R.id.txt_cancel -> (activity as MainActivity).onBackPressed()

            R.id.img_back -> try {
                (activity as MainActivity).onBackPressed()
            }catch (e:Exception){
                Log.d("onBackClick", "onClick: $e")
            }

            //   replaceFragment(RequestsFragment(), R.id.flFragment, false)


            R.id.txt_save -> {

                checkValidations(isFileMandatory)


            }

            R.id.txt_from_date -> {

                datePicker(binding.txtFromDate.text.toString(), binding.txtFromDate)

            }
            R.id.txt_to_date -> {

                datePicker(binding.txtToDate.text.toString(), binding.txtToDate)


            }
            R.id.txt_duration -> {
                timePicker(binding.txtDuration)
            }
            R.id.txt_date -> {
                datePicker(binding.txtDate.text.toString(), binding.txtDate)

            }



            R.id.txt_to_time -> {
                timePicker(binding.txtToTime)
            }
            R.id.txt_from_time -> {
                timePicker(binding.txtFromTime)

            }


            R.id.txt_reasons_time -> {
                timePicker(binding.txtReasonsTime)

            }
            R.id.selectedFilecancelbtn ->{
                binding.selectedFilecancelbtn.visibility=View.GONE
                binding.selectedFileImageView.visibility=View.GONE
                binding.txtFilename.visibility=View.GONE
                isFileMandatory = true
                base64String = ""
                fileTypeStr =""


            }
        }
    }
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*" // All file types
        startActivityForResult(intent, FILE_PICK_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->

                val selectedPdfFileUri: Uri = uri
                base64String = FileUtils.uriToBase64(mContext!!, selectedPdfFileUri)
                val mimeType = FileUtils.getMimeType(mContext!!, selectedPdfFileUri)
                val fileName = FileUtils.getFileNameFromUri(requireContext(), selectedPdfFileUri)
                println("File Name: $fileName")
                binding.selectedFileImageView.visibility = View.VISIBLE
                binding.txtFilename.visibility = View.VISIBLE
                binding.selectedFilecancelbtn.visibility=View.VISIBLE
                binding.txtFilename.text = fileName
                val fileExtension = getFileExtension(fileName)
                if (fileExtension.isNotEmpty()) {
                    fileTypeStr = fileExtension
                    getDocumentIconResId(fileTypeStr)
                    isFileMandatory=false
                    println("File extension: $fileExtension")
                } else {
                    fileTypeStr = ""
                    println("No file extension found.")
                }

                if (base64String != null) {

                    Log.e("base64String", base64String!!)

                } else {
                    // Handle the case where conversion failed
                }

            }
        }
    }

    private fun getDocumentIconResId(fileTypeStr: String?) {
        if (fileTypeStr!!.contains("pdf")) {
            //   fileTypeStr = "pdf"
            binding.selectedFileImageView.setImageResource(R.drawable.pdf)

        } else if (fileTypeStr!!.contains("jpeg")) {
            // fileTypeStr = "jpeg"
            binding.selectedFileImageView.setImageResource(R.drawable.jpeg)
        } else if (fileTypeStr!!.contains("jpg")) {
            // fileTypeStr = "jpg"/**/
            binding.selectedFileImageView.setImageResource(R.drawable.jpeg)
        } else if (fileTypeStr!!.contains("png")) {
            //  fileTypeStr = "png"
            binding.selectedFileImageView.setImageResource(R.drawable.png)
        } else if ((fileTypeStr!!.contains("xlsx")) || (fileTypeStr!!.contains("xls")) ||
            (fileTypeStr!!.contains("xlsm")) || (fileTypeStr!!.contains("xlsb"))
        ) {
            // fileTypeStr = "xlsx"
            binding.selectedFileImageView.setImageResource(R.drawable.excel)
        } else if (fileTypeStr!!.contains("docx") || (fileTypeStr!!.contains("doc"))

            || fileTypeStr!!.contains("txt")
        ) {
            // fileTypeStr = "docx"
            binding.selectedFileImageView.setImageResource(R.drawable.document)
        } else if (fileTypeStr!!.contains("msg")) {
            // fileTypeStr = "msg"
            binding.selectedFileImageView.setImageResource(R.drawable.msg)
        } else if (fileTypeStr!!.contains("mp4")){
            binding.selectedFilecancelbtn.visibility=View.GONE
            binding.txtFilename.visibility=View.GONE
            binding.selectedFileImageView.setImageResource(R.drawable.document)
            showDialog(mContext!!,
                resources.getString(R.string.title_upload_file_not_support_txt),
                resources.getString(R.string.upload_file_not_support_txt),
                R.drawable.app_icon,
                resources.getColor(R.color.red))
        } else
        {
            binding.selectedFilecancelbtn.visibility=View.GONE
            binding.txtFilename.visibility=View.GONE
            binding.selectedFileImageView.setImageResource(R.drawable.document)
            showDialog(mContext!!,
                resources.getString(R.string.title_upload_file_not_support_txt),
                resources.getString(R.string.upload_file_not_support_txt),
                R.drawable.app_icon,
                resources.getColor(R.color.red))

        }

    }
    private fun showDialog(
        context: Context,
        messageheading: String,

        message: String,
        drawable_icon: Int,
        color: Int,
    ) {
        // Create a new instance of the AlertDialog.Builder class
        /* val builder = AlertDialog.Builder(context)
         builder.setTitle(title)//Set the tittle
         builder.setMessage(mess)//Set the message of the dialog
         builder.setPositiveButton("OK") { dialog, _ ->
             // Implement the Code when OK Button is Clicked
             dialog.dismiss()

             b_show_out_office_dialog = false
         }
         *//* builder.setNegativeButton("Cancel") { dialog, _ ->
             // Implement the Code when Cancel Button is CLiked
             dialog.dismiss() //Close or dismiss the alert dialog
         }*//*
        b_show_out_office_dialog = true
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()*/




        try {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var alertDialogView: View? = null
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_, null)
                alertDialog.setView(alertDialogView)
                val textDialog = alertDialogView.findViewById<TextView>(R.id.diag_text)
                textDialog.text = message
                val diag_ok = alertDialogView.findViewById<TextView>(R.id.diag_ok)

                val icon = alertDialogView.findViewById<ImageView>(R.id.icon)

                //   icon.setImageResource(drawable_icon)

                val resourceName = context.resources.getResourceEntryName(drawable_icon)
                Log.e("resourceName", resourceName)
                if (resourceName.contains("caution")) {
                    icon.setImageResource(drawable_icon)
                }
                diag_ok.setBackgroundColor(color)
                val textHeadingDialog =
                    alertDialogView.findViewById<TextView>(R.id.diag_text_heading)
                textHeadingDialog.text = messageheading


                val show = alertDialog.show()
                val window = show.window
                if (window != null) {
                    window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    //TODO Dialog Size
                    show.window!!.setLayout(
                        SnackBarUtil.getWidth(context) / 100 * 90,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    show.window!!.setGravity(Gravity.CENTER)
                }
                diag_ok.setOnClickListener {
                    show.dismiss()


                }

                alertDialog.setCancelable(false)
            }
//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }


    }

    private fun pickDate(day: Int, month: Int, year: Int, textView: TextView) {
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"AdminPermissionFragment")
        datePickerFragment.show(parentFragmentManager, "datePicker")
       /* val now = Calendar.getInstance()
        now[year, month - 1] = day
        val dpd = DatePickerDialog.newInstance(
            { view, year, monthOfYear, dayOfMonth ->
                var monthOfYear = monthOfYear
                try {
                    val yearNew = year
                    monthOfYear = monthOfYear + 1
                    var monthnew = ""
                    var daynew = ""
                    monthnew = if (monthOfYear < 10) {
                        "0$monthOfYear"
                    } else {
                        "" + monthOfYear
                    }
                    daynew = if (dayOfMonth < 10) {
                        "0$dayOfMonth"
                    } else {
                        "" + dayOfMonth
                    }
                    textView.text = "$yearNew-$monthnew-$daynew"
                } catch (e: java.lang.Exception) {
                }
            }, now[Calendar.YEAR], now[Calendar.MONTH], now[Calendar.DAY_OF_MONTH]
        )
        dpd.locale= Locale.ENGLISH
        dpd.setOkText("OK")
        dpd.setCancelText("Cancel")
        dpd.version = DatePickerDialog.Version.VERSION_2
        dpd.show(requireActivity().supportFragmentManager, "Datepickerdialog")*/

    }

    private fun datePicker(date: String, txt_Date: TextView) {
        try {
            // val todate: String = binding.txtToDate.getText().toString()
            if (TextUtils.isEmpty(date)) {
                val c = Calendar.getInstance()
                val mDay = c[Calendar.DAY_OF_MONTH]
                val mMonth = c[Calendar.MONTH] + 1
                val mYear = c[Calendar.YEAR]
                pickDate(mDay, mMonth, mYear, txt_Date)
            } else {
                val dateString = date.split("-").toTypedArray()
                pickDate(
                    dateString[2].toInt(), dateString[1].toInt(), dateString[0].toInt(), txt_Date
                )
            }
        } catch (e: Exception) {
            e.message
        }

    }

    private fun timePicker(textView: TextView) {
        val now = Calendar.getInstance()
        var hours: Int
        val timePickerDialog = TimePickerDialog.newInstance(
            { view, hourOfDay, minute, second -> //  textView.setText(hourOfDay + ":" + minute);
                if (hourOfDay > 12) {
                    hours = hourOfDay
                } else if (hourOfDay == 12) {
                    hours = 12
                } else if (hourOfDay == 0) {
                    hours = 12
                } else {
                    hours = hourOfDay
                }
                textView.text =
                    LocaleHelper.arabicToEnglish(String.format("%02d", hours) + ":" + String.format("%02d", minute))
            }, now[Calendar.HOUR_OF_DAY], now[Calendar.MINUTE], false
        )
        timePickerDialog.setLocale(Locale.ENGLISH)
        timePickerDialog.setOkText("OK")
        timePickerDialog.setCancelText("Cancel")
        timePickerDialog.version = TimePickerDialog.Version.VERSION_2
        timePickerDialog.accentColor = ContextCompat.getColor(mContext!!, R.color.mdtp_accent_color)
        timePickerDialog.show(requireActivity().supportFragmentManager, "TimePickerDialog")
    }


    private fun setPermissionRequestLayoutsVisible() {

        binding.lReasons.visibility = View.GONE
        binding.lLeavePermission.visibility = View.VISIBLE
        binding.lPermissiontype.visibility = View.VISIBLE
        binding.lRemarks.visibility = View.VISIBLE
        binding.lFlexible.visibility= View.VISIBLE



    }
    fun downloadFile(context: Context, fileUrl: String, title: String, mimeType: String) {

        SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,"Downloading File..",R.drawable.app_icon,colorPrimary)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // Create a DownloadManager.Request with the file URL
        val request = DownloadManager.Request(Uri.parse(fileUrl))

        // Set the title and description for the download
        request.setTitle(title)
        request.setDescription("Downloading File..")

        // Set the destination folder and file name
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "$title.${getFileExtension(fileUrl)}"
        )

        // Set the MIME type of the file
        request.setMimeType(mimeType)

        // Enqueue the download and get a download ID
        val downloadId = downloadManager.enqueue(request)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {


            R.id.select_all_switch -> {
                if(isChecked) {

                   /* str_entity_id = "0"
                    str_workGroup_id="0"
                    str_workLocation_id="0"*/
                    filter_type = 1
                    callAdminAPI(filter_type,str_entity_id,str_workGroup_id,str_workLocation_id)



                    binding.spEmployees.isEnabled = false
                    binding.spEmployees.isClickable = false
                    binding.spEmployees.background =
                        resources.getDrawable(R.drawable.edit_disable_text)



                    binding.spAll.isEnabled = false
                    binding.spAll.isClickable = false
                    binding.spAll.background =
                        resources.getDrawable(R.drawable.edit_disable_text)

                }

                else{
                    binding.spEmployees.isEnabled = true
                    binding.spEmployees.isClickable = true
                    binding.spEmployees.background =
                        resources.getDrawable(R.drawable.edit_text)

                    binding.spAll.isEnabled = true
                    binding.spAll.isClickable = true
                    binding.spAll.background =
                        resources.getDrawable(R.drawable.edit_text)
                }
            }



            R.id.is_for_period_switch -> {
                if (isChecked) {
                    binding.lFromDate.visibility = View.VISIBLE
                    binding.lToDate.visibility = View.VISIBLE
                    binding.lDate.visibility = View.GONE

                } else {
                    binding.lFromDate.visibility = View.GONE
                    binding.lToDate.visibility = View.GONE
                    binding.lDate.visibility = View.VISIBLE
                }
            }


            R.id.is_for_full_day_check_box -> {
                if (isChecked) {
                    binding.lToTime.visibility = View.GONE
                    binding.lFromTime.visibility = View.GONE
                    binding.lDuration.visibility = View.GONE

                    isFullDay = true



                    binding.isFlexibleSwitch.isChecked = false
                    binding.isFlexibleSwitch.isClickable = false
                    binding.isFlexibleSwitch.background =
                        resources.getDrawable(R.drawable.edit_disable_text)

                } else {
                    binding.lToTime.visibility = View.VISIBLE
                    binding.lFromTime.visibility = View.VISIBLE
                    binding.lDuration.visibility = View.GONE
                    isFullDay =false

                    binding.isFlexibleSwitch.isChecked = false
                    binding.isFlexibleSwitch.isClickable = true
                    binding.isFlexibleSwitch.background =
                        resources.getDrawable(R.drawable.edit_text)


                }
            }

            R.id.is_flexible_switch -> {





                if (isChecked) {

                    binding.lDuration.visibility = View.VISIBLE

                    binding.lToTime.visibility = View.GONE
                    binding.lFromTime.visibility = View.GONE

                } else {

                    if(binding.isForFullDayCheckBox.isChecked){
                        binding.lDuration.visibility = View.GONE

                        binding.lToTime.visibility = View.GONE
                        binding.lFromTime.visibility = View.GONE
                    }
                    else{
                        binding.lDuration.visibility = View.GONE

                        binding.lToTime.visibility = View.VISIBLE
                        binding.lFromTime.visibility = View.VISIBLE
                    }


                }
            }


        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


        when (parent?.id) {

            R.id.sp_select_request -> {

                if(position==0){

                }
                else{
                    type_id = arrayList_type_leave_permission[position-1].id
                    duration = arrayList_type_leave_permission[position-1].maxDuration
                    isFlexible = arrayList_type_leave_permission[position-1].hasFlexiblePermission
                    isFileMandatory=arrayList_type_leave_permission[position-1].attachmentIsMandatory




                    if (arrayList_type_leave_permission[position-1].hasPermissionForPeriod) {
                        binding.isForPeriodSwitch.isChecked = false
                        binding.isForPeriodSwitch.isClickable = true
                        binding.isForPeriodSwitch.background =
                            resources.getDrawable(R.drawable.edit_text)
                    } else {
                        binding.isForPeriodSwitch.isChecked = false
                        binding.isForPeriodSwitch.isClickable = false
                        binding.isForPeriodSwitch.background =
                            resources.getDrawable(R.drawable.edit_disable_text)
                    }


                    if (arrayList_type_leave_permission[position-1].hasFullDayPermission) {
                        binding.isForFullDayCheckBox.isChecked = false
                        binding.isForFullDayCheckBox.isClickable = true
                        binding.isForFullDayCheckBox.background =
                            resources.getDrawable(R.drawable.edit_text)
                    } else {
                        binding.isForFullDayCheckBox.isChecked = false
                        binding.isForFullDayCheckBox.isClickable = false
                        binding.isForFullDayCheckBox.background =
                            resources.getDrawable(R.drawable.edit_disable_text)

                    }

                    if (arrayList_type_leave_permission[position-1].hasFlexiblePermission) {
                        binding.isFlexibleSwitch.isChecked = false
                        binding.isFlexibleSwitch.isClickable = true
                        binding.isFlexibleSwitch.background =
                            resources.getDrawable(R.drawable.edit_text)
                    } else {
                        binding.isFlexibleSwitch.isChecked = false
                        binding.isFlexibleSwitch.isClickable = false
                        binding.isFlexibleSwitch.background =
                            resources.getDrawable(R.drawable.edit_disable_text)
                    }

                }


            }
            R.id.sp_company -> {
                if (position==0){
                    str_company_id ="0"
                }
                else{


                    str_company_id = arrayList_Company[position-1].emp_id.toString()
                    callRequestsAPI("P")
                    binding.spEmployees.isEnabled = true
                    binding.spEmployees.isClickable = true
                    binding.spEmployees.background =
                        resources.getDrawable(R.drawable.edit_text)

                    binding.spAll.isEnabled = true
                    binding.spAll.isClickable = true
                    binding.spAll.background =
                        resources.getDrawable(R.drawable.edit_text)
                }
                binding.rg.clearCheck()
                binding.selectAllSwitch.isChecked= false
                filter_type =1

              //  str_company_id = "1062"
              //

            }

            R.id.sp_all -> {


                if (position==0){

                }
                else{
                    if(filter_type==1){
                        str_entity_id = arrayList_All[position-1].emp_id.toString()
                        str_select_entity_name= arrayList_All[position-1].title.toString()
                        str_workLocation_id ="0"
                        str_workGroup_id ="0"
                        callAdminAPI(filter_type, str_entity_id, str_workGroup_id, str_workLocation_id)
                    }
                    else  if(filter_type==2){
                        str_workLocation_id = arrayList_All[position-1].emp_id.toString()
                        str_select_workLoc_name= arrayList_All[position-1].title.toString()
                        str_entity_id = "0"
                        str_workGroup_id = "0"
                        callAdminAPI(filter_type, str_entity_id, str_workGroup_id, str_workLocation_id)
                    }
                    else if(filter_type==3){
                        str_workGroup_id = arrayList_All[position-1].emp_id.toString()
                        str_select_workGroup_name=arrayList_All[position-1].title.toString()
                        str_entity_id = "0"
                        str_workLocation_id = "0"
                        callAdminAPI(filter_type, str_entity_id, str_workGroup_id, str_workLocation_id)
                    }

                }


            }

            R.id.sp_employees -> {


                if (position==0){

                }
                else{
                    if(!binding.selectAllSwitch.isChecked){
                        if(arrayList_Employees.size>0){
                            employeeIdsString.clear()
                            employeeIdsString.append(arrayList_Employees[position-1].emp_id.toString())
                        }

                    }

                }


            }

        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {





    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_entity -> {
                callRequestsAPI("P")
                filter_type = 1
                callCompanyAPI(str_company_id!!.toInt(),1)
                type_id=0
                isFileMandatory=false
            }
            R.id.rb_work_locations -> {
                callRequestsAPI("P")
                filter_type = 2
                callCompanyAPI(str_company_id!!.toInt(),2)
                type_id=0
                isFileMandatory=false

            }
            R.id.rb_work_group -> {
                callRequestsAPI("P")
                filter_type = 3
                callCompanyAPI(str_company_id!!.toInt(),3)
                type_id=0
                isFileMandatory=false
            }
        }
    }
    private fun callAdminAPI(
        filter_type: Int,
        str_entity_id: String?,
        str_workGroup_id: String?,
        str_workLocation_id: String?
    ) {
        addObserverAdmin()
        getAdminEmployeeDetails(filter_type, str_entity_id, str_workGroup_id, str_workLocation_id)
    }
    private fun getAdminEmployeeDetails(

        filter_type: Int,
        str_entity_id: String?,
        str_workGroup_id: String?,
        str_workLocation_id: String?

    ) {
        /* val adminRequest = AdminRequest(1062,0,1,
             0,0, UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId,
             0,0
         )*/

        val adminRequest = AdminRequest(str_company_id!!.toInt(),str_entity_id!!.toInt(),filter_type,
            0,0, UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId,
            str_workGroup_id!!.toInt(), str_workLocation_id!!.toInt()
        )
        Log.e("commonRequest",adminRequest.toString())

        viewModel_admin.getAdminData(mContext!!,adminRequest


        )



    }


    @SuppressLint("ResourceAsColor")
    fun showSnackbar_Drawable_backroundcolor_with_more(
        context: Context,
        message: String?,
        drawable_icon: Int,
        bgColor: Int,
        more_details: String
    ) {
        try {
//            new CustomDialogClass(context, message).show();
            val alertDialog = AlertDialog.Builder(context)
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var alertDialogView: View? = null
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_, null)
                alertDialog.setView(alertDialogView)
                val textDialog = alertDialogView.findViewById<TextView>(R.id.diag_text)
                textDialog.text = message
                val diag_ok = alertDialogView.findViewById<TextView>(R.id.diag_ok)
                val txt_view_moredetails = alertDialogView.findViewById<TextView>(R.id.txt_view_moredetails)
                val icon = alertDialogView.findViewById<ImageView>(R.id.icon)
                txt_view_moredetails.visibility= View.VISIBLE
              //  icon.setImageResource(drawable_icon)

                val resourceName = context.resources.getResourceEntryName(drawable_icon)
                Log.e("resourceName", resourceName)
                if (resourceName.contains("caution")) {
                    icon.setImageResource(drawable_icon)
                }
                diag_ok.setBackgroundColor(bgColor)
                txt_view_moredetails.setBackgroundColor(resources.getColor(R.color.red))
                val show = alertDialog.show()
                show.setCancelable(false)
                val window = show.window
                if (window != null) {
                    window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    //TODO Dialog Size
                    show.window!!
                        .setLayout(
                            SnackBarUtil.getWidth(context) / 100 * 90,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    show.window!!.setGravity(Gravity.CENTER)
                }
                diag_ok.setOnClickListener {


                    show.dismiss() }

                txt_view_moredetails.setOnClickListener{
                    show.dismiss()

                    val viewMoreFragment = ViewMoreFragment()
                    val args_viewmore = Bundle()
                    args_viewmore.putString("view_more", more_details.toString())
                    viewMoreFragment.arguments = args_viewmore
                    replaceFragment(viewMoreFragment, R.id.flFragment, true)
                }
            }
            //            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }


    private fun addObserverAdmin() {
        viewModel_admin.adminResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {



                        (activity as MainActivity).hideProgressBar()
                        Log.e("admin",response.toString())
                        response.data?.let { managerEmpResponse ->
                            val requestResponse = managerEmpResponse
                            if (requestResponse != null && requestResponse.data!=null) {




                                if (requestResponse.data.isNotEmpty()) {
                                    arrayList_Employees.clear()

                                    employeeIdsString.clear()

                                    for (i in 0 until requestResponse.data.size) {
                                        if(binding.selectAllSwitch.isChecked) {

                                            employeeIdsString.append(requestResponse.data[i].employeeId)


                                            if (i < requestResponse.data.size - 1) {
                                                employeeIdsString.append(", ")
                                            }
                                        }

                                        Log.e("employeeIdsString",employeeIdsString.toString())

                                        if(UserShardPrefrences.getLanguage(mContext).equals("0")){
                                            arrayList_Employees.add(
                                                SpinnerItem(
                                                    requestResponse.data[i].employeeName,
                                                    requestResponse.data[i].employeeId,
                                                    requestResponse.data[i].employeeNo
                                                )
                                            )
                                        }
                                        else{
                                            arrayList_Employees.add(
                                                SpinnerItem(
                                                    requestResponse.data[i].employeeArabicName,
                                                    requestResponse.data[i].employeeId,
                                                    requestResponse.data[i].employeeNo


                                                )
                                            )
                                        }




                                    }
                                }




                                setUpDropDownEmployees(arrayList_Employees)










                            }
                            else{

                                arrayList_Employees.clear()
                                binding.spEmployees.adapter = null

                              //  setUpDropDownEmployees(arrayList_Employees)

                            }
                        }
                    }


                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.caution,resources.getColor(R.color.red))

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }
}
