package com.sait.tawajudpremiumplusnewfeatured.ui.violations

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.RequestsFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.adapter.ViolationUpdateAdapter
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.listener.ViolationItemClickListener
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationData
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.viewmodels.ViolationViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.util.*
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentViolationsPendingBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection

class ViolationsPendingFragment() : BaseFragment(), ViolationItemClickListener, OnClickListener {
    private var _binding: FragmentViolationsPendingBinding? = null
    private val binding get() = _binding!!
    private var selectedTab: Int = 0
    private var violations: ViolationData? = null
    private var fromDate: String = ""
    private var toDate: String = ""
    private lateinit var viewModel: ViolationViewModel
    private var mContext: Context? = null
    private var flag: Boolean? = false
    private lateinit var arrListViolation: ArrayList<ViolationData>
    private var violationAdapter: ViolationUpdateAdapter? = null
    private var fromRefresh = false
    private lateinit var bundle: Bundle
    var handler: Handler = Handler()
    private var isApiCalled = false // Flag to track if the API has been called before

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentViolationsPendingBinding.inflate(inflater, container, false)

        mContext = inflater.context

        viewModel = ViewModelProvider(this)[ViolationViewModel::class.java]
        bundle = Bundle()
        /*  if (flag == true){
              restoreState()
          }*/

        setDate()
        setClickListeners()
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }
// Only call the API if it's the first time or on refresh
        if (!isApiCalled) {
            init()
            isApiCalled = true
        }
        // callViolationsAPI()

        return binding.root
    }


    private fun saveState() {
        bundle = Bundle()
        bundle.putInt("selected_tab", 1)
        bundle.putParcelable("violations", violations)
        bundle.putString("from_date", binding.txtFromDateP.text.toString())
        bundle.putString("to_date", binding.txtToDateP.text.toString())
        bundle.putParcelableArrayList("violation_list", arrListViolation)
        flag = true
        arguments = bundle
    }


   /* fun View.saveState(): Bundle {
        val state = Bundle()
        val viewState = SparseArray<Parcelable>()
        saveHierarchyState(viewState)
        state.putSparseParcelableArray("viewState", viewState)
        return state
    }

    fun restoreState() {
        *//*val viewState = state.getSparseParcelableArray<Parcelable>("violations")
        restoreHierarchyState(viewState)*//*
        val state = bundle
        selectedTab = state.getInt("selected_tab", 0)
        violations = state.getParcelable("violations")
        binding.txtFromDate.text = state.getString("from_date") ?: ""
        binding.txtToDate.text = state.getString("to_date") ?: ""
        arrListViolation = state.getParcelableArrayList("violation_list") ?: arrayListOf()
        // Update adapter with the restored list data
        violationAdapter?.updateList(arrListViolation)

    }*/

    private fun setClickListeners() {
        (activity as MainActivity)?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.rlFromDateP.setOnClickListener(this)
        binding.rlToDateP.setOnClickListener(this)
        binding.ivSearchIcon.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(requireActivity().resources.getString(R.string.self_service))

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
    private fun setDate() {
//        String currentDate = DateTimeOp.getCurrentDateTime("yyyy-MM-dd");
        val finalDateMonth: String = DateTime_Op.getFinalDateMonth("yyyy-MM-dd")

//        String previousMonthDate = DateTimeOp.getPreviousMonthDateTime("yyyy-MM-dd");
        val initialDateMonth: String = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")
        binding.txtFromDateP.setText(initialDateMonth)
        binding.txtToDateP.setText(finalDateMonth)

    }

    private fun init() {


        initComponents()
        setAdapter()
        callViolationsAPI()
        addSwipeToRefresh()


    }


    private fun addSwipeToRefresh() {


        binding.rvSwipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            fromRefresh = true
            callViolationsAPI()

        })

    }

    private fun initComponents() {
        arrListViolation = arrayListOf()
    }


    private fun setAdapter() {

        binding.rvViolation.layoutManager = LinearLayoutManager(mContext)

        violationAdapter = ViolationUpdateAdapter(arrListViolation, this, mContext!!)

        binding.rvViolation.adapter = violationAdapter


    }

    private fun callViolationsAPI() {
        addObserver()
        getViolationDetails()
    }

    private fun addObserver() {
        viewModel.violationResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->


                when (response) {
                    is Resource.Success -> {

                        binding.rvSwipeRefresh.isRefreshing = false
                        fromRefresh = false
                        binding.rvSwipeRefresh.visibility = View.VISIBLE

                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { violationResponse ->
                            val violationResponse = violationResponse
                            if (violationResponse != null) {

                                if (violationResponse.message!!.contains(
                                        mContext!!.resources.getString(
                                            R.string.no_record_found_txt
                                        )
                                    )
                                ) {
                                    binding.txtNoRecord.visibility = View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.GONE
                                } else {

                                    if (violationResponse.data != null && violationResponse.data.isNotEmpty()) {
                                        binding.txtNoRecord.visibility = View.GONE
                                        binding.rvSwipeRefresh.visibility = View.VISIBLE
                                        arrListViolation.clear()

                                        for (i in 0 until violationResponse.data.size) {
                                            if (violationResponse.data[i].permissionId > 0) {
                                                arrListViolation.addAll(listOf(violationResponse.data[i]))
                                                Log.e(
                                                    "arrListViolation",
                                                    arrListViolation.toString()
                                                )
                                            }
                                        }
                                        //   arrListViolation.addAll(violationResponse.data)
                                        binding.rvViolation.adapter?.notifyDataSetChanged()
                                        if (arrListViolation.size == 0) {
                                            binding.txtNoRecord.visibility = View.VISIBLE
                                            binding.rvSwipeRefresh.visibility = View.GONE
                                        } else {
                                            binding.txtNoRecord.visibility = View.GONE
                                            binding.rvSwipeRefresh.visibility = View.VISIBLE
                                        }
                                    }

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


    private fun getViolationDetails() {




        viewModel.getViolationData(
            mContext!!,
            UserShardPrefrences.getUserInfo(context).fKEmployeeId,
            binding.txtFromDateP.text.toString(),
            binding.txtToDateP.text.toString(),
            UserShardPrefrences.getLanguage(mContext)!!
        )






    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

              /*  bundle= FileUtils.loadBundle(mContext,"my_bundle")!!
            if (bundle!=null) {
                selectedTab = bundle.getInt("selected_tab", 0)
                violations = bundle.getParcelable("violations")
                binding.txtFromDate.text = bundle.getString("from_date") ?: ""
                binding.txtToDate.text = bundle.getString("to_date") ?: ""
                arrListViolation = bundle.getParcelableArrayList("violation_list") ?: arrayListOf()
                violationAdapter?.updateList(arrListViolation)
            }*/

    }

    override fun onPermissionItemClick(v: ViolationData, position: Int) {
        UserShardPrefrences.setSelfServicePos(activity, position)
        UserShardPrefrences.setCheckSelfService(activity, 0)

        GlobalVariables.updateRequest = true
        val requestsFragment = RequestsFragment()
        val args_violations = Bundle()
        args_violations.putInt("selected_tab", 2)
        args_violations.putParcelable("violations", v)
        requestsFragment.arguments = args_violations
        replaceFragment(requestsFragment, R.id.flFragment, false)
        saveState()

    }

    override fun onViolationSwipeItemClick(v: ViolationData, position: Int) {
        UserShardPrefrences.setSelfServicePos(activity, position)
        UserShardPrefrences.setCheckSelfService(activity, 0)


        GlobalVariables.updateRequest = true
        val requestsFragment = RequestsFragment()
        val args_violations = Bundle()
        args_violations.putInt("selected_tab", 2)
        args_violations.putParcelable("violations", v)
        requestsFragment.arguments = args_violations

        replaceFragment(requestsFragment, R.id.flFragment, false)
        saveState()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()

            R.id.rl_to_date_p -> {
                try {
                    val todate: String = binding.txtToDateP.getText().toString()
                    if (TextUtils.isEmpty(todate)) {
                        val calendar = Calendar.getInstance()
                        pickDate(
                            Calendar.DAY_OF_MONTH,
                            Calendar.MONTH,
                            Calendar.YEAR,
                            binding.txtToDateP
                        )
                    } else {
                        val dateString = todate.split("-").toTypedArray()
                        pickDate(
                            dateString[2].toInt(),
                            dateString[1].toInt(),
                            dateString[0].toInt(),
                            binding.txtToDateP
                        )
                    }
                } catch (e: Exception) {
                    e.message
                }
            }


            R.id.rl_from_date_p -> {
                try {
                    val date: String = binding.txtFromDateP.getText().toString()
                    if (TextUtils.isEmpty(date)) {
                        val calendar = Calendar.getInstance()
                        pickDate(
                            Calendar.DAY_OF_MONTH,
                            Calendar.MONTH,
                            Calendar.YEAR,
                            binding.txtFromDateP
                        )
                    } else {
                        val dateString = date.split("-").toTypedArray()
                        pickDate(
                            dateString[2].toInt(),
                            dateString[1].toInt(),
                            dateString[0].toInt(),
                            binding.txtFromDateP
                        )
                    }
                } catch (e: java.lang.Exception) {
                    e.message
                }

            }

            R.id.iv_search_icon -> {

                if (DateTime_Op.validateDates(
                        binding.txtFromDateP.text.toString(),
                        binding.txtToDateP.text.toString()
                    )
                ) {
                    callViolationsAPI()
                } else {
                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                        context,
                        mContext!!.resources.getString(R.string.date_validation),
                        R.drawable.caution,
                        resources.getColor(R.color.red)
                    )

                    //SnackBarUtil.showSnackbar(mContext,"Please make sure from date is less than or equal to to date.")
                }


            }
        }

    }


    fun pickDate(day: Int, month: Int, year: Int, textView: TextView) {
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"ViolationPendingFragment")
        datePickerFragment.show(parentFragmentManager, "datePicker")
    /*val now = Calendar.getInstance()
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
            },
            now[Calendar.YEAR],
            now[Calendar.MONTH],
            now[Calendar.DAY_OF_MONTH]
        )
        dpd.locale = Locale.ENGLISH
        dpd.setOkText("OK")
        dpd.setCancelText("Cancel")
        dpd.version = DatePickerDialog.Version.VERSION_2
        dpd.show(requireActivity().supportFragmentManager, "Datepickerdialog")*/

    }

}