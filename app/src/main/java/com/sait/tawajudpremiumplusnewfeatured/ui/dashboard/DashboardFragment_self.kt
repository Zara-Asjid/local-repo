package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentDashboardSelfBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.adapter.DashboardAdapter
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardData
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.viewmodels.DashboardViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import ir.mahozad.android.PieChart
import ir.mahozad.android.unit.Dimension
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class DashboardFragment_self : BaseFragment(), OnClickListener {
    private var _binding: FragmentDashboardSelfBinding? = null
    private val binding get() = _binding!!
    private lateinit var arrListDashboard: ArrayList<DashboardData>

    private lateinit var viewModel: DashboardViewModel
    private var dashboardAdapter: DashboardAdapter? = null
    private var mContext: Context? = null
    private var fromRefresh = false
    var handler: Handler = Handler()


    var months = arrayOf("Attendance", "Absent", "Leave", "Delay", "Holiday")
    var salary = intArrayOf(30, 20, 10, 15, 5)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardSelfBinding.inflate(inflater, container, false)
//setUpChartAnyChart()

        mContext = inflater.context
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]


        //   setUpHighChart()
        setDate()
        init()


        val activity = this.activity as MainActivity?

        // setTopbar(activity)

        setClickListeners(activity)

        return binding.root
    }

    private fun setDate() {
//        String currentDate = DateTimeOp.getCurrentDateTime("yyyy-MM-dd");
        val finalDateMonth: String = DateTime_Op.getFinalDateMonth("yyyy-MM-dd")

//        String previousMonthDate = DateTimeOp.getPreviousMonthDateTime("yyyy-MM-dd");
        val initialDateMonth: String = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")
        binding.txtFromDateS.text = initialDateMonth
        binding.txtToDateS.text = finalDateMonth

    }



    private fun init() {
        initComponents()
        callDashboardAPI()
        setAdapter()
        addSwipeToRefresh()
    }
    private fun initComponents() {
        arrListDashboard = arrayListOf()
    }


    private fun calculateRecyclerViewHeight(): Int {
        val totalHeight = dashboardAdapter!!.itemCount * resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._35sdp) // Assuming each item has a fixed height
        // You can modify this based on your item's actual height
        return totalHeight
    }

    // Set the calculated height to the RecyclerView's layout params
    private fun setRecyclerViewHeight() {
        val layoutParams = binding.rvDashboard.layoutParams
        layoutParams.height = calculateRecyclerViewHeight()
        binding.rvDashboard.layoutParams = layoutParams
    }

    private fun addSwipeToRefresh() {


/*
        binding.rvSwipeDashboardRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            fromRefresh = true
            callDashboardAPI()

        })
*/

    }
    private fun setAdapter() {

        binding.rvDashboard.layoutManager = LinearLayoutManager(mContext)

        dashboardAdapter = DashboardAdapter(arrListDashboard,mContext )

        binding.rvDashboard.adapter = dashboardAdapter

      //  setRecyclerViewHeight()

    }

    private fun callDashboardAPI() {
        addObserver()
        getDashboardDetails()
    }

    private fun getDashboardDetails() {


        val dashboardRequest = DashboardRequest(
            0, 0, 0, binding.txtFromDateS.text.toString(),
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            0, binding.txtToDateS.text.toString(),0,  UserShardPrefrences.getUserInfo(context).fKEmployeeId
        )


    /*    val dashboardRequest = DashboardRequest(
            0, 0, 0, "2022-02-01T06:14:59.084Z",
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            0, "2024-02-01T06:14:59.084Z",0, 23
        )*/

        viewModel.getDashboardData(
            mContext!!,
            dashboardRequest
        )
    }

    @SuppressLint("SuspiciousIndentation")
    private fun addObserver()  {
        var color : Int =0
        val piechart: PieChart = binding.pieChart
        var percentage = 0.0F
        arrListDashboard.clear()
        viewModel.dashboardResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                      //  binding.rvSwipeDashboardRefresh.isRefreshing = false
                        //fromRefresh = false
                        //binding.rvSwipeDashboardRefresh.visibility = View.VISIBLE
                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { dashBoardResponse ->
                            val dashboardResponse = dashBoardResponse
                            if (dashboardResponse != null) {


                                // arrListDashboard = arrayListOf()
                                // arrListDashboard.addAll(dashboardResponse.data)
                                val slicesList = mutableListOf<PieChart.Slice>()

                                  arrListDashboard.clear()
                              //  for (i in 0 until 4) {
                                  for (i in 0 until dashboardResponse.data.size) {
                                    arrListDashboard.addAll(listOf(dashboardResponse.data[i]))
                                }

                               // for (i in 0 until 4) {

                                for (i in 0 until arrListDashboard.size) {
                                    val currentDashboard = arrListDashboard[i]
                                    color = Color.parseColor(currentDashboard.colorCode)
                                    piechart.apply {

                                        if(currentDashboard.percentage!=0.00f){



/*
                                            slicesList.add(
                                                PieChart.Slice(
                                                     kotlin.math.round(currentDashboard.percentage / 1000),
                                                  //  0.02f,
                                                    color,
                                                    legend = currentDashboard.name,
                                                    label = currentDashboard.name
                                                ))
*/
                                            val df = DecimalFormat("#.###")


                                            if( LocaleHelper.arabicToEnglish(df.format(currentDashboard.percentage/100)).toFloat()>0.0) {
                                                slicesList.add(
                                                    PieChart.Slice(
                                                        LocaleHelper.arabicToEnglish(df.format(currentDashboard.percentage / 100))
                                                            .toFloat(),
                                                        //  0.02f,
                                                        color,
                                                        legend = currentDashboard.name,
                                                        label = currentDashboard.name
                                                    )
                                                )

                                              /*  Log.e(
                                                    "percent",
                                                    df.format(currentDashboard.percentage / 100)
                                                        .toFloat()
                                                        .toString()
                                                )*/
                                            }
                                        }


                                        slices = slicesList

                                        if(slicesList.size>0){
                                            binding.pieChart.visibility = View.VISIBLE
                                        }
                                        labelIconsPlacement = PieChart.IconPlacement.BOTTOM
                                        legendIconsMargin = Dimension.DP(2f)
                                       // legendsTitleColor = R.color.black
                                        legendsIcon = PieChart.DefaultIcons.SQUARE

                                    }

                                }




                                binding.rvDashboard.adapter?.notifyDataSetChanged()

                                // Do something with each item
                                // percentage += item.percentage.toFloat()








                                //Log.d("ArrayList", "addObserver: "+dashboardResponse.data)

                                //implementation of PieChart


                                //binding.rvViolation.adapter?.notifyDataSetChanged()


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

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.rlToDateS.setOnClickListener(this)
        binding.rlFromDateS.setOnClickListener(this)
        binding.ivSearchIcon.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.dashboard_txt))


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
        callDashboardAPI()

    }


    private fun setUpChartAnyChart() {


        /* val pie = AnyChart.pie()
         val dataEntries: MutableList<DataEntry> = ArrayList()

         for (i in months.indices) {
             dataEntries.add(ValueDataEntry(months[i], salary[i]))
         }


         pie.data(dataEntries)
 //        pie.title("Salary")
         pie.labels().position("outside");

         binding.anyChartView.setChart(pie)

 */

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> (activity as MainActivity).onBackPressed()

            R.id.rl_to_date_s -> {
                datePicker(binding.txtToDateS.text.toString(),binding.txtToDateS)
            }


            R.id.rl_from_date_s -> {
                datePicker(binding.txtFromDateS.text.toString(),binding.txtFromDateS)

            }
            R.id.iv_search_icon ->{

                if(DateTime_Op.validateDates(binding.txtFromDateS.text.toString(),binding.txtToDateS.text.toString())){
                    callDashboardAPI()
                }
                else{
                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,mContext!!.resources.getString(R.string.date_validation),R.drawable.caution,resources.getColor(R.color.red))

                  //  SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,mContext!!.resources.getString(R.string.date_validation),R.drawable.app_icon,colorPrimary)

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
                    txtDate                )
            }
        } catch (e: Exception) {
            e.message
        }

    }

    private fun pickDate(day: Int, month: Int, year: Int, textView: TextView) {
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"DashboardFragment")
        datePickerFragment.show(parentFragmentManager, "datePicker")
    /*  val now = Calendar.getInstance()
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
        dpd.locale= Locale.ENGLISH
        dpd.setOkText("OK")
        dpd.setCancelText("Cancel")
        dpd.version = DatePickerDialog.Version.VERSION_2
        dpd.show(requireActivity().supportFragmentManager, "Datepickerdialog")
*/
    }


}
