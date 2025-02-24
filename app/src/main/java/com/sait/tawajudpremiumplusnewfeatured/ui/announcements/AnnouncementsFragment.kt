package com.sait.tawajudpremiumplusnewfeatured.ui.announcements

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
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
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentAnnouncementsBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.adapter.AnnouncementAdapter
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementData
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.viewmodels.AnnouncementViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment



import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.util.*
import kotlin.collections.ArrayList

class AnnouncementsFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentAnnouncementsBinding
    private lateinit var viewModel: AnnouncementViewModel
    private var mContext: Context? = null

    var handler: Handler = Handler()

    private lateinit var arrListViolation: ArrayList<AnnouncementData>
    private var announcementAdapter: AnnouncementAdapter? = null
    private var fromRefresh = false
    var unreadCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnnouncementsBinding.inflate(inflater, container, false)
        mContext = inflater.context

        viewModel = ViewModelProvider(this)[AnnouncementViewModel::class.java]

        val activity = this.activity as MainActivity?

        setClickListeners(activity)

        setDate()

        init()




        return binding.root
    }


    private fun init() {


        initComponents()
        callAnnouncementsAPI()
        setAdapter()
        addSwipeToRefresh()


    }


    private fun addSwipeToRefresh() {


        binding.rvSwipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            fromRefresh = true
            callAnnouncementsAPI()

        })

    }

    private fun initComponents() {
        arrListViolation = arrayListOf()
    }


    private fun setAdapter() {

        binding.rvAnnouncements.layoutManager = LinearLayoutManager(mContext)

        announcementAdapter = mContext?.let { AnnouncementAdapter(arrListViolation, mContext!!) }

        binding.rvAnnouncements.adapter = announcementAdapter


        unreadCount  = announcementAdapter!!.getUnreadItemCount() ?: 0


       // toast(unreadCount.toString())

        UserShardPrefrences.saveCountAnnouncementsUnRead(requireActivity(),unreadCount.toString())

    }

    private fun callAnnouncementsAPI() {
        addObserver()
        getAnnouncementsDetails()
    }

    private fun addObserver() {
        viewModel.announcementResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        binding.rvSwipeRefresh.isRefreshing = false
                        fromRefresh = false
                        binding.rvSwipeRefresh.visibility = View.VISIBLE

                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { managerEmpResponse ->
                            val managerEmpResponse = managerEmpResponse
                            if (managerEmpResponse.data != null && managerEmpResponse.data.isNotEmpty()) {


                                if(managerEmpResponse.message!!.contains(mContext!!.resources.getString(R.string.no_record_found_txt))){
                                    binding.txtNoRecord.visibility= View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.GONE
                                }

                                else{
                                    binding.txtNoRecord.visibility= View.GONE
                                    binding.rvAnnouncements.visibility= View.VISIBLE
                                    binding.rvSwipeRefresh.visibility = View.VISIBLE
                                    arrListViolation.clear()
                                    arrListViolation.addAll(managerEmpResponse.data)
                                    binding.rvAnnouncements.adapter?.notifyDataSetChanged()
                                    setAdapter()

                                }

                            }
                            else{
                                binding.txtNoRecord.visibility= View.VISIBLE
                                binding.rvSwipeRefresh.visibility = View.GONE
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

    private fun getAnnouncementsDetails() {
        val managerEmpRequest = AnnouncementRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
          0
            ,binding.txtToDate.text.toString(),
            binding.txtFromDate.text.toString()
            )
        Log.e("commonRequest",managerEmpRequest.toString())

         viewModel.getAnnouncementData(mContext!!, managerEmpRequest)


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
        binding.imgSearch.setOnClickListener(this)
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
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.announcements))

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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back ->

            {

                //callAnnouncementsAPI()

                //replaceFragment(HomeFragment(), R.id.flFragment, false)
                 (activity as MainActivity).onBackPressed()

            }




            R.id.rl_to_date -> {
                datePicker(binding.txtToDate.text.toString(),binding.txtToDate)
            }


            R.id.rl_from_date -> {
                datePicker(binding.txtFromDate.text.toString(),binding.txtFromDate)

            }

            R.id.img_search -> {
                if(DateTime_Op.validateDates(binding.txtFromDate.text.toString(),binding.txtToDate.text.toString())){
                    callAnnouncementsAPI()
                }
                else{
                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,mContext!!.resources.getString(R.string.date_validation),R.drawable.caution,resources.getColor(R.color.red))

                    //SnackBarUtil.showSnackbar(mContext,"Please make sure from date is less than or equal to to date.")
                }
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
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"AnnouncementsFragment")
        datePickerFragment.show(parentFragmentManager, "datePicker")
       /* val now = Calendar.getInstance()
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


}
