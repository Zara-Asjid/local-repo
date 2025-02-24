package com.sait.tawajudpremiumplusnewfeatured.ui.reports

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
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


import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentSelfReportsBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
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

class Reports_SelfFragment : BaseFragment(), View.OnClickListener, ReportSelfItemClickListener {
    private lateinit var binding: FragmentSelfReportsBinding
    private lateinit var viewModel: ReportsViewModel
    private var mContext: Context? = null
    private lateinit var arrListSelfReport: ArrayList<Reports_Self_Sevice_Data>
    private var violationAdapter: SelfReportAdapter? = null
    private var fromRefresh = false
    var handler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelfReportsBinding.inflate(inflater, container, false)
        mContext = inflater.context

        viewModel = ViewModelProvider(this)[ReportsViewModel::class.java]


        val activity = this.activity as MainActivity?


        binding.txtEmployeeName.text = UserShardPrefrences.getUserInfo(mContext).employeeName



        binding.txtEmployeeName.background =
            resources.getDrawable(R.drawable.edit_disable_text)
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        setClickListeners(activity)
        init()
        setDate()


        return binding.root
    }

    private fun init() {
        initComponents()
        callSelfReportAPI()
        setAdapter()
        addSwipeToRefresh()
    }

    private fun callSelfReportAPI() {

        addObserver()
        getSelfReportsDetails()
    }

    private fun addSwipeToRefresh() {

        binding.rvSwipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            fromRefresh = true
            callSelfReportAPI()
        })

    }

    private fun setAdapter() {

        binding.rvSelfReports.layoutManager = LinearLayoutManager(mContext)

        violationAdapter = SelfReportAdapter(arrListSelfReport, this, mContext)

        binding.rvSelfReports.adapter = violationAdapter


    }

    private fun getSelfReportsDetails() {
        val Self_ReportRequest = Reports_Self_Service_Request(
             UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext).toString(),
            UserShardPrefrences.getManagerId(mContext!!)!!.toInt()
        )


        viewModel.getReports_Self_ServiceData(
            mContext!!,
            Self_ReportRequest
        )
    }

    private fun initComponents() {
        arrListSelfReport = arrayListOf()
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

                                if(selfReportResponse.message!!.contains(mContext!!.resources.getString(R.string.no_record_found_txt))){
                                    binding.txtNoRecord.visibility= View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.GONE
                                }
                                //
                                else{
                                    arrListSelfReport.clear()
                                    for (i in 0 until selfReportResponse.data.size) {
                                        if(selfReportResponse.data[i].desc_En.contains("Self Service")||selfReportResponse.data[i].desc_Ar.contains("الخدمات الذاتية")){
                                            arrListSelfReport.addAll(listOf(selfReportResponse.data[i]))
                                        }
                                    }
                                  //  arrListSelfReport.addAll(selfReportResponse.data)
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
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.caution,resources.getColor(R.color.red))

                          //  SnackBarUtil.showSnackbar(context, message, false)

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
        binding.txtFromDateSr.setText(initialDateMonth)
        binding.txtToDateSr.setText(finalDateMonth)

    }

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.rlToDateSr.setOnClickListener(this)
        binding.rlFromDateSr.setOnClickListener(this)
        binding.ivSearchIcon.setOnClickListener(this)
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
        callSelfReportAPI()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()


            R.id.rl_to_date_sr -> {
                datePicker(binding.txtToDateSr.text.toString(), binding.txtToDateSr)
            }


            R.id.rl_from_date_sr-> {
                datePicker(binding.txtFromDateSr.text.toString(), binding.txtFromDateSr)

            }
            R.id.iv_search_icon -> {


                if(DateTime_Op.validateDates(binding.txtFromDateSr.text.toString(),binding.txtToDateSr.text.toString())) {
                    callSelfReportAPI()
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
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"Reports_SelfFragment")
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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("string_value", outState.toString())
    }
    override fun onReportItemClick( position: Int) {


        val targetFragment = PdfGenaratorFragment()
        val args_profile = Bundle()
        args_profile.putString("form_id", arrListSelfReport[position].formID.toString())
        args_profile.putString("from_date", binding.txtFromDateSr.text.toString())
        args_profile.putString("to_date", binding.txtToDateSr.text.toString())
        args_profile.putBoolean("isFromAdmin",false)
        args_profile.putInt("selected_tab",0)

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

}
