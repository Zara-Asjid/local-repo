package com.sait.tawajudpremiumplusnewfeatured.ui.summary

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentSummaryBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.models.SummaryData
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.models.SummaryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.viewmodels.SummaryViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*


class SummaryFragment : BaseFragment(), OnSeekBarChangeListener, OnClickListener {
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    var handler: Handler = Handler()

    private lateinit var viewModel: SummaryViewModel
    private var mContext: Context? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSummaryBinding.inflate(inflater, container, false)

        mContext = inflater.context

        viewModel = ViewModelProvider(this)[SummaryViewModel::class.java]

        val activity = this.activity as MainActivity?
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        setDate()
        setClickListeners(activity)
        //getsDaysExceptWeekEndDays(binding.txtFromDate.text.toString(),binding.txtToDate.text.toString())
        callEmpAttendanceSummaryAPI()

        return binding.root
    }

    private fun settextOnseekbar(data: SummaryData?) {
        binding.totalAbsentText.text = data!!.absent.toString()
        binding.totalRemainPersonalPermText.text =
            data!!.remainingTimesPersonalPermission.toString()
        binding.totalMissinginTxt.text = data!!.missingIn.toString()
        binding.totalMissingoutTxt.text = data!!.missingOut.toString()
        binding.totalNotCompleteWorkhourTxt.text = data!!.notCompletionHalfDay.toString()

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun callEmpAttendanceSummaryAPI() {
        addObserver()
        getAttendanceSummaryDetails()
    }

    private fun getAttendanceSummaryDetails() {
        val emp_attanceSummary = SummaryRequest(
            UserShardPrefrences.getUserInfo(context).fKEmployeeId,
            binding.txtFromDate.text.toString(),
            binding.txtToDate.text.toString(), UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )
        viewModel.getSummaryData(
            mContext!!,
            emp_attanceSummary
        )
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun addObserver() {

        viewModel.summaryResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        /* binding.rvSwipeRefresh.isRefreshing = false
                         fromRefresh = false
                         binding.rvSwipeRefresh.visibility = View.VISIBLE*/

                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { summaryResponse ->
                            val summaryResponse = summaryResponse
                            if (summaryResponse != null) {


                                binding.txtTimeTotalDelay.text = summaryResponse.data.delay
                                binding.txtTimeTotalEarlyOut.text = summaryResponse.data.early_Out
                                binding.txtTimeTotalLostTime.text = summaryResponse.data.lostTime
                                binding.txtTimeTotalDelayAndEarlyout.text = summaryResponse.data.delay_Early_Out

                                settextOnseekbar(summaryResponse.data)


                                setSeekBarValues(
                                    summaryResponse.data,
                                    getsDaysExceptWeekEndDays(
                                        binding.txtFromDate.text.toString(),
                                        binding.txtToDate.text.toString()
                                    )
                                )

                                /*  val duration=  parseDuration(summaryResponse.data.delay)
                                     val localTime1 = LocalTime.parse(
                                         summaryResponse.data.delay,
                                         DateTimeFormatter.ofPattern("mm:ss")
                                     )
                                     val localTime2 = LocalTime.parse(
                                         summaryResponse.data.early_Out,
                                         DateTimeFormatter.ofPattern("HH:mm")
                                     )

                                     val totalDuration = Duration.ofMinutes(
                                         localTime1.hour.toLong() * 60 + localTime1.minute.toLong() +
                                                 localTime2.hour.toLong() * 60 + localTime2.minute.toLong()
                                     )
                                     // Extract total hours and minutes
                                     val totalHours = totalDuration.toHours()
                                     val totalMinutes = totalDuration.toMinutes() % 60
     */
                                // binding.txtTimeTotalDelayAndEarlyout.text = duration.toMinutes().toString()
                                //binding.rvViolation.adapter?.notifyDataSetChanged()

                            }
                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                           // SnackBarUtil.showSnackbar(context, message, false)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message ,R.drawable.caution,resources.getColor(R.color.red))


                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDuration(durationString: String): Duration {
        val components = durationString.split(":")
        if (components.size != 2) {
            throw IllegalArgumentException("Invalid duration format: $durationString")
        }

        val minutes = components[0].toLong()
        val seconds = components[1].toLong()

        return Duration.ofMinutes(minutes).plusSeconds(seconds)
    }

    private fun setDate() {
//        String currentDate = DateTimeOp.getCurrentDateTime("yyyy-MM-dd");
        val finalDateMonth: String = DateTime_Op.getFinalDateMonth("yyyy-MM-dd")

//        String previousMonthDate = DateTimeOp.getPreviousMonthDateTime("yyyy-MM-dd");
        val initialDateMonth: String = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")
        binding.txtFromDate.text = initialDateMonth
        binding.txtToDate.text = finalDateMonth

    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.summary_txt))

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


    @RequiresApi(Build.VERSION_CODES.N)
    private fun onRefresh() {
        callEmpAttendanceSummaryAPI()

    }

    private fun setClickListeners(activity: MainActivity?) {
        binding.seekBarAbsent.setOnSeekBarChangeListener(this)
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.imgSearch.setOnClickListener(this)
        binding.rlToDate.setOnClickListener(this)
        binding.rlFromDate.setOnClickListener(this)

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun setSeekBarValues(data: SummaryData, noOfDays: Int) {


        binding.seekBarAbsent.setProgress(data.absent, true)
        binding.seekBarRemainingTimeOfPersonalPermission.setProgress(
            data.remainingTimesPersonalPermission,
            true
        )
        binding.seekBarMissingIn.setProgress(data.missingIn, true)
        binding.seekBarTotalMissingOut.setProgress(data.missingOut, true)
        binding.seekBarNotCompleteWorkHours.setProgress(data.notCompletionHalfDay, true)


        /*  binding.seekBarAbsent.setProgress((data.absent), true)
          binding.seekBarAbsent.setProgress(data.remainingTimesPersonalPermission, true)
          binding.seekBarAbsent.setProgress((data.missingIn), true)
          binding.seekBarAbsent.setProgress((data.missingOut), true)
          binding.seekBarAbsent.setProgress((data.notCompletionHalfDay), true)

  */

        /* binding.seekBarAbsent.setProgress(((data.absent * 100) / noOfDays), true)
         binding.seekBarAbsent.setProgress(data.remainingTimesPersonalPermission, true)
         binding.seekBarAbsent.setProgress(((data.missingIn * 100) / noOfDays), true)
         binding.seekBarAbsent.setProgress(((data.missingOut * 100) / noOfDays), true)
         binding.seekBarAbsent.setProgress(((data.notCompletionHalfDay * 100) / noOfDays), true)

 */

/* binding.seekBarAbsent.setProgress(((data.absent.toDouble() * 100) / noOfDays).toInt(), true)
   binding.seekBarRemainingTimeOfPersonalPermission.setProgress((data.remainingTimesPersonalPermission.toDouble()  / noOfDays).toInt() , true)
   binding.seekBarMissingIn.setProgress(((data.missingIn.toDouble() * 100) / noOfDays).toInt(), true)
   binding.seekBarTotalMissingOut.setProgress(((data.missingOut.toDouble() * 100) / noOfDays).toInt(), true)
   binding.seekBarNotCompleteWorkHours.setProgress(((data.notCompletionHalfDay.toDouble() * 100) / noOfDays).toInt(), true)
*/
        binding.seekBarAbsent.isEnabled = false
        binding.seekBarRemainingTimeOfPersonalPermission.isEnabled = false
        binding.seekBarMissingIn.isEnabled = false
        binding.seekBarTotalMissingOut.isEnabled = false
        binding.seekBarNotCompleteWorkHours.isEnabled = false
        // say_minutes_left(80)

        /*  binding.seekBarAbsent.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
              override fun onStopTrackingTouch(seekBar: SeekBar) {
                  Toast.makeText(activity, "onStopTrackingTouch ner", Toast.LENGTH_SHORT).show()
              }

              override fun onStartTrackingTouch(seekBar: SeekBar) {
                  Toast.makeText(activity, "onStartTrackingTouch ner", Toast.LENGTH_SHORT).show()
              }

              override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                  // TODO Auto-generated method stub

                  Toast.makeText(activity, "onProgressChanged ner", Toast.LENGTH_SHORT).show()
              }
          })*/

    }

    private fun say_minutes_left(how_many: Int) {


        val what_to_say = how_many.toString()
        binding.totalAbsentText.text = what_to_say

        val left: Int = binding.seekBarAbsent.left + binding.seekBarAbsent.paddingLeft
        val right: Int = binding.seekBarAbsent.right - binding.seekBarAbsent.paddingRight


        val seek_label_pos: Int = (((right - left) *
                binding.seekBarAbsent.progress) /
                binding.seekBarAbsent.max) + left
        binding.totalAbsentText.x = (seek_label_pos - binding.totalAbsentText.width / 2).toFloat()

    }

    private fun getsDaysExceptWeekEndDays(fromDate: String, toDate: String): Int {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")

        val date1: Date = df.parse(fromDate)
        val date2: Date = df.parse(toDate)
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2

        var numberOfDays = 0
        while (cal1.before(cal2)) {
            if (Calendar.SATURDAY != cal1[Calendar.DAY_OF_WEEK] && Calendar.SUNDAY != cal1[Calendar.DAY_OF_WEEK]) {
                numberOfDays++
            }
            cal1.add(Calendar.DATE, 1)
        }
        return numberOfDays
    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // Toast.makeText(activity, "onProgressChanged", Toast.LENGTH_SHORT).show()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

        Toast.makeText(activity, "onStartTrackingTouch", Toast.LENGTH_SHORT).show()

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        Toast.makeText(activity, "onStopTrackingTouch", Toast.LENGTH_SHORT).show()


    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()


            R.id.rl_to_date -> {
                datePicker(binding.txtToDate.text.toString(), binding.txtToDate)
            }


            R.id.rl_from_date -> {
                datePicker(binding.txtFromDate.text.toString(), binding.txtFromDate)

            }
            R.id.img_search -> {

                if(DateTime_Op.validateDates(binding.txtFromDate.text.toString(),binding.txtToDate.text.toString())) {
                    callEmpAttendanceSummaryAPI()
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
        val datePickerFragment = DatePickerFragment.newInstance(year, month, day,textView.id,"SummaryFragment")
        datePickerFragment.show(parentFragmentManager, "datePicker")
    /*  val now = Calendar.getInstance()
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
                   // textView.text = String.format(Locale("ar"), "%04d-%02d-%02d", yearNew, monthnew, daynew)
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


