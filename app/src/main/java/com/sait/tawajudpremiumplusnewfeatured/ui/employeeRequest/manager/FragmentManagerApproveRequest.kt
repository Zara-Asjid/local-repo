package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.adapters.Spinner_Adapter
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentManagerApproveRequestBinding
import com.sait.tawajudpremiumplusnewfeatured.items.SpinnerItem_BO_Type
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.adapters.Approve_Adapter_Manager
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.ApproveLeaveData
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve_LeavesRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryData
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionData
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.viewmodels.Leaves.Approve.ApproveLeaveViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.ApprovedList.ApproveManualEntryViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Permissions.ApprovedList.ApprovePermissionViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.requestType.RequestTypeData
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.requestType.RequestTypeViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.util.*
import kotlin.collections.ArrayList

class FragmentManagerApproveRequest : BaseFragment(), View.OnClickListener,AdapterView.OnItemSelectedListener{
    private var _binding: FragmentManagerApproveRequestBinding? = null
    private val binding get() = _binding!!
    var spinnerItemBO_requsettype: SpinnerItem_BO_Type? = null
    val arrayList_type = java.util.ArrayList<SpinnerItem_BO_Type>()
    var handler: Handler = Handler()

    private lateinit var approve_leave_viewModel: ApproveLeaveViewModel
    private lateinit var approveManualEntryviewModel: ApproveManualEntryViewModel
    private lateinit var approvePermissionviewModel: ApprovePermissionViewModel

    private lateinit var approveleaveslist: ArrayList<ApproveLeaveData>
    private lateinit var approvePermissionlist: ArrayList<ApprovePermissionData>
    private lateinit var approveManualEntrylist: ArrayList<ApproveManualEntryData>
    var selectedPosition: Int = 0
    private var approveAdapter: Approve_Adapter_Manager? = null
    private lateinit var viewModel_RequestType: RequestTypeViewModel

    private var fromRefresh = false
    private var mContext: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentManagerApproveRequestBinding.inflate(inflater, container, false)
        val activity = this.activity as MainActivity?
        mContext = inflater.context
        viewModel_RequestType = ViewModelProvider(this)[RequestTypeViewModel::class.java]
        approveManualEntryviewModel =
            ViewModelProvider(this)[ApproveManualEntryViewModel::class.java]
        approvePermissionviewModel =
            ViewModelProvider(this)[ApprovePermissionViewModel::class.java]
        approve_leave_viewModel =
            ViewModelProvider(this)[ApproveLeaveViewModel::class.java]
        setClickListeners(activity)
        setDate()
        init()
        callRequestTypeAPI()
        return binding.root

    }
    private fun init() {
        initComponents()
     //   setUpDropDown()
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



    private fun setUpDropDown_RequestType(empList: java.util.ArrayList<SpinnerItem_BO_Type>) {
        val arrayList = java.util.ArrayList<String>()
        //arrayList.add("Please select")
        for (i in empList.indices) {

            arrayList.add(empList[i].desc_en)

        }
        val customAdapter = Spinner_Adapter(context, arrayList)
        binding.spList1.adapter = customAdapter
        binding.spList1?.setSelection(0)
    }

    private fun getRequestTypeDetails() {


        val commonRequest = CommonRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )


        viewModel_RequestType.getRequestTypeData(
            mContext!!, commonRequest
        )


    }
    private fun initComponents() {
        approveleaveslist = arrayListOf()
        approvePermissionlist= arrayListOf()
        approveManualEntrylist= arrayListOf()

    }
    private fun setAdapter() {
        if (approveAdapter == null || binding.recyclerView.adapter == null) {
            binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
            binding.recyclerView.itemAnimator = DefaultItemAnimator()
           approveAdapter = Approve_Adapter_Manager(
                approveleaveslist,
               approvePermissionlist,
               approveManualEntrylist,
                this,
                mContext!!
            )
            binding.recyclerView.adapter = approveAdapter
        }
    }
    private fun addSwipeToRefresh() {

        binding.rvSwipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            fromRefresh = true
            if (approveleaveslist.size != 0 && approvePermissionlist.size == 0 && approveManualEntrylist.size == 0) {
                callgetAllApproveLeavesAPI()
            }
            if (approveleaveslist.size == 0 && approvePermissionlist.size != 0 && approveManualEntrylist.size == 0) {
                callAllApprovePermissionsAPI()
            }
            if (approveleaveslist.size == 0 && approvePermissionlist.size == 0 && approveManualEntrylist.size != 0) {
              callAllApproveManualEntryRequestAPI()
            }
        })

    }
    private fun setDate() {
//        String currentDate = DateTimeOp.getCurrentDateTime("yyyy-MM-dd");
        val finalDateMonth: String = DateTime_Op.getFinalDateMonth("yyyy-MM-dd")

//        String previousMonthDate = DateTimeOp.getPreviousMonthDateTime("yyyy-MM-dd");
        val initialDateMonth: String = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")
        binding.txtMngFromDateApReq.setText(initialDateMonth)
        binding.txtMngToDateApReq.setText(finalDateMonth)

    }
/*
    private fun setUpDropDown() {
        val arrayList = ArrayList<String>()
        arrayList.add("" + requireContext().resources.getString(R.string.h_Leaves))
        arrayList.add("" + requireContext().resources.getString(R.string.h_Permissions))
        arrayList.add("" + requireContext().resources.getString(R.string.manual_entry_request))
     //   arrayList.add("" + requireContext().resources.getString(R.string.over_time_request))
        val customAdapter = Spinner_Adapter(context, arrayList)
        binding.spList1?.adapter = customAdapter
        binding.spList1?.setSelection(0)
        arrayList_type.clear()
        arrayList_type= arrayList
    }
*/

    private fun callAllApproveManualEntryRequestAPI() {
        addObserverForAllApproveManualEntryRequest()
        getAllApproveManualEntryRequestDetails()
    }

    private fun getAllApproveManualEntryRequestDetails() {
        val approveManualEntryRequest = ApproveManualEntryRequest(
           /* UserShardPrefrences.getUserInfo(mContext).fKEmployeeId*/
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            binding.txtMngFromDateApReq.text.toString(),
            binding.txtMngToDateApReq.text.toString(),
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )


        approveManualEntryviewModel.getApproveManualEntryStatus(
            mContext!!,
            approveManualEntryRequest
        )
    }

    private fun addObserverForAllApproveManualEntryRequest() {
        approveManualEntryviewModel.approve_manual_entry_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.rvSwipeRefresh.isRefreshing = false
                        fromRefresh = false
                        binding.rvSwipeRefresh.visibility = View.VISIBLE

                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { approveallManualEntryResponse ->
                            val approveallManualEntryResponse = approveallManualEntryResponse
                            if (approveallManualEntryResponse != null) {

                                if (approveallManualEntryResponse.message!!.contains(mContext!!.resources.getString(R.string.response_error_txt))) {
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.GONE

                                }
                                //
                                else {
                                    if (approveallManualEntryResponse.data !=null&&approveallManualEntryResponse.data.isNotEmpty()) {
                                        binding.txtNoRecord.visibility = View.GONE
                                        binding.rvSwipeRefresh.visibility = View.VISIBLE
                                        approveManualEntrylist.clear()
                                        for (i in 0 until approveallManualEntryResponse.data.size) {
                                            approveManualEntrylist.addAll(
                                                listOf(
                                                    approveallManualEntryResponse.data[i]
                                                )
                                            )

                                        }
                                    approveleaveslist.clear()
                                    approvePermissionlist.clear()
                                    binding.recyclerView.adapter?.notifyDataSetChanged()
                                    binding.txtNoRecord.visibility = View.GONE
                                    binding.rvSwipeRefresh.visibility = View.VISIBLE
                                    }
                                   /* if (approveallManualEntryResponse.data == null){
                                        SnackBarUtil.showSnackbar(context, approveallManualEntryResponse.message)
                                    }*/
                                }


                            }


                        }

                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,
                                message,R.drawable.caution,resources.getColor(R.color.red))

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

    private fun callAllApprovePermissionsAPI() {
        addObserverForAllApprovePermissions()
        getAllApprovePermissionsDetails()
    }

    private fun addObserverForAllApprovePermissions() {
        approvePermissionviewModel.approve_permission_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.rvSwipeRefresh.isRefreshing = false
                        fromRefresh = false
                        binding.rvSwipeRefresh.visibility = View.VISIBLE

                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { approveallPermissionResponse ->
                            val approveallPermissionResponse = approveallPermissionResponse
                            if (approveallPermissionResponse != null) {

                                if (approveallPermissionResponse.message!!.contains(mContext!!.resources.getString(R.string.response_error_txt))) {
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.GONE

                                }
                                //
                                else {
                                    if (approveallPermissionResponse.data !=null&&approveallPermissionResponse.data.isNotEmpty()) {
                                        binding.txtNoRecord.visibility = View.GONE
                                        binding.rvSwipeRefresh.visibility = View.VISIBLE
                                        approvePermissionlist.clear()
                                        for (i in 0 until approveallPermissionResponse.data.size) {
                                            approvePermissionlist.addAll(
                                                listOf(
                                                    approveallPermissionResponse.data[i]
                                                )
                                            )

                                        }
                                    approveleaveslist.clear()
                                    approveManualEntrylist.clear()
                                    binding.recyclerView.adapter?.notifyDataSetChanged()
                                    binding.txtNoRecord.visibility = View.GONE
                                    binding.rvSwipeRefresh.visibility = View.VISIBLE
                                    }else{

                                    }
                                  /*  if (approveallPermissionResponse.data== null){
                                        SnackBarUtil.showSnackbar(context, approveallPermissionResponse.message)
                                    }*/
                                }


                            }


                        }

                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                           // SnackBarUtil.showSnackbar(context, message, false)
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

    private fun getAllApprovePermissionsDetails() {
        val approvePermissionsRequest = ApprovePermissionRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            binding.txtMngFromDateApReq.text.toString(),
            binding.txtMngToDateApReq.text.toString(),
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )


        approvePermissionviewModel.getApprovePermissionStatus(
            mContext!!,
            approvePermissionsRequest
        )
    }


    private fun callgetAllApproveLeavesAPI() {
        addObserverForAllApproveLeaves()
        getAllApproveLeavesDetails()
    }

    private fun getAllApproveLeavesDetails() {
        val approveLeavesRequest = Approve_LeavesRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId
            /*UserShardPrefrences.getUserInfo(mContext).fKEmployeeId*/,
            binding.txtMngFromDateApReq.text.toString(),
            binding.txtMngToDateApReq.text.toString(),
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )


        approve_leave_viewModel.getAllApproveLeaves(
            mContext!!,
            approveLeavesRequest
        )
    }

    private fun addObserverForAllApproveLeaves() {
        approve_leave_viewModel.approver_leaves_response.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.rvSwipeRefresh.isRefreshing = false
                        fromRefresh = false
                        binding.rvSwipeRefresh.visibility = View.VISIBLE

                        (activity as MainActivity).hideProgressBar()
                        response.data?.let { approveLeaveResponse ->
                            val approveLeaveResponse = approveLeaveResponse
                            if (approveLeaveResponse != null) {

                                if (approveLeaveResponse.message!!.contains(mContext!!.resources.getString(R.string.response_error_txt))) {
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.GONE

                                }
                                //
                                else {
                                    if (approveLeaveResponse.data !=null&&approveLeaveResponse.data.isNotEmpty()) {
                                        binding.txtNoRecord.visibility = View.GONE
                                        binding.rvSwipeRefresh.visibility = View.VISIBLE
                                        approveleaveslist.clear()
                                        for (i in 0 until approveLeaveResponse.data.size) {
                                            approveleaveslist.addAll(listOf(approveLeaveResponse.data[i]))
                                        }
                                    approvePermissionlist.clear()
                                    approveManualEntrylist.clear()

                                    binding.recyclerView.adapter?.notifyDataSetChanged()
                                    binding.txtNoRecord.visibility = View.GONE
                                    binding.rvSwipeRefresh.visibility = View.VISIBLE
                                    }
                                /*    if (approveLeaveResponse.data == null){
                                        SnackBarUtil.showSnackbar(context, approveLeaveResponse.message)
                                    }*/
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


    private fun getFromDate(): String? {
        return binding.txtMngFromDateApReq.text.toString()
    }

    private fun getToDate(): String? {
        return binding.txtMngToDateApReq.text.toString()
    }
    private fun pickDate(day: Int, month: Int, year: Int, textView: TextView) {
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"FragmentManagerApproveRequests")
        datePickerFragment.show(parentFragmentManager, "datePicker")
    /*     val now = Calendar.getInstance()
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
        dpd.locale=Locale.ENGLISH
        dpd.setOkText("OK")
        dpd.setCancelText("Cancel")
        dpd.version = DatePickerDialog.Version.VERSION_2
        dpd.show(requireActivity().supportFragmentManager, "Datepickerdialog")*/

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

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgInfo.setOnClickListener(this)
        activity?.binding!!.layout.imgAlert.setOnClickListener(this)
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.spList1!!.onItemSelectedListener = this
        binding.rlMngToDateApReq.setOnClickListener(this)
        binding.rlMngFromDateApReq.setOnClickListener(this)
        binding.ivSearchIcon.setOnClickListener(this)


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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onPause() {
        super.onPause()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {

        when(p0!!.id){
            R.id.img_info->navigateToUserInfo()
            R.id.img_alert -> navigateToNotifications()
            R.id.img_back -> (activity as MainActivity).onBackPressed()

            R.id.rl_mng_to_date_ap_req -> {
                datePicker(binding.txtMngToDateApReq.text.toString(), binding.txtMngToDateApReq)
            }


            R.id.rl_mng_from_date_ap_req -> {
                datePicker(binding.txtMngFromDateApReq.text.toString(), binding.txtMngFromDateApReq)

            }
            R.id.iv_search_icon -> {
                if(DateTime_Op.validateDates(binding.txtMngFromDateApReq.text.toString(),binding.txtMngToDateApReq.text.toString())) {
                    when {
                        (selectedPosition == 0) -> {

                            callAllApprovePermissionsAPI()

                        }

                        (selectedPosition == 1) -> {
                            callgetAllApproveLeavesAPI()
                        }

                        (selectedPosition == 2) -> {
                            callAllApproveManualEntryRequestAPI()
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

    private fun navigateToNotifications() {

        //  Toast.makeText(activity,"zsdfghjkltygjh",Toast.LENGTH_SHORT).show()

    }

    private fun navigateToUserInfo() {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.sp_list1 -> {
                spinnerItemBO_requsettype = arrayList_type[position]
                selectedPosition=position
                when{
                    (selectedPosition==0)->{
                        callAllApprovePermissionsAPI()

                    }
                    (selectedPosition==1)->{
                        callgetAllApproveLeavesAPI()
                    }
                    (selectedPosition==2)->{
                        callAllApproveManualEntryRequestAPI()
                    }
                }


            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}