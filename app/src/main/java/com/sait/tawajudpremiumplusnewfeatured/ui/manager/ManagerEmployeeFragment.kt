package com.sait.tawajudpremiumplusnewfeatured.ui.manager

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
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sait.tawajudpremiumplusnewfeatured.ProfileFragment
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentManagerEmployeeProfilesBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.adapter.ManagerEmpAdapter


import com.sait.tawajudpremiumplusnewfeatured.ui.manager.listener.EmployeeItemClickListenerManager
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.ManagerEmpRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.Manager_EmployeeData
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.viewmodels.Manager_empViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.util.Calendar

class ManagerEmployeeFragment : BaseFragment(), View.OnClickListener,
    EmployeeItemClickListenerManager {
    private lateinit var binding: FragmentManagerEmployeeProfilesBinding
    private lateinit var viewModel: Manager_empViewModel

    private var mContext: Context? = null

    private var str_employee_no: String? = null

    private lateinit var arrListViolation: ArrayList<Manager_EmployeeData>
    private var violationAdapter: ManagerEmpAdapter? = null
    private var fromRefresh = false
    var handler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagerEmployeeProfilesBinding.inflate(inflater, container, false)
        mContext = inflater.context

        viewModel = ViewModelProvider(this)[Manager_empViewModel::class.java]

        binding.txtEmployeeName.text = UserShardPrefrences.getUserInfo(mContext).employeeName
        binding.txtEmployeeName.background =
            resources.getDrawable(R.drawable.edit_disable_text)

        val activity = this.activity as MainActivity?
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        setClickListeners(activity)

        setDate()

        init()


        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Filter data based on search query
                violationAdapter!!.filter.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return binding.root
    }


    private fun init() {


        initComponents()
        callEmployeeDetailsUnderManagerAPI()
        setAdapter()
        addSwipeToRefresh()


    }


    private fun addSwipeToRefresh() {


        binding.rvSwipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            fromRefresh = true
            callEmployeeDetailsUnderManagerAPI()

        })

    }

    private fun initComponents() {
        arrListViolation = arrayListOf()
    }


    private fun setAdapter() {

        binding.rvManageremp.layoutManager = LinearLayoutManager(mContext)

        violationAdapter = mContext?.let { ManagerEmpAdapter(arrListViolation, this, it) }

        binding.rvManageremp.adapter = violationAdapter


    }

    private fun callEmployeeDetailsUnderManagerAPI() {
        addObserver()
        getEmployeeDetails()
    }






    private fun addObserver() {
        viewModel.manager_empResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        binding.rvSwipeRefresh.isRefreshing = false
                        fromRefresh = false
                        binding.rvSwipeRefresh.visibility = View.VISIBLE

                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { managerEmpResponse ->
                            val managerEmpResponse = managerEmpResponse
                            if (managerEmpResponse != null) {


                                if(managerEmpResponse.message!!.contains(mContext!!.resources.getString(R.string.response_no_record_found_txt))){
                                    binding.txtNoRecord.visibility= View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.GONE
                                }

                                else{
                                    binding.txtNoRecord.visibility= View.GONE
                                    binding.rvManageremp.visibility= View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.VISIBLE
                                    arrListViolation.clear()
                                    arrListViolation.addAll(managerEmpResponse.data)
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

    private fun getEmployeeDetails() {
        val managerEmpRequest = ManagerEmpRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt()

            // UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )
        Log.e("commonRequest",managerEmpRequest.toString())

        viewModel.getManager_empData(mContext!!, managerEmpRequest)


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
                callEmployeeDetailsUnderManagerAPI()
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
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"ManagerEmployeeFragment")
        datePickerFragment.show(parentFragmentManager, "datePicker")
    /*   val now = Calendar.getInstance()
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

        dpd.version = DatePickerDialog.Version.VERSION_2
        dpd.show(requireActivity().supportFragmentManager, "Datepickerdialog")*/

    }

    override fun onEmployeeItemClickManager(manager_EmployeeData: Manager_EmployeeData) {

        //  str_employee_no = arrListViolation[position].employeeNo
        // SnackBarUtil.showSnackbar(context, "Employee Item click", false)
        //replaceFragment(ProfileFragment(), R.id.flFragment, true)


        val profileFragment = ProfileFragment()
        val args_profile = Bundle()
        args_profile.putString("employee no", manager_EmployeeData.fK_EmployeeId.toString())
        profileFragment.arguments = args_profile

        replaceFragment(profileFragment, R.id.flFragment, true)
    }



}
