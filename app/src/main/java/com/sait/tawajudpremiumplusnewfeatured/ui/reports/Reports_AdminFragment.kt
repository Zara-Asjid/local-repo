package com.sait.tawajudpremiumplusnewfeatured.ui.reports

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.adapters.Spinner_Adapter
import com.sait.tawajudpremiumplusnewfeatured.adapters.Spinner_Custom_Adapter
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentAdminReportsBinding


import com.sait.tawajudpremiumplusnewfeatured.items.SpinnerItem
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.AdminViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.filter.AdminFilterViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.ManagerEmpRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.viewmodels.Manager_empViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.PdfGenaratorFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.adapter.SelfReportAdapter
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.listener.ReportSelfItemClickListener
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.models.Reports_Self_Service_Request
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.models.Reports_Self_Sevice_Data
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.viewmodels.ReportsViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.util.*
import kotlin.collections.ArrayList

class Reports_AdminFragment : BaseFragment(), View.OnClickListener,
    ReportSelfItemClickListener,
    Spinner_Custom_Adapter.OnFilterCompleteListener,
    AdapterView.OnItemSelectedListener{
    var handler: Handler = Handler()

    private lateinit var binding: FragmentAdminReportsBinding
    private lateinit var viewModel: ReportsViewModel
    private var mContext: Context? = null
    private lateinit var arrListEmpReports: ArrayList<Reports_Self_Sevice_Data>
    private var violationAdapter: SelfReportAdapter? = null
    private var fromRefresh = false
    private lateinit var viewModel_ManagerEmp: Manager_empViewModel
    private var spinner_Custom_Adapter: Spinner_Custom_Adapter? = null
    private lateinit var viewModel_admin: AdminViewModel
    private lateinit var viewModel_adminFilter: AdminFilterViewModel

    var str_company_id: String? = "0"
    var filter_type:Int=0
    val arrayList_companyIds = arrayListOf<SpinnerItem>()

    val arrayList_type = arrayListOf<SpinnerItem>()
    var str_emp_id : String? = "0"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminReportsBinding.inflate(inflater, container, false)
        mContext = inflater.context

        viewModel = ViewModelProvider(this)[ReportsViewModel::class.java]
        viewModel_ManagerEmp = ViewModelProvider(this)[Manager_empViewModel::class.java]
        viewModel_admin = ViewModelProvider(this)[AdminViewModel::class.java]
        viewModel_adminFilter = ViewModelProvider(this)[AdminFilterViewModel::class.java]


        val activity = this.activity as MainActivity?
    //    str_emp_id = UserShardPrefrences.getUserInfo(mContext).fKEmployeeId.toString()
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        setClickListeners(activity)
        init()
        setDate()

      //  callManagerUnderEmpDetailsAPI()

        callCompanyAPI(0,0)


        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Filter data based on search query
               try {
                   if (s!!.isNotEmpty() && arrayList_companyIds.isNotEmpty()){
                       spinner_Custom_Adapter!!.filter.filter(s)
                   }else{
                       Log.e("empty check", "afterTextChanged: $s" )
                   }
               }
               catch (exception:Exception){
                   Log.e("empty check", "afterTextChanged: "+ exception )

               }
                //  processFilteredEmployeeNo()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return binding.root
    }

    private fun callCompanyAPI(company_id: Int, filter_type: Int) {
        addObserver_Company()
        getCompanyDetails(company_id,filter_type)
    }

    private fun getCompanyDetails(companyId: Int, filterType: Int) {

        val adminFilterRequest = AdminFilterRequest(companyId,filterType)

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

                                    arrayList_companyIds.clear()
                                    for (i in 0 until requestResponse.data.size) {

                                        if(requestResponse.data[i].companyName!=null && requestResponse.data[i].companyName.isNotEmpty()){
                                            filter_type = 0
                                            if(UserShardPrefrences.getLanguage(mContext).equals("0")){
                                                arrayList_companyIds.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].companyName,
                                                        requestResponse.data[i].companyId,
                                                        ""
                                                    )
                                                )
                                            }
                                            else{
                                                arrayList_companyIds.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].companyArabicName,
                                                        requestResponse.data[i].companyId,
                                                        ""


                                                    )
                                                )
                                            }
                                        }

                                    }
                                }



                                if(filter_type==0){
                                    setUpDropDown_Comapny(arrayList_companyIds)
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
    @SuppressLint("SuspiciousIndentation")
    private fun setUpDropDown_Comapny(empList: java.util.ArrayList<SpinnerItem>) {
        val arrayList = java.util.ArrayList<String>()
        arrayList.add(resources.getString(R.string.select_company))
        for (i in empList.indices) {
            arrayList.add(empList[i].title)
        }

        val customAdapter = Spinner_Adapter(context, arrayList)

        //  val  spinner_Custom_Adapter = Spinner_Custom_Adapter(context, arrayList,this)
        binding.spCompanyRp.adapter = customAdapter
        binding.spCompanyRp.setSelection(0)

        //   str_company_id = arrayList_Company[0].emp_id.toString()

    }
    private fun callManagerUnderEmpDetailsAPI() {

        addObserverRequestType()
        getEmpDetails()

    }
    private fun getAdminEmployeeDetails(company_id: String) {
        val adminRequest = AdminRequest(company_id.toInt(),0,1,
            0,0, UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId,0,0
        )
        Log.e("commonRequest",adminRequest.toString())

        viewModel_admin.getAdminData(mContext!!,adminRequest


        )



    }

    private fun callAdminAPI(company_id: String) {
        addObserverAdmin()
        getAdminEmployeeDetails(company_id)
    }

    private fun addObserverAdmin() {
        viewModel_admin.adminResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        binding.rvSwipeRefresh.isRefreshing = false
                        fromRefresh = false
                        binding.rvSwipeRefresh.visibility = View.VISIBLE


                        (activity as MainActivity).hideProgressBar()
                        Log.e("admin",response.toString())
                        response.data?.let { managerEmpResponse ->
                            val requestResponse = managerEmpResponse
                            if (requestResponse != null && requestResponse.data!=null) {


                                    if (requestResponse.data.isNotEmpty()) {
                                        arrayList_type.clear()




                                        for (i in 0 until requestResponse.data.size) {

                                            if(UserShardPrefrences.getLanguage(mContext).equals("0")){
                                                arrayList_type.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].employeeName,
                                                        requestResponse.data[i].employeeId,
                                                        requestResponse.data[i].employeeNo
                                                    )
                                                )
                                            }
                                            else{
                                                arrayList_type.add(
                                                    SpinnerItem(
                                                        requestResponse.data[i].employeeArabicName,
                                                        requestResponse.data[i].employeeId,
                                                        requestResponse.data[i].employeeNo


                                                    )
                                                )
                                            }




                                        }
                                    }


                                    setUpDropDown_RequestType(arrayList_type)










                            } else{

                                arrayList_type.clear()
                                binding.spSelectEmployee.adapter = null

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

    private fun getEmpDetails() {


        val commonRequest = ManagerEmpRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )


        viewModel_ManagerEmp.getManager_empData(
            mContext!!, commonRequest
        )


    }

    private fun addObserverRequestType() {
        viewModel_ManagerEmp.manager_empResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse
                            //  val arrayList_type = ArrayList<SpinnerItem_BO_Type>()

                            if (requestResponse.data != null) {


                                //   SnackBarUtil.showSnackbar(mContext, requestResponse.data.toString())




                                if (requestResponse.data.isNotEmpty()) {
                                    arrayList_type.clear()




                                    for (i in 0 until requestResponse.data.size) {

                                        if(UserShardPrefrences.getLanguage(mContext).equals("0")){
                                            arrayList_type.add(
                                                SpinnerItem(
                                                    requestResponse.data[i].employeeName,
                                                    requestResponse.data[i].fK_EmployeeId,
                                                    requestResponse.data[i].employeeNo
                                                )
                                            )
                                        }
                                        else{
                                            arrayList_type.add(
                                                SpinnerItem(
                                                    requestResponse.data[i].employeeArabicName,
                                                    requestResponse.data[i].fK_EmployeeId,
                                                    requestResponse.data[i].employeeNo


                                                )
                                            )
                                        }




                                    }
                                }


                                setUpDropDown_RequestType(arrayList_type)


                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.caution,resources.getColor(R.color.red))

                           // SnackBarUtil.showSnackbar(context, message, false)

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }


    private fun setUpDropDown_RequestType(empList: java.util.ArrayList<SpinnerItem>) {
        val arrayList = java.util.ArrayList<String>()
        arrayList.add(mContext!!.resources.getString(R.string.select_all))
        for (i in empList.indices) {
            arrayList.add(empList[i].emp_no+"  "+empList[i].title)

        }
        spinner_Custom_Adapter = Spinner_Custom_Adapter(context, arrayList,this)
        binding.spSelectEmployee.adapter = spinner_Custom_Adapter
        binding.spSelectEmployee.setSelection(0)



    }

    private fun init() {
        initComponents()
        callManagerReportAPI(str_emp_id!!)
        setAdapter()
        addSwipeToRefresh()
    }

    private fun callManagerReportAPI(str_emp_id: String) {

        addObserver()
        getManagerReports(str_emp_id)
    }

    private fun addSwipeToRefresh() {

        binding.rvSwipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            fromRefresh = true
            callManagerReportAPI(str_emp_id!!)
        })

    }

    private fun setAdapter() {

        binding.rvSelfReports.layoutManager = LinearLayoutManager(mContext)

        violationAdapter = SelfReportAdapter(arrListEmpReports, this, mContext)

        binding.rvSelfReports.adapter = violationAdapter


    }

    private fun getManagerReports(str_emp_id: String) {

        if(str_emp_id.equals("0")){
            val Self_ReportRequest = Reports_Self_Service_Request(
                UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
                // 23,
                UserShardPrefrences.getLanguage(mContext).toString(),
                0
                // str_emp_id.toInt()
            )
            viewModel.getReports_Self_ServiceData(
                mContext!!,
                Self_ReportRequest
            )
        }
        else{
            val Self_ReportRequest = Reports_Self_Service_Request(
                //  str_emp_id.toInt(),
                UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
                UserShardPrefrences.getLanguage(mContext).toString(),
                //UserShardPrefrences.getUserInfo(mContext).fKEmployeeId
                0
            )
            viewModel.getReports_Self_ServiceData(
                mContext!!,
                Self_ReportRequest
            )
        }



    }
    private fun initComponents() {
        arrListEmpReports = arrayListOf()
        binding.companyIdSpinner.visibility=View.VISIBLE


    }

    private fun addObserver() {

        viewModel.reports_Self_ServiceResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.rvSwipeRefresh.isRefreshing = false
                        fromRefresh = false
                        binding.rvSwipeRefresh.visibility = View.VISIBLE

                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { selfReportResponse ->
                            val selfReportResponse = selfReportResponse
                            if (selfReportResponse != null) {
                                arrListEmpReports.clear()

                                if(selfReportResponse.message!!.contains(mContext!!.resources.getString(R.string.no_record_found_txt))){
                                    binding.txtNoRecord.visibility= View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.GONE
                                }
                                else{




                                    for (i in 0 until selfReportResponse.data.size) {
                                        if(selfReportResponse.data[i].desc_En.contains(mContext!!.resources.getString(R.string.self_service))
                                            ||selfReportResponse.data[i].desc_Ar.contains("الخدمات الذاتية")){
                                            arrListEmpReports.addAll(listOf(selfReportResponse.data[i]))
                                        }
                                    }


                                    // arrListSelfReport.addAll(selfReportResponse.data)
                                    binding.rvSelfReports.adapter?.notifyDataSetChanged()

                                    binding.txtNoRecord.visibility= View.GONE
                                    binding.rvSwipeRefresh.visibility = View.VISIBLE
                                }





                            }
                        }

                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                          //  SnackBarUtil.showSnackbar(context, message, false)
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

    private fun setDate() {
//        String currentDate = DateTimeOp.getCurrentDateTime("yyyy-MM-dd");
        val finalDateMonth: String = DateTime_Op.getFinalDateMonth("yyyy-MM-dd")

//        String previousMonthDate = DateTimeOp.getPreviousMonthDateTime("yyyy-MM-dd");
        val initialDateMonth: String = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")
        binding.txtFromDateAdmr.setText(initialDateMonth)
        binding.txtToDateAdmr.setText(finalDateMonth)

    }

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.rlToDateAdmr.setOnClickListener(this)
        binding.rlFromDateAdmr.setOnClickListener(this)
        binding.ivSearchIcon.setOnClickListener(this)
        binding.spCompanyRp.onItemSelectedListener=this
        binding.spSelectEmployee.onItemSelectedListener = this

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
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.reports_txt))



        if (GlobalVariables.from_background) {
            handler.post(checkInternetRunnable)
        }

    }
    private val checkInternetRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun run() {
            val application = requireActivity().application as TawajudApplication

            if (application.hasInternetConnection()) {
                onRefresh()
            } else {
                // Repeat the check after 1 second if there is no internet connection
                handler.postDelayed(this, 1000)
            }
        }
    }
    private fun onRefresh() {
        callCompanyAPI(0,0)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()


            R.id.rl_to_date_admr -> {
                datePicker(binding.txtToDateAdmr.text.toString(), binding.txtToDateAdmr)
            }


            R.id.rl_from_date_admr -> {
                datePicker(binding.txtFromDateAdmr.text.toString(), binding.txtFromDateAdmr)

            }
            R.id.iv_search_icon -> {

                if(DateTime_Op.validateDates(binding.txtFromDateAdmr.text.toString(),binding.txtToDateAdmr.text.toString())) {
                    callManagerReportAPI(str_emp_id!!)
                }
                else{
                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,mContext!!.resources.getString(R.string.date_validation),R.drawable.caution,resources.getColor(R.color.red))

                   // SnackBarUtil.showSnackbar(mContext,"Please make sure from date is less than or equal to to date.")
                }



            }
        }

    }


    private fun datePicker(date: String, txtDate: TextView) {
        try {
            // val todate: String = binding.txtToDate.getText().toString()
            if (TextUtils.isEmpty(date)) {
                val c = Calendar.getInstance()
                val mDay = c[Calendar.DAY_OF_MONTH]
                val mMonth = c[Calendar.MONTH]
                val mYear = c[Calendar.YEAR]
                pickDate(mDay, mMonth, mYear, txtDate)
            } else {
                val dateString = date.split("-").toTypedArray()
                pickDate(
                    dateString[2].toInt(),
                    dateString[1].toInt(),
                    dateString[0].toInt(),
                    txtDate
                )
            }
        } catch (e: Exception) {
            e.message
        }

    }

    private fun pickDate(day: Int, month: Int, year: Int, textView: TextView) {
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"Reports_AdminFragment")
        datePickerFragment.show(parentFragmentManager, "datePicker")
    /*    val now = Calendar.getInstance()
        now[year, month - 1] = day
        val dpd = DatePickerDialog.newInstance(
            { view, year, monthOfYear, dayOfMonth ->
                var monthOfYear = monthOfYear
                try {
                    val yearNew = year
                    monthOfYear += 1
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
            },
            now[Calendar.YEAR],
            now[Calendar.MONTH],
            now[Calendar.DAY_OF_MONTH]
        )
        dpd.locale= Locale.ENGLISH
        dpd.setOkText("OK")
        dpd.setCancelText("Cancel")
        dpd.version = DatePickerDialog.Version.VERSION_2
        dpd.show(requireActivity().supportFragmentManager, "Datepickerdialog")
*/
    }

    override fun onReportItemClick( position: Int) {
        val targetFragment = PdfGenaratorFragment()
        val args_profile = Bundle()
        args_profile.putString("form_id", arrListEmpReports[position].formID.toString())
        args_profile.putString("from_date", binding.txtFromDateAdmr.text.toString())
        args_profile.putString("to_date", binding.txtToDateAdmr.text.toString())
        args_profile.putString("manager_id",
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId.toString()
        )
        args_profile.putString("emp_id",str_emp_id)
        args_profile.putBoolean("isFromAdmin",true)
        args_profile.putInt("selected_tab",2)


        targetFragment.arguments = args_profile
        replaceFragment(targetFragment, R.id.flFragment, false)
        //SnackBarUtil.showSnackbar(context, "Item is clicked", false)
        /*
                when (position) {
                    R.id.download_pdf_btn -> {
                        val targetFragment = HelpFragment()
                        val args_profile = Bundle()
                        args_profile.putString("form_id", arrListSelfReport[position].formID.toString())
                        targetFragment.arguments = args_profile
                        replaceFragment(targetFragment, R.id.flFragment, true)

                    }

                }
        */

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


        when (parent?.id) {
            R.id.sp_select_employee -> {

                if (position == 0) {

                    callManagerReportAPI("0")

                } else {
                    // toast(arrayList_type[position-1].title)
                    str_emp_id = arrayList_type[position - 1].emp_id.toString()
                    callManagerReportAPI(str_emp_id!!)

                }

            }
            R.id.sp_company_rp -> {
                if (position == 0) {

                } else {
                    str_company_id = arrayList_companyIds[position - 1].emp_id.toString()
                    callAdminAPI(str_company_id!!)

                }

            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onFilterComplete(filteredList: java.util.ArrayList<String>?) {




        /* if (filteredList != null && filteredList.size == 1) {
             val filteredName = filteredList[0] // Assuming you want to get the position of the first item in the filtered list
             val position = arrayList_type.indexOfFirst { it.title == filteredName } // Assuming 'title' is the property of SpinnerItem containing the name
             if (position != -1) {
                 // Position found, do something with it
                 toast("Position of $filteredName is $position")
             } else {
                 // Position not found
                 toast("Name $filteredName not found in the array")
             }
         }*/

        if(filteredList!!.size==1){
            val filteredName = filteredList[0]
            val position = arrayList_type.indexOfFirst { it.emp_no+"  "+it.title == filteredName }

            //empList[i].emp_no+"  "+empList[i].title
            // toast("$position:$filteredList   "+arrayList_type.indexOfFirst{it.emp_no+"  "+it.title == filteredName})

            //  toast(arrayList_type[position].emp_id.toString()
            if (position>=0) {
                str_emp_id = arrayList_type[position].emp_id.toString()
            }

        }

    }

}

