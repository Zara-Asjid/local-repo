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
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentViolationsBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.RequestsFragment

import com.sait.tawajudpremiumplusnewfeatured.ui.violations.adapter.ViolationAdapter
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.listener.ViolationItemClickListener
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationData
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.viewmodels.ViolationViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.util.*
import kotlin.collections.ArrayList

class ViolationsFragment : BaseFragment(), ViolationItemClickListener, OnClickListener {
    private var _binding: FragmentViolationsBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: ViolationViewModel
    private var mContext: Context? = null

    private lateinit var arrListViolation: ArrayList<ViolationData>
    private var violationAdapter: ViolationAdapter? = null
    private var fromRefresh = false
    var handler: Handler = Handler()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentViolationsBinding.inflate(inflater, container, false)

        mContext = inflater.context

        viewModel = ViewModelProvider(this).get(ViolationViewModel::class.java)


        setDate()
        setClickListeners()
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        // callViolationsAPI()
        init()
        return binding.root
    }

    private fun setClickListeners() {
        binding.rlFromDate.setOnClickListener(this)
        binding.rlToDate.setOnClickListener(this)

        binding.ivSearchIcon.setOnClickListener(this)
    }

    private fun setDate() {
//        String currentDate = DateTimeOp.getCurrentDateTime("yyyy-MM-dd");

          val finalDateMonth: String = DateTime_Op.getFinalDateMonth("yyyy-MM-dd")

//        String previousMonthDate = DateTimeOp.getPreviousMonthDateTime("yyyy-MM-dd");

        val initialDateMonth: String = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")
        binding.txtFromDate.text = initialDateMonth
        binding.txtToDate.text = finalDateMonth

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

        violationAdapter = ViolationAdapter(arrListViolation, this, mContext!!)

        binding.rvViolation.adapter = violationAdapter


    }

    private fun callViolationsAPI() {
        addObserver()
        getViolationDetails()
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

                                if(violationResponse.message!!.contains(mContext!!.resources.getString(R.string.no_record_found_txt))){
                                    binding.txtNoRecord.visibility= View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.GONE
                                }
                                else{

                                    if (violationResponse.data != null && violationResponse.data.isNotEmpty()) {
                                        binding.txtNoRecord.visibility = View.GONE
                                        binding.rvSwipeRefresh.visibility = View.VISIBLE
                                        arrListViolation.clear()

                                        for (i in 0 until violationResponse.data.size) {
                                            if (violationResponse.data[i].permissionId == 0) {
                                                arrListViolation.addAll(listOf(violationResponse.data[i]))
                                                Log.e("arrListViolation", arrListViolation.toString())

                                            }
                                        }

                                        Log.e("arrListViolation", arrListViolation.toString())

                                        //   arrListViolation.addAll(violationResponse.data)
                                        binding.rvViolation.adapter?.notifyDataSetChanged()


                                        if(arrListViolation.size==0){

                                            binding.txtNoRecord.visibility= View.VISIBLE
                                            binding.rvSwipeRefresh.visibility = View.GONE
                                        }
                                        else{
                                            binding.txtNoRecord.visibility= View.GONE
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

    private fun getViolationDetails() {
        /*  viewModel.getViolationData(mContext!!,
              UserShardPrefrences.getUserInfo(context).fKEmployeeId,
              from_date!!,
              to_date!!,UserShardPrefrences.getLanguage(mContext)!!)*/



        viewModel.getViolationData(
            mContext!!,
           UserShardPrefrences.getUserInfo(context).fKEmployeeId,
            binding.txtFromDate.text.toString(),
            binding.txtToDate.text.toString(),
             UserShardPrefrences.getLanguage(mContext)!!
        )

    }

    override fun onPermissionItemClick(v: ViolationData, position: Int) {
        UserShardPrefrences.setSelfServicePos(activity, position)
        UserShardPrefrences.setCheckSelfService(activity, 0)

        GlobalVariables.updateRequest = true
        val requestsFragment = RequestsFragment()
        val args_violations = Bundle()
        args_violations.putParcelable("violations", v)
        requestsFragment.arguments = args_violations
        replaceFragment(requestsFragment, R.id.flFragment, false)
    }


    override fun onViolationSwipeItemClick(v: ViolationData, position: Int) {

        UserShardPrefrences.setSelfServicePos(activity, position)
        UserShardPrefrences.setCheckSelfService(activity, 0)

        //SnackBarUtil.showSnackbar(context, "Swipe click", true)


        GlobalVariables.updateRequest = true
       val requestsFragment = RequestsFragment()
        val args_violations = Bundle()
        args_violations.putInt("selected_tab",1)

        args_violations.putParcelable("violations", v)
        requestsFragment.arguments = args_violations
        replaceFragment(requestsFragment, R.id.flFragment, false)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()

            R.id.rl_to_date -> {
                try {
                    val todate: String = binding.txtToDate.text.toString()
                    if (TextUtils.isEmpty(todate)) {
                        val calendar = Calendar.getInstance()
                        pickDate(
                            Calendar.DAY_OF_MONTH,
                            Calendar.MONTH,
                            Calendar.YEAR,
                            binding.txtToDate
                        )
                    } else {
                        val dateString = todate.split("-").toTypedArray()
                        pickDate(
                            dateString[2].toInt(),
                            dateString[1].toInt(),
                            dateString[0].toInt(),
                            binding.txtToDate
                        )
                    }
                } catch (e: Exception) {
                    e.message
                }
            }


            R.id.rl_from_date -> {
                try {
                    val date: String = binding.txtFromDate.text.toString()
                    if (TextUtils.isEmpty(date)) {
                        val calendar = Calendar.getInstance()
                        pickDate(
                            Calendar.DAY_OF_MONTH,
                            Calendar.MONTH,
                            Calendar.YEAR,
                            binding.txtFromDate
                        )
                    } else {
                        val dateString = date.split("-").toTypedArray()
                        pickDate(
                            dateString[2].toInt(),
                            dateString[1].toInt(),
                            dateString[0].toInt(),
                            binding.txtFromDate
                        )
                    }
                } catch (e: java.lang.Exception) {
                    e.message
                }

            }
            R.id.iv_search_icon -> {

                if(DateTime_Op.validateDates(binding.txtFromDate.text.toString(),binding.txtToDate.text.toString())){
                    callViolationsAPI()
                }
                else{
                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,mContext!!.resources.getString(R.string.date_validation),R.drawable.caution,resources.getColor(R.color.red))

                    //SnackBarUtil.showSnackbar(mContext,"Please make sure from date is less than or equal to to date.")
                }


            }


        }

    }

    fun pickDate(day: Int, month: Int, year: Int, textView: TextView) {
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"ViolationsFragment")
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

}