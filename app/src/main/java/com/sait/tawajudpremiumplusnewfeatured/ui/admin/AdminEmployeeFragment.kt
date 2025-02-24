package com.sait.tawajudpremiumplusnewfeatured.ui.admin

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
import com.sait.tawajudpremiumplusnewfeatured.ProfileFragment
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.adapters.Spinner_Adapter
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentManagerEmployeeProfilesBinding
import com.sait.tawajudpremiumplusnewfeatured.items.SpinnerItem
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.adapter.AdminAdapter
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.AdminViewModel


import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.AdminData
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.filter.AdminFilterViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.listener.EmployeeItemClickListenerAdmin
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.util.*
import kotlin.collections.ArrayList

class AdminEmployeeFragment : BaseFragment(), View.OnClickListener, EmployeeItemClickListenerAdmin, AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentManagerEmployeeProfilesBinding
    private lateinit var viewModel_admin: AdminViewModel

    private var mContext: Context? = null
    var str_company_id: String? = "0"
    var filter_type:Int=0
    val arrayList_companyIds = arrayListOf<SpinnerItem>()

    private lateinit var arrListEmployees: ArrayList<AdminData>
    private var adminAdapter: AdminAdapter? = null
    private var fromRefresh = false
    private lateinit var viewModel_adminFilter: AdminFilterViewModel
    var handler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagerEmployeeProfilesBinding.inflate(inflater, container, false)
        mContext = inflater.context

        viewModel_admin = ViewModelProvider(this)[AdminViewModel::class.java]
        viewModel_adminFilter = ViewModelProvider(this)[AdminFilterViewModel::class.java]

        binding.txtEmployeeName.text = UserShardPrefrences.getUserInfo(mContext).employeeName
        binding.txtEmployeeName.background =
            resources.getDrawable(R.drawable.edit_disable_text)

        val activity = this.activity as MainActivity?

        setClickListeners(activity)

        setDate()
        callCompanyAPI(0,0)
        init()
      //  callAdminAPI()


        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Filter data based on search query
                adminAdapter!!.filter.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return binding.root
    }

    private fun callCompanyAPI(companyId: Int, filterTypeId: Int) {
        addObserver_Company()
        getCompanyDetails(companyId,filterTypeId)
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

    private fun setUpDropDown_Comapny(arraylistCompanyids: ArrayList<SpinnerItem>) {
        val arrayList = java.util.ArrayList<String>()
        arrayList.add(resources.getString(R.string.select_company))
        for (i in arraylistCompanyids.indices) {
            arrayList.add(arraylistCompanyids[i].title)
        }

        val customAdapter = Spinner_Adapter(context, arrayList)

        //  val  spinner_Custom_Adapter = Spinner_Custom_Adapter(context, arrayList,this)
        binding.spCompanyProfile.adapter = customAdapter
        binding.spCompanyProfile.setSelection(0)

        //   str_company_id = arrayList_Company[0].emp_id.toString()
    }

    private fun init() {


        initComponents()
        callAdminAPI(str_company_id!!)
        setAdapter()
        addSwipeToRefresh()


    }


    private fun addSwipeToRefresh() {


        binding.rvSwipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            fromRefresh = true
            callAdminAPI(str_company_id!!)

        })

    }

    private fun initComponents() {
        arrListEmployees = arrayListOf()
    }


    private fun setAdapter() {

        binding.rvManageremp.layoutManager = LinearLayoutManager(mContext)

        adminAdapter = mContext?.let { AdminAdapter(arrListEmployees, this, it) }

        binding.rvManageremp.adapter = adminAdapter


    }



    private fun callAdminAPI(companyId: String) {
        addObserverAdmin()
        getAdminEmployeeDetails(companyId)
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
                            val managerEmpResponse = managerEmpResponse
                            if (managerEmpResponse != null) {


                                if(managerEmpResponse.message!!.contains(mContext!!.resources.getString(R.string.no_record_found_txt_admin))){
                                    binding.txtNoRecord.visibility= View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.GONE
                                }

                                else{
                                    binding.txtNoRecord.visibility= View.GONE
                                    binding.rvManageremp.visibility= View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.VISIBLE
                                    arrListEmployees.clear()
                                    arrListEmployees.addAll(managerEmpResponse.data)
                                    binding.rvManageremp.adapter?.notifyDataSetChanged()
                                    setAdapter()

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

    private fun getAdminEmployeeDetails(companyId: String) {
        val adminRequest = AdminRequest(companyId.toInt(),0,1,
           0,0, UserShardPrefrences.getUserInfo(mContext)!!.fKEmployeeId,0,0

            //
        )
        Log.e("commonRequest",adminRequest.toString())

       // viewModel_admin.getAdminData(mContext!!, adminRequest)



        viewModel_admin.getAdminData(mContext!!,adminRequest


        )



    }



    private fun setDate() {
//        String currentDate = DateTimeOp.getCurrentDateTime("yyyy-MM-dd");
        val finalDateMonth: String = DateTime_Op.getFinalDateMonth("yyyy-MM-dd")
//        String previousMonthDate = DateTimeOp.getPreviousMonthDateTime("yyyy-MM-dd");
        val initialDateMonth: String = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")
        binding.txtFromDate.setText(initialDateMonth)
        binding.txtToDate.setText(finalDateMonth)
    }

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.rlToDate.setOnClickListener(this)
        binding.rlFromDate.setOnClickListener(this)
        binding.searchIconManagerEmpData.setOnClickListener(this)
        binding.spCompanyProfile.onItemSelectedListener=this
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
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.employee_profiles))
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
        init()
    }
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()


            R.id.rl_to_date -> {
                datePicker(binding.txtToDate.text.toString(),binding.txtToDate)
            }


            R.id.rl_from_date -> {
                datePicker(binding.txtFromDate.text.toString(),binding.txtFromDate)

            }

            R.id.search_icon_managerEmpData -> {
                callAdminAPI(str_company_id!!)
            }
        }

    }

    override fun onPause() {
        super.onPause()

    }


    private fun datePicker(date: String, txtDate: TextView) {
        try {
            // val todate: String = binding.txtToDate.getText().toString()
            if (TextUtils.isEmpty(date)) {
                val c = Calendar.getInstance()
                val mDay = c[Calendar.DAY_OF_MONTH]
                val mMonth = c[Calendar.MONTH]
                val mYear = c[Calendar.YEAR]
                pickDate(mDay, mMonth, mYear,txtDate)
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
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"AdminEmployeeFragment")
        datePickerFragment.show(parentFragmentManager, "datePicker")
    /*val now = Calendar.getInstance()
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
        dpd.show(requireActivity().supportFragmentManager, "Datepickerdialog")*/

    }



    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
       when(parent?.id){
           R.id.sp_company_profile -> {
               if (position == 0) {

               } else {
                   str_company_id = arrayList_companyIds[position - 1].emp_id.toString()
                   callAdminAPI(str_company_id!!)

               }

           }

       }


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onEmployeeItemClickAdmin(adminData: AdminData) {
        val profileFragment = ProfileFragment()
        val args_profile = Bundle()
        args_profile.putString("employee no", adminData.employeeId.toString())
        profileFragment.arguments = args_profile

        replaceFragment(profileFragment, R.id.flFragment, true)
    }

}
