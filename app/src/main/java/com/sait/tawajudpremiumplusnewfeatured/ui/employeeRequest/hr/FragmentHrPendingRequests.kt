package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.adapters.Spinner_Adapter
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentPendingRequestBinding
import com.sait.tawajudpremiumplusnewfeatured.items.SpinnerItem_BO_Type
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.adapters.Pending_Adapter_Hr
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Leave.Approve.ApprovesViewModel_HR
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.listener.PendingLeaveApprovalListener_Manager
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingForManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Reject.RejectLeaveByManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.pending_HR_leaves_data
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingHRManualEntryData
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickReject.onClickRejectManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingHRPermissionData
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingPermissonRequest

import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Leave.Pending.PendingHRLeavesViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Leave.Reject.RejectViewModel_HR
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.ManualEntry.PendingHRManualEntryViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions.Approve.ApprovesPermissionViewModel_HR
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions.PendingHRPermissionViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.Approve.onClickApproveManualEntryHRViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Permissions.ApprovedList.ApprovePermissionViewModel

import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.requestType.RequestTypeData
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.requestType.RequestTypeViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.EnumUtils
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.util.*


import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.Reject.onClickRejectManualEntryHRViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.onClickApprove.onClickApproveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve_HRLeavesRequest
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection


class FragmentHrPendingRequests : BaseFragment(), View.OnClickListener, PendingLeaveApprovalListener_Manager,
    AdapterView.OnItemSelectedListener {

    var handler: Handler = Handler()

    private lateinit var binding: FragmentPendingRequestBinding
    private lateinit var viewModel: PendingHRLeavesViewModel
    var spinnerItemBO_requsettype: SpinnerItem_BO_Type? = null
    val arrayList_type = ArrayList<SpinnerItem_BO_Type>()


    private lateinit var approve_leavesViewModel: ApprovesViewModel_HR

    private lateinit var rejectviewModel: RejectViewModel_HR
    private lateinit var onclick_approv_permission_viewModel: ApprovesPermissionViewModel_HR
    private lateinit var onclick_approv_manual_entry_viewModel: onClickApproveManualEntryHRViewModel
    private lateinit var onclick_reject_manual_entry_viewModel: onClickRejectManualEntryHRViewModel

    private lateinit var pendingManualEntryviewModel: PendingHRManualEntryViewModel
    private lateinit var approvePermissionviewModel: ApprovePermissionViewModel
    private lateinit var viewModel_RequestType: RequestTypeViewModel

    private lateinit var pendingPermissionViewModel: PendingHRPermissionViewModel

    private var mContext: Context? = null

    private lateinit var pendingleaveslist: ArrayList<pending_HR_leaves_data>
    private lateinit var pendingPermissionlist: ArrayList<PendingHRPermissionData>
    private lateinit var pendindManualEntrylist: ArrayList<PendingHRManualEntryData>
    var selectedPosition: Int = 0
    private var pendingAdapter: Pending_Adapter_Hr? = null
    private var fromRefresh = false
    var sp_list: Spinner? = null
    var btn_click_status = true
    var status_id: Int = 0


    var check_tab = 0
    var check_leaves = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPendingRequestBinding.inflate(inflater, container, false)
        mContext = inflater.context
        sp_list = binding.spList
        viewModel_RequestType = ViewModelProvider(this)[RequestTypeViewModel::class.java]
        viewModel = ViewModelProvider(this)[PendingHRLeavesViewModel::class.java]
        approvePermissionviewModel =
            ViewModelProvider(this)[ApprovePermissionViewModel::class.java]
        approve_leavesViewModel = ViewModelProvider(this)[ApprovesViewModel_HR::class.java]
        rejectviewModel = ViewModelProvider(this)[RejectViewModel_HR::class.java]
        onclick_approv_permission_viewModel= ViewModelProvider(this)[ApprovesPermissionViewModel_HR::class.java]
        onclick_approv_manual_entry_viewModel=ViewModelProvider(this)[onClickApproveManualEntryHRViewModel::class.java]
        onclick_reject_manual_entry_viewModel=ViewModelProvider(this)[onClickRejectManualEntryHRViewModel::class.java]

        pendingManualEntryviewModel = ViewModelProvider(this)[PendingHRManualEntryViewModel::class.java]
        pendingPermissionViewModel = ViewModelProvider(this)[PendingHRPermissionViewModel::class.java]

        val activity = this.activity as MainActivity?



        setClickListeners(activity)
        init()
        setDate()

        callRequestTypeAPI()
        return binding.root
    }

    private fun init() {
        initComponents()
        //  setUpDropDown()
        setAdapter()
        addSwipeToRefresh()

    }



    private fun callRequestTypeAPI() {

        addObserverRequestType()
        getRequestTypeDetails()

    }



    private fun addObserverRequestType() {
        viewModel_RequestType.requestTypeResponse.observe(viewLifecycleOwner) { event ->
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
                                    val permissionTypes: List<RequestTypeData> =
                                        requestResponse.data

                                    for (i in 0 until requestResponse.data.size) {
                                        val permissionType: RequestTypeData = permissionTypes[i]

                                        if(!permissionType.desc_En.contains("Enrollment")) {
                                            if (UserShardPrefrences.getLanguage(mContext)
                                                    .equals("1")
                                            ) {
                                                arrayList_type.add(
                                                    SpinnerItem_BO_Type(
                                                        permissionType.formID,
                                                        permissionType.desc_Ar,
                                                        permissionType.desc_Ar,
                                                        permissionType.requestType
                                                    )
                                                )

                                            } else {
                                                arrayList_type.add(
                                                    SpinnerItem_BO_Type(
                                                        permissionType.formID,
                                                        permissionType.desc_En,
                                                        permissionType.desc_En,
                                                        permissionType.requestType
                                                    )
                                                )

                                            }
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
                            //    SnackBarUtil.showSnackbar(context, message, false)

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }



    private fun setUpDropDown_RequestType(empList: ArrayList<SpinnerItem_BO_Type>) {
        val arrayList = ArrayList<String>()
        //arrayList.add("Please select")
        for (i in empList.indices) {

            arrayList.add(empList[i].desc_en)

        }
        val customAdapter = Spinner_Adapter(context, arrayList)
        binding.spList.adapter = customAdapter

        sp_list?.setSelection(0)


    }

    private fun getRequestTypeDetails() {


        val commonRequest = CommonRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            //172,
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )


        viewModel_RequestType.getRequestTypeData(
            mContext!!, commonRequest
        )


    }


    private fun setUpDropDown() {
        val arrayList = ArrayList<String>()
        arrayList.add("" + requireContext().resources.getString(R.string.h_Leaves))
        arrayList.add("" + requireContext().resources.getString(R.string.h_Permissions))
        arrayList.add("" + requireContext().resources.getString(R.string.manual_entry_request))
        //   arrayList.add("" + requireContext().resources.getString(R.string.over_time_request))
        val customAdapter = Spinner_Adapter(context, arrayList)
        sp_list?.setAdapter(customAdapter)
        sp_list?.setSelection(0)
        arrayList_type.clear()
        // arrayList_type= arrayList


    }



    private fun callPendingLeavesAPI(status_id: Int) {

        addObserverForPendingLeaves()
        getPendingLeavesDetails(status_id)
    }

    private fun addSwipeToRefresh() {

        /*
                binding.rvSwipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                    fromRefresh = true
                    if (pendingleaveslist.size==0||pendindPermissionlist.size==0||pendindManualEntrylist.size==0){
                        binding.txtNoRecord.visibility = View.VISIBLE
                        binding.rvSwipeRefresh.visibility = View.GONE
                        binding.recyclerView.visibility=View.GONE
                    }
                    if (pendingleaveslist.size != 0 && pendindPermissionlist.size == 0 && pendindManualEntrylist.size == 0) {
                        callPendingLeavesAPI(0)
                    }
                    if (pendingleaveslist.size == 0 && pendindPermissionlist.size != 0 && pendindManualEntrylist.size == 0) {
                        callAllPendingPermissionsAPI()
                    }
                    if (pendingleaveslist.size == 0 && pendindPermissionlist.size == 0 && pendindManualEntrylist.size != 0) {
                        callAllPendingManualEntryRequestAPI()
                    }

                })
        */

    }

    private fun setAdapter() {
        // if (pendingAdapter == null || binding.recyclerView.adapter == null) {
        binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        pendingAdapter = Pending_Adapter_Hr(
            pendingleaveslist,
            pendingPermissionlist,
            pendindManualEntrylist,
            this,
            mContext!!
        )
        binding.recyclerView.adapter = pendingAdapter

        //}

    }

    private fun callAllPendingPermissionsAPI() {
        addObserverPendingPermission()
        getallpendingPermissions()
    }



    private fun callAllPendingManualEntryRequestAPI() {
        addObserverForPendingManualEntryRequest()
        getallPendingManualEntry()
    }

    private fun getallPendingManualEntry() {
        val pendingmanualEntryRequest = PendingManualEntryRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            0,
            binding.txtFromDatePendReq.text.toString(),
            binding.txtToDatePendReq.text.toString()
        )
        pendingManualEntryviewModel.getPendingManualEntryStatus(
            mContext!!,
            pendingmanualEntryRequest
        )
    }

    private fun addObserverForPendingManualEntryRequest() {
        pendingManualEntryviewModel.pending_manual_entry_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        /*  binding.rvSwipeRefresh.isRefreshing = false
                          fromRefresh = false
                          binding.rvSwipeRefresh.visibility = View.VISIBLE
  */
                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { pendingManualEntryResponse ->
                            val pendingManualEntryResponse = pendingManualEntryResponse
                            if (pendingManualEntryResponse != null) {
                                if(pendingManualEntryResponse.message!!.contains(mContext!!.resources.getString(R.string.no_record_found_txt))){
                                    //  if (pendingManualEntryResponse.message!!.contains(mContext!!.resources.getString(R.string.response_no_record_found_txt))) {
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    //   binding.rvSwipeRefresh.visibility = View.GONE

                                    pendingPermissionlist.clear()
                                    pendingleaveslist.clear()
                                    pendindManualEntrylist.clear()
                                    binding.recyclerView.adapter?.notifyDataSetChanged()
                                }
                                else
                                {
                                    if (pendingManualEntryResponse.data !=null&&pendingManualEntryResponse.data.isNotEmpty()) {
                                        binding.txtNoRecord.visibility = View.GONE
                                        //    binding.rvSwipeRefresh.visibility = View.VISIBLE
                                        pendindManualEntrylist.clear()
                                        pendingPermissionlist.clear()
                                        pendingleaveslist.clear()
                                        for (i in 0 until pendingManualEntryResponse.data.size) {
                                            pendindManualEntrylist.addAll(
                                                arrayListOf(
                                                    pendingManualEntryResponse.data[i]
                                                )
                                            )
                                        }


                                        binding.recyclerView.adapter?.notifyDataSetChanged()
                                        binding.txtNoRecord.visibility = View.GONE
                                        // binding.rvSwipeRefresh.visibility = View.VISIBLE
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

                            //SnackBarUtil.showSnackbar(context, message, false)

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }

    private fun getallpendingPermissions() {
        val pendingPermissionRequest = PendingPermissonRequest(
            //UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            0,
            binding.txtFromDatePendReq.text.toString(),
            binding.txtToDatePendReq.text.toString()
        )


        pendingPermissionViewModel.getPendingHRPermissionStatus(
            mContext!!,
            pendingPermissionRequest
        )
    }

    private fun addObserverPendingPermission() {
        pendingPermissionViewModel.pending_permission_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        /* binding.rvSwipeRefresh.isRefreshing = false
                         fromRefresh = false
                         binding.rvSwipeRefresh.visibility = View.VISIBLE*/

                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { pendingPermissionResponse ->
                            val pendingPermissionResponse = pendingPermissionResponse
                            if (pendingPermissionResponse != null) {

                                // if (pendingPermissionResponse.message!!.contains(mContext!!.resources.getString(R.string.response_error_txt))) {
                                if(pendingPermissionResponse.message!!.contains(mContext!!.resources.getString(R.string.no_record_found_txt))){
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    // binding.rvSwipeRefresh.visibility = View.GONE
                                    pendingPermissionlist.clear()
                                    pendingleaveslist.clear()
                                    pendindManualEntrylist.clear()
                                    binding.recyclerView.adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (pendingPermissionResponse.data !=null&&pendingPermissionResponse.data.isNotEmpty()) {
                                        binding.txtNoRecord.visibility = View.GONE
                                        // binding.rvSwipeRefresh.visibility = View.VISIBLE
                                        pendingleaveslist.clear()
                                        pendingPermissionlist.clear()
                                        pendindManualEntrylist.clear()
                                        for (i in 0 until pendingPermissionResponse.data.size) {
                                            pendingPermissionlist.addAll(
                                                arrayListOf(
                                                    pendingPermissionResponse.data[i]
                                                )
                                            )
                                        }
                                        //arrListSelfReport.addAll(selfReportResponse.data)

                                        binding.recyclerView.adapter?.notifyDataSetChanged()

                                        binding.txtNoRecord.visibility = View.GONE
                                        // binding.rvSwipeRefresh.visibility = View.VISIBLE
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

    private fun getPendingLeavesDetails(id: Int) {

        val pendingLeavesRequest = PendingForManagerRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            //UserShardPrefrences.getManagerId(mContext!!)!!.toInt(),
            //174,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            id,
            binding.txtFromDatePendReq.text.toString(),
            binding.txtToDatePendReq.text.toString()
        )


        viewModel.getPendingHRLeaves(
            mContext!!,
            pendingLeavesRequest
        )
    }

    private fun initComponents() {
        pendingleaveslist = arrayListOf()
        pendindManualEntrylist = arrayListOf()
        pendingPermissionlist = arrayListOf()

    }

    private fun addObserverForPendingLeaves() {
        viewModel.pendingHR_leaves_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        /*  binding.rvSwipeRefresh.isRefreshing = false
                          fromRefresh = false
                          binding.rvSwipeRefresh.visibility = View.VISIBLE*/

                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { pendingLeaveResponse ->
                            val pendingLeaveResponse = pendingLeaveResponse
                            if (pendingLeaveResponse != null) {
                                if(pendingLeaveResponse.message!!.contains(mContext!!.resources.getString(R.string.no_record_found_txt))){
                                    // if (pendingLeaveResponse.message!!.contains(mContext!!.resources.getString(R.string.no_record_found_txt))) {
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    // binding.rvSwipeRefresh.visibility = View.GONE
                                    Log.d("data", "addObserverForPendingLeaves: $pendingLeaveResponse")
                                    pendingPermissionlist.clear()
                                    pendingleaveslist.clear()
                                    pendindManualEntrylist.clear()
                                    binding.recyclerView.adapter?.notifyDataSetChanged()

                                }
                                //SnackBarUtil.showSnackbar(context, pendingLeaveResponse.message)
                                else {
                                    if (pendingLeaveResponse.data !=null&&pendingLeaveResponse.data.isNotEmpty()) {

                                        pendingleaveslist.clear()
                                        pendingPermissionlist.clear()
                                        pendindManualEntrylist.clear()

                                        for (i in 0 until pendingLeaveResponse.data.size) {
                                            pendingleaveslist.addAll(listOf(pendingLeaveResponse.data[i]))
                                        }

                                        binding.recyclerView.adapter?.notifyDataSetChanged()
                                        binding.txtNoRecord.visibility = View.GONE
                                        // binding.rvSwipeRefresh.visibility = View.VISIBLE

                                        //setAdapter()

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

    private fun setDate() {
//        String currentDate = DateTimeOp.getCurrentDateTime("yyyy-MM-dd");
        val finalDateMonth: String = DateTime_Op.getFinalDateMonth("yyyy-MM-dd")

//        String previousMonthDate = DateTimeOp.getPreviousMonthDateTime("yyyy-MM-dd");
        val initialDateMonth: String = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")
        binding.txtFromDatePendReq.setText(initialDateMonth)
        binding.txtToDatePendReq.setText(finalDateMonth)

    }

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.rlToDatePendReq.setOnClickListener(this)
        binding.rlFromDatePendReq.setOnClickListener(this)
        binding.ivSearchIcon.setOnClickListener(this)
        binding.spList.onItemSelectedListener = this
    }


    override fun onResume() {
        super.onResume()


        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.fragment_title_employee_request))
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
        callRequestTypeAPI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()


            R.id.rl_to_date_pend_req -> {
                datePicker(binding.txtToDatePendReq.text.toString(), binding.txtToDatePendReq)
            }

            R.id.rl_from_date_pend_req -> {
                datePicker(binding.txtFromDatePendReq.text.toString(), binding.txtFromDatePendReq)

            }
            R.id.iv_search_icon -> {
                if(DateTime_Op.validateDates(binding.txtFromDatePendReq.text.toString(),binding.txtToDatePendReq.text.toString())){
                    when{
                        (selectedPosition==0)->{

                            callAllPendingPermissionsAPI()

                        }
                        (selectedPosition==1)->{
                            callPendingLeavesAPI(0)
                        }
                        (selectedPosition==2)->{
                            callAllPendingManualEntryRequestAPI()
                        }
                    }

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
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"FragmentManagerPendingRequests")
        datePickerFragment.show(parentFragmentManager, "datePicker")
        /*  val now = Calendar.getInstance()
            //now[year, month - 1] = day

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
                        Log.d("DatePickerDialogue", "pickDate: $e")
                    }
                },

                now[Calendar.YEAR],
                now[Calendar.MONTH],
                now[Calendar.DAY_OF_MONTH]
            )
            dpd.locale=Locale.ENGLISH
            dpd.setOkText("OK")
            dpd.setCancelText("Cancel")
            dpd.version = DatePickerDialog.Version.VERSION_2
            dpd.show(requireActivity().supportFragmentManager, "Datepickerdialog")

    */
    }

    override fun onApproveItemClick(position: Int, type: String) {
        // Create custom dialog object
    status_id = 3;
        val builder = AlertDialog.Builder(mContext!!)
        val inflater = LayoutInflater.from(mContext)
        val dialogView = inflater.inflate(R.layout.accept_reject_dialogue, null)
        builder.setView(dialogView)

        val text = dialogView.findViewById<TextInputLayout>(R.id.dialogWrapper)
        val title = dialogView.findViewById<TextView>(R.id.tv_title)
        val submit = dialogView.findViewById<Button>(R.id.submitButton)
        val tv_msg = dialogView.findViewById<TextView>(R.id.tv_msg)
        val cancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        val alertDialog = builder.create()
        submit.setTextColor(colorPrimary)
      //  cancel.setTextColor(colorPrimary)
        cancel.setTextColor(resources.getColor(R.color.red))

        text.visibility = View.GONE
        tv_msg.visibility = View.VISIBLE
        tv_msg.setText(R.string.dialog_msg)
        submit.setText(R.string.dialog_btn_yes)
        title.text = if (status_id == 3) getString(R.string.approve) else getString(R.string.reject)
        val buttonsLayout = dialogView.findViewById<LinearLayout>(R.id.btn_layout)
        val params = buttonsLayout.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.BELOW, tv_msg.id)
        buttonsLayout.layoutParams = params

        alertDialog.setOnShowListener {
            alertDialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                // Set the dialog width to 90% of the screen width and height to WRAP_CONTENT
                setLayout(
                    SnackBarUtil.getWidth(context) / 100 * 90,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setGravity(Gravity.CENTER)
            }
        }

        submit.setOnClickListener {
            if (EnumUtils.isNetworkConnected(context)) {
                callRejectOrAccept(type, type, position, 3)
            } else {
                SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,getString(R.string.no_internet_connection),R.drawable.caution,resources.getColor(R.color.red))

                //SnackBarUtil.showSnackbar(context, getString(R.string.no_connection), false)
            }
            alertDialog.dismiss()
        }


        cancel.setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()
    }

    override fun onRejectItemClick(position: Int, type: String) {
        val builder = AlertDialog.Builder(mContext!!)
        val inflater = LayoutInflater.from(mContext)
        val dialogView = inflater.inflate(R.layout.accept_reject_dialogue, null)
        builder.setView(dialogView)

        val text = dialogView.findViewById<TextInputLayout>(R.id.dialogWrapper)
        val title = dialogView.findViewById<TextView>(R.id.tv_title)
        val submit = dialogView.findViewById<Button>(R.id.submitButton)
        val tv_msg = dialogView.findViewById<TextView>(R.id.tv_msg)
        val cancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        val alertDialog = builder.create()
      submit.setTextColor(colorPrimary)
     // cancel.setTextColor(colorPrimary)
        cancel.setTextColor(resources.getColor(R.color.red))


      alertDialog.setOnShowListener {
            alertDialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                // Set the dialog width to 90% of the screen width and height to WRAP_CONTENT
                setLayout(
                    SnackBarUtil.getWidth(context) / 100 * 90,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setGravity(Gravity.CENTER)
            }
        }
        submit.setOnClickListener {
            val remarks = text.editText!!.text.toString()
            title.text=mContext!!.resources.getString(R.string.reject_txt)
            if (EnumUtils.isNetworkConnected(context)) {
                when (type) {
                    "L" -> {
                        callRejectOrAccept(type,remarks,position,5)
                    }
                    "M" -> {
                        callRejectOrAccept(type,remarks,  position, 5)
                    }
                    "P" -> {
                        callRejectOrAccept(type,remarks,  position, 5)
                    }
                }

            } else {
                SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,getString(R.string.no_internet_connection),R.drawable.caution,resources.getColor(R.color.red))

                // SnackBarUtil.showSnackbar(context, getString(R.string.no_connection), false)
            }
            alertDialog.dismiss()
        }
        cancel.setOnClickListener { alertDialog.dismiss() }

      alertDialog.show()
    }


    private fun callRejectOrAccept(
        type: String,
        remarks: String,
        position: Int,
        nextApprovalStatus: Int
    ) {
        if (nextApprovalStatus==3 && type.equals("L")){
            CallforSaveApprovedLeaveAPI(position,type)
        }
        if (nextApprovalStatus==5 && type.equals("L")){
            CallforRejectbyHRAPI(position,  remarks ,nextApprovalStatus,type)
        }
        if (nextApprovalStatus==3 && type.equals("P")){
            CallforSaveApprovedPermissionAPI(position,3)
        }
        if (nextApprovalStatus==5 && type.equals("P")){
            CallforRejectbyHRAPI(position,  remarks,nextApprovalStatus,type)
        }
        if (nextApprovalStatus==3 && type.equals("M")){
            CallForsaveManualEntryRequestAPI(position,3)
        }
        if (nextApprovalStatus==5 && type.equals("M")){
            CallForRejectManualEntryRequestAPI(position,remarks)
        }
    }

    private fun CallForRejectManualEntryRequestAPI(position: Int,remarks: String) {
        addObserverForRejectManualEntryByManager()
        getRejectManualEntryStatus(position, remarks)
    }

    private fun getRejectManualEntryStatus(position: Int, remarks: String) {
        val  rejectManualEntryData = onClickRejectManualEntryRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            pendindManualEntrylist[position].moveRequestId,
            UserShardPrefrences.getUniqueId(mContext)!!.toString(),
            remarks ,
            remarks
        )


        onclick_reject_manual_entry_viewModel.getonClickRejectManualEntryStatus(
            mContext!!,
            rejectManualEntryData
        )
    }

    private fun addObserverForRejectManualEntryByManager() {
        onclick_reject_manual_entry_viewModel.onclick_reject_manual_entry_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        /* binding.rvSwipeRefresh.isRefreshing = false
                         fromRefresh = false
                         binding.rvSwipeRefresh.visibility = View.VISIBLE
 */
                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { rejectmanualEntryResponse ->
                            val rejectmanualEntryResponse = rejectmanualEntryResponse
                            if (rejectmanualEntryResponse != null) {

                                if (rejectmanualEntryResponse.message!!.contains(mContext!!.resources.getString(R.string.response_error_txt))) {
                                    binding.txtNoRecord.visibility = View.VISIBLE

                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,rejectmanualEntryResponse.data.success,R.drawable.caution,resources.getColor(R.color.red))


                                }
                                //
                                else {


                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,rejectmanualEntryResponse.data.success,R.drawable.app_icon,colorPrimary,
                                        SnackBarUtil.OnClickListenerNew {
                                            callAllPendingManualEntryRequestAPI()

                                            binding.recyclerView.adapter?.notifyDataSetChanged()

                                            binding.txtNoRecord.visibility = View.GONE
                                        })




                                }

                                // SnackBarUtil.showSnackbar(context, rejectmanualEntryResponse.message)


                            }


                        }

                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.app_icon,colorPrimary)

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

    private fun CallForsaveManualEntryRequestAPI(position: Int, nextApprovalStatus: Int) {
        addObserverForSaveApproveManualEntryRequest()
        ApproveManualEntryRequestByManager(nextApprovalStatus,position)
    }

    private fun ApproveManualEntryRequestByManager(nextApprovalStatus: Int, position: Int) {
        val  approveManualEntryData = onClickApproveManualEntryRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            pendindManualEntrylist[position].moveRequestId,
            nextApprovalStatus,
            ""
        )


        onclick_approv_manual_entry_viewModel.getonClickApproveManualEntryStatus(
            mContext!!,
            approveManualEntryData
        )
    }

    private fun addObserverForSaveApproveManualEntryRequest() {
        onclick_approv_manual_entry_viewModel.onclick_approve_manual_entry_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        /* binding.rvSwipeRefresh.isRefreshing = false
                         fromRefresh = false
                         binding.rvSwipeRefresh.visibility = View.VISIBLE*/

                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { approvemanualEntryResponse ->
                            val approvemanualEntryResponse = approvemanualEntryResponse
                            if (approvemanualEntryResponse != null) {

                                if (approvemanualEntryResponse.message!!.contains(mContext!!.resources.getString(R.string.response_error_txt))) {
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    // binding.rvSwipeRefresh.visibility = View.GONE

                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,approvemanualEntryResponse.data.error,R.drawable.caution,resources.getColor(R.color.red))

                                }

                                else {



                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,approvemanualEntryResponse.data.success,R.drawable.app_icon,colorPrimary,
                                        SnackBarUtil.OnClickListenerNew {
                                            callAllPendingManualEntryRequestAPI()

                                            binding.recyclerView.adapter?.notifyDataSetChanged()

                                            binding.txtNoRecord.visibility = View.GONE
                                        })



                                }



                            }


                        }

                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.app_icon,colorPrimary)

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

    private fun CallforSaveApprovedPermissionAPI( position: Int ,nextApprovalStatus: Int) {
        addObserverForSaveApprovePermissionRequest()
        ApprovePermissionRequestByManager(nextApprovalStatus,position)
    }

    private fun ApprovePermissionRequestByManager( nextApprovalStatus: Int, position: Int) {
        val approvePermissionData = onClickApproveRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            nextApprovalStatus,
            pendingPermissionlist[position].permissionId,
            ""
        )


        onclick_approv_permission_viewModel.getApprovePermissions(
            mContext!!,
            approvePermissionData
        )
    }

    private fun addObserverForSaveApprovePermissionRequest() {
        onclick_approv_permission_viewModel.approve_byHR_permission_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        /*  binding.rvSwipeRefresh.isRefreshing = false
                          fromRefresh = false
                          binding.rvSwipeRefresh.visibility = View.VISIBLE
  */
                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { saveapproveLeaveResponse ->
                            val saveapproveLeaveResponse = saveapproveLeaveResponse
                            if (saveapproveLeaveResponse != null) {

                                if (saveapproveLeaveResponse.message!!.contains(mContext!!.resources.getString(R.string.response_error_txt))) {
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    //   binding.rvSwipeRefresh.visibility = View.GONE
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,saveapproveLeaveResponse.data.error,R.drawable.caution,resources.getColor(R.color.red))

                                    // SnackBarUtil.showSnackbar(context, saveapproveLeaveResponse.message)
                                }
                                //
                                else {



                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,saveapproveLeaveResponse.data.success,R.drawable.app_icon,colorPrimary,
                                        SnackBarUtil.OnClickListenerNew {

                                            callAllPendingPermissionsAPI()
                                            binding.recyclerView.adapter?.notifyDataSetChanged()

                                            binding.txtNoRecord.visibility = View.GONE
                                            // binding.rvSwipeRefresh.visibility = View.VISIBLE


                                        })





                                }


                            }


                        }

                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.app_icon,colorPrimary)

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

    private fun CallforRejectbyHRAPI(
        position: Int,
        remarks: String,
        nextApprovalStatus: Int,
        type: String
    ) {
        addObserverForRejectByManager(position)
        getRejectRequestStatus(type,position, remarks, nextApprovalStatus)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getRejectRequestStatus(type: String, position: Int, remarks: String, nextApprovalStatus: Int) {
        if (type.equals("L")){
            val rejectLeaveData = RejectLeaveByManagerRequest(
                UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                pendingleaveslist[position].leaveId,
                remarks,
                "L"
            )
            rejectviewModel.getRejectHRLeaveStatus(
                mContext!!,
                rejectLeaveData
            )


        }
        else
        {
            val rejectPermRequest = RejectLeaveByManagerRequest(
                UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
                UserShardPrefrences.getLanguage(mContext)!!.toInt(),
                pendingPermissionlist[position].permissionId,
                remarks,
                "P"
            )
            rejectviewModel.getRejectHRLeaveStatus(
                mContext!!,
                rejectPermRequest
            )
        }

    }

    private fun addObserverForRejectByManager(position: Int) {
        rejectviewModel.reject_leave_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        /*  binding.rvSwipeRefresh.isRefreshing = false
                          fromRefresh = false
                          binding.rvSwipeRefresh.visibility = View.VISIBLE*/

                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { rejectLeaveResponse ->
                            val rejectLeaveResponse = rejectLeaveResponse
                            if (rejectLeaveResponse != null) {

                                if (rejectLeaveResponse.message!!.contains(mContext!!.resources.getString(R.string.response_error_txt))) {
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    //   binding.rvSwipeRefresh.visibility = View.GONE
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,rejectLeaveResponse.data.error,R.drawable.caution,resources.getColor(R.color.red))

                                }
                                //
                                else {



                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,rejectLeaveResponse.data.success,R.drawable.app_icon,colorPrimary,
                                        SnackBarUtil.OnClickListenerNew {
                                            if (position < pendingPermissionlist.size){
                                                callAllPendingPermissionsAPI()
                                            } else{
                                                callPendingLeavesAPI(0)
                                            }

                                            binding.recyclerView.adapter?.notifyDataSetChanged()
                                            binding.txtNoRecord.visibility = View.GONE
                                        })



                                    // binding.rvSwipeRefresh.visibility = View.VISIBLE

                                }


                            }


                        }

                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.app_icon,colorPrimary)

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

    private fun CallforSaveApprovedLeaveAPI(
        position: Int,
        type: String
    ) {
        addObserverForApproveRequest()
        getonClickApproveRequest(position,type)
    }

    private fun getonClickApproveRequest(
        position: Int,
        type: String,
    ) {

        val approveLeavesRequest = Approve_HRLeavesRequest(
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            pendingleaveslist[position].leaveId,
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId
        )
        approve_leavesViewModel.getApproveLeaves(
            mContext!!,
            approveLeavesRequest
        )


    }

    private fun addObserverForApproveRequest() {
        approve_leavesViewModel.approve_byHR_leaves_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        /*  binding.rvSwipeRefresh.isRefreshing = false
                          fromRefresh = false
                          binding.rvSwipeRefresh.visibility = View.VISIBLE*/

                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { approveLeaveResponse ->
                            val approveLeaveResponse = approveLeaveResponse
                            if (approveLeaveResponse != null) {

                                if (approveLeaveResponse.message!!.contains(mContext!!.resources.getString(R.string.response_error_txt))) {
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    //  binding.rvSwipeRefresh.visibility = View.GONE
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,approveLeaveResponse.message,R.drawable.caution,resources.getColor(R.color.red))

                                }
                                //
                                else {


                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,approveLeaveResponse.message,R.drawable.app_icon,colorPrimary,
                                        SnackBarUtil.OnClickListenerNew {

                                            callPendingLeavesAPI(0)

                                            binding.recyclerView.adapter?.notifyDataSetChanged()
                                            binding.txtNoRecord.visibility = View.GONE
                                        })




                                    //   binding.rvSwipeRefresh.visibility = View.VISIBLE
                                }

                            }


                        }

                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.app_icon,colorPrimary)

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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.sp_list -> {
                spinnerItemBO_requsettype = arrayList_type[position]
                selectedPosition=position

                when{
                    (selectedPosition==0)->{
                        callAllPendingPermissionsAPI()

                    }
                    (selectedPosition==1)->{
                        callPendingLeavesAPI(0)
                    }
                    (selectedPosition==2)->{
                        callAllPendingManualEntryRequestAPI()
                    }
                }

                /*
                               when (spinnerItemBO_requsettype!!) {
                                    mContext!!.resources.getString(R.string.leaves_txt) -> {
                                        callPendingLeavesAPI(0)
                                    }
                                   mContext!!.resources.getString(R.string.permissions_txt) -> {
                                        callAllPendingPermissionsAPI()
                                    }
                                   mContext!!.resources.getString(R.string.manual_entry_request) -> {
                                        callAllPendingManualEntryRequestAPI()
                                    }
                                   mContext!!.resources.getString(R.string.over_time_request) -> {
                                        callAllPendingManualEntryRequestAPI()
                                    }
                                }
                */



            }
        }

        //  parent?.setSelection(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}
