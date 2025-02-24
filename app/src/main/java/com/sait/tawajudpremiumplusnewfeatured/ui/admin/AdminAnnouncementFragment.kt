package com.sait.tawajudpremiumplusnewfeatured.ui.admin


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.adapters.Spinner_Adapter
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentAdminAnnouncementsBinding
import com.sait.tawajudpremiumplusnewfeatured.items.SpinnerItem
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminAnnouncementsRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.AdminViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.announcements.AdminAnnouncementsViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.filter.AdminFilterViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.util.EnumUtils
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.util.*

class AdminAnnouncementFragment : BaseFragment(), View.OnClickListener,
    AdapterView.OnItemSelectedListener,
    CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {
    private lateinit var binding: FragmentAdminAnnouncementsBinding
    private var mContext: Context? = null
    private lateinit var viewModel: AdminAnnouncementsViewModel
    private lateinit var viewModel_adminFilter: AdminFilterViewModel
    private lateinit var viewModel_admin: AdminViewModel

    val arrayList_Company = ArrayList<SpinnerItem>()
    val arrayList_All = ArrayList<SpinnerItem>()
    val arrayList_Employees = ArrayList<SpinnerItem>()


    var str_company_id: String? = "0"
    var str_entity_id: String? = "0"
    var str_workLocation_id: String? = "0"
    var str_workGroup_id: String? = "0"
    var filter_type: Int = 0
    val employeeIdsString = StringBuilder()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminAnnouncementsBinding.inflate(inflater, container, false)
        mContext = inflater.context
        viewModel = ViewModelProvider(this)[AdminAnnouncementsViewModel::class.java]

        viewModel_adminFilter = ViewModelProvider(this)[AdminFilterViewModel::class.java]

        viewModel_admin = ViewModelProvider(this)[AdminViewModel::class.java]

        val activity = this.activity as MainActivity?

        setClickListeners(activity)
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        callCompanyAPI(0, 0)
        return binding.root
    }

    private fun callCompanyAPI(company_id: Int, filter_type: Int) {
        addObserver_Company()
        getCompanyDetails(company_id, filter_type)
    }

    private fun getCompanyDetails(company_id: Int, filter_type: Int) {

        val adminFilterRequest = AdminFilterRequest(company_id, filter_type)

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
                                    for (i in 0 until requestResponse.data.size) {

                                        if (requestResponse.data[i].companyName != null && requestResponse.data[i].companyName.isNotEmpty()) {
                                            filter_type = 0
                                            if (UserShardPrefrences.getLanguage(mContext)
                                                    .equals("0")
                                            ) {
                                                arrayList_Company.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].companyName,
                                                        requestResponse.data[i].companyId,
                                                        ""
                                                    )
                                                )
                                            } else {
                                                arrayList_Company.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].companyArabicName,
                                                        requestResponse.data[i].companyId,
                                                        ""


                                                    )
                                                )
                                            }
                                        } else if (requestResponse.data[i].entityName != null && requestResponse.data[i].entityName.isNotEmpty()) {
                                            filter_type = 1
                                            if (UserShardPrefrences.getLanguage(mContext)
                                                    .equals("0")
                                            ) {
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].entityName,
                                                        requestResponse.data[i].entityId,
                                                        ""
                                                    )
                                                )
                                            } else {
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].entityName,
                                                        requestResponse.data[i].entityId,
                                                        ""


                                                    )
                                                )
                                            }
                                        } else if (requestResponse.data[i].workLocationName != null && requestResponse.data[i].workLocationName.isNotEmpty()) {
                                            filter_type = 2
                                            if (UserShardPrefrences.getLanguage(mContext)
                                                    .equals("0")
                                            ) {
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].workLocationName,
                                                        requestResponse.data[i].workLocationId,
                                                        ""
                                                    )
                                                )
                                            } else {
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].workLocationName,
                                                        requestResponse.data[i].workLocationId,
                                                        ""


                                                    )
                                                )
                                            }
                                        } else if (requestResponse.data[i].groupName != null && requestResponse.data[i].groupName.isNotEmpty()) {
                                            filter_type = 3
                                            if (UserShardPrefrences.getLanguage(mContext)
                                                    .equals("0")
                                            ) {
                                                arrayList_All.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].groupName,
                                                        requestResponse.data[i].groupId,
                                                        ""
                                                    )
                                                )
                                            } else {
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



                                if (filter_type == 0) {
                                    setUpDropDown_Comapny(arrayList_Company)
                                } else if (filter_type == 1) {
                                    setUpDropDown(arrayList_All, binding.spAll)
                                } else if (filter_type == 2) {
                                    setUpDropDown(arrayList_All, binding.spAll)
                                } else if (filter_type == 3) {
                                    setUpDropDown(arrayList_All, binding.spAll)
                                }


                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
                                message,
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )

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

        // str_company_id = arrayList_Company[0].emp_id.toString()

    }

    private fun setClickListeners(activity: MainActivity?) {
        binding.selectAllSwitch.setOnCheckedChangeListener(this)

        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.isForPeriodSwitch.setOnCheckedChangeListener(this)
        binding.txtToDate.setOnClickListener(this)
        binding.txtFromDate.setOnClickListener(this)
        binding.txtCancel.setOnClickListener(this)
        binding.txtSave.setOnClickListener(this)


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
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.fragment_title_emp_anouncemnt))

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()

            R.id.txt_save -> {
                checkvalidation()
            }
            R.id.txt_cancel -> (activity as MainActivity).onBackPressed()


            R.id.txt_from_date -> {

                datePicker(binding.txtFromDate.text.toString(), binding.txtFromDate)

            }
            R.id.txt_to_date -> {

                datePicker(binding.txtToDate.text.toString(), binding.txtToDate)


            }


        }
    }

    private fun checkvalidation() {
        binding.txtToDate.error = null
        binding.txtFromDate.error = null
        binding.txtDate.error = null

        val fromDate: String = binding.txtFromDate.text.toString()
        val toDate: String = binding.txtToDate.text.toString()

        var cancel = false
        var focusView: View? = null
        if (binding.isForPeriodSwitch.isChecked) {
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


        } else {
            if (TextUtils.isEmpty(fromDate)) {
                binding.txtFromDate.error = getString(R.string.error_field_required)
                focusView = binding.txtFromDate
                cancel = true
            }
        }


        cancel = false


        if (cancel) {
            focusView!!.requestFocus()
            if (str_company_id == "0" || str_company_id == null) {
                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                    context,
                    resources.getString(R.string.select_company),
                    R.drawable.caution,
                    resources.getColor(R.color.red)
                )
            }

        } else {
            try {
                if (EnumUtils.isNetworkConnected(context)) {
                    if (str_company_id == "0" || str_company_id == null) {
                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                            context,
                            resources.getString(R.string.select_company),
                            R.drawable.caution,
                            resources.getColor(R.color.red)
                        )
                    } else {
                        callAdminAnnouncementsAPI()
                    }
                } else {
                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                        context,
                        getString(R.string.no_internet_connection),
                        R.drawable.caution,
                        resources.getColor(R.color.red)
                    )
                }
            } catch (e: Exception) {
            }

        }

    }

    private fun callAdminAnnouncementsAPI() {
        addObserver()
        postAdminAnnouncements()
    }

    private fun postAdminAnnouncements() {
        val adminAnnouncementsRequest = AdminAnnouncementsRequest(
            0,
            true,
            false,
            binding.txtFromDate.text.toString(),
            binding.txtFromDate.text.toString(),
            binding.txtFromDate.text.toString(),
            binding.edtTitle.text.toString(),
            binding.edtTitle.text.toString(),
            binding.edtContent.text.toString(),
            binding.edtContent.text.toString(),
            binding.txtFromDate.text.toString(),
            str_company_id!!.toInt(),
            UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId,
            str_entity_id!!.toInt(),
            str_workGroup_id!!.toInt(),
            str_workLocation_id!!.toInt(),
            UserShardPrefrences.getLanguage(mContext)!!.toInt()

        )


        /*    val dashboardRequest = DashboardRequest(
                0, 0, 0, "2022-02-01T06:14:59.084Z",
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                0, "2024-02-01T06:14:59.084Z",0, 23
            )*/

        viewModel.getAdminAnnouncementsData(
            mContext!!,
            adminAnnouncementsRequest
        )
    }

    private fun addObserver() {
        viewModel.adminAnnouncementsResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        (activity as MainActivity).hideProgressBar()
                        // binding.progressOverlay.visibility = View.GONE

                        response.data?.let { adminAnnouncementsResponse ->
                            val adminAnnouncementsResponse = adminAnnouncementsResponse
                            if (adminAnnouncementsResponse != null) {


                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,
                                    adminAnnouncementsResponse.data.success,
                                    R.drawable.app_icon,
                                    colorPrimary,
                                    SnackBarUtil.OnClickListenerNew {

                                        (activity as MainActivity).onBackPressed()

                                    })


                            }
                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        // binding.progressOverlay.visibility = View.GONE
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
                                message,
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )

                            //  SnackBarUtil.showSnackbar(context, message, false)

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                    // binding.progressOverlay.visibility=View.VISIBLE                    }
                }
            }
        }
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

    private fun pickDate(day: Int, month: Int, year: Int, textView: TextView) {
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"AdminAnnouncementFragment")
        datePickerFragment.show(parentFragmentManager, "datePicker")
       /*
     val now = Calendar.getInstance()
       // now[year, month - 1] = day
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
        dpd.locale = Locale.ENGLISH
        dpd.setOkText("OK")
        dpd.setCancelText("Cancel")
        dpd.version = DatePickerDialog.Version.VERSION_2
        *//*dpd.setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialogTheme)
        dpd.setStyle(DialogFragment.STYLE_NORMAL, R.style.PickerDialogStyle_LTR_Transparent)*//*
        val newFragment = DatePickerFragment()
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")*/

        //dpd.show(requireActivity().supportFragmentManager, "Datepickerdialog")
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
                    binding.txtDate.text = mContext!!.resources.getString(R.string.from_date_txt)
                    binding.txtFromDate.hint=mContext!!.resources.getString(R.string.from_date_txt)



                } else {
                    binding.txtDate.text = mContext!!.resources.getString(R.string.date)
                    binding.txtFromDate.hint=mContext!!.resources.getString(R.string.date)
                    binding.lFromDate.visibility = View.VISIBLE
                    binding.lToDate.visibility = View.GONE
                }
            }



        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_entity -> {
                filter_type = 1
                callCompanyAPI(str_company_id!!.toInt(),1)
            }
            R.id.rb_work_locations -> {
                filter_type = 2

                callCompanyAPI(str_company_id!!.toInt(),2)
            }
            R.id.rb_work_group -> {
                filter_type = 3

                callCompanyAPI(str_company_id!!.toInt(),3)
            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


        when (parent?.id) {

            R.id.sp_company -> {



                if (position==0){

                }
                else{
                    str_company_id = arrayList_Company[position-1].emp_id.toString()

                    str_company_id = arrayList_Company[position-1].emp_id.toString()
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

            }

            R.id.sp_all -> {

                if (position==0){

                }
                else {

                    if (filter_type == 1) {
                        str_entity_id = arrayList_All[position-1].emp_id.toString()
                        str_workLocation_id = "0"
                        str_workGroup_id = "0"
                        callAdminAPI(
                            filter_type,
                            str_entity_id,
                            str_workGroup_id,
                            str_workLocation_id
                        )

                    } else if (filter_type == 2) {
                        str_workLocation_id = arrayList_All[position-1].emp_id.toString()
                        str_entity_id = "0"
                        str_workGroup_id = "0"
                        callAdminAPI(
                            filter_type,
                            str_entity_id,
                            str_workGroup_id,
                            str_workLocation_id
                        )

                    } else if (filter_type == 3) {
                        str_workGroup_id = arrayList_All[position-1].emp_id.toString()
                        str_entity_id = "0"
                        str_workLocation_id = "0"
                        callAdminAPI(
                            filter_type,
                            str_entity_id,
                            str_workGroup_id,
                            str_workLocation_id
                        )

                    }
                }
            }

            R.id.sp_employees -> {

                if (position==0){

                }
                else {

                    if (!binding.selectAllSwitch.isChecked) {
                        if (arrayList_Employees.size > 0) {
                            employeeIdsString.clear()
                            employeeIdsString.append(arrayList_Employees[position-1].emp_id.toString())
                        }

                    }
                }

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
    override fun onNothingSelected(parent: AdapterView<*>?) {
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

    @SuppressLint("SuspiciousIndentation")
    private fun setUpDropDownEmployees(empList: ArrayList<SpinnerItem>) {
        val arrayList = java.util.ArrayList<String>()
        arrayList.add(resources.getString(R.string.select))
        for (i in empList.indices) {
            // arrayList.add(empList[i].emp_id.toString()+"  "+empList[i].title)
            arrayList.add(empList[i].title)
        }

        val customAdapter = Spinner_Adapter(context, arrayList)

        //  val  spinner_Custom_Adapter = Spinner_Custom_Adapter(context, arrayList,this)
        binding.spEmployees.adapter = customAdapter
        binding.spEmployees.setSelection(0)

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


                                    for (i in 0 until requestResponse.data.size) {

                                        if(binding.selectAllSwitch.isChecked) {
                                            employeeIdsString.append(requestResponse.data[i].employeeId)
                                            if (i < requestResponse.data.size - 1) {
                                                employeeIdsString.append(", ")
                                            }
                                        }

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
