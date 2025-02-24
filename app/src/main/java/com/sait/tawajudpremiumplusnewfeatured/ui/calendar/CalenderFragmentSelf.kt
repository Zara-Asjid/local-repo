package com.sait.tawajudpremiumplusnewfeatured.ui.calendar


import android.annotation.SuppressLint
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
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentCalenderBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models.CalendarData
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models.CalendarRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.models.CalendarResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.viewmodels.CalendarViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.DatePickerFragment
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.DateFormatSymbols
import java.util.*


class CalendarFragmentSelf : BaseFragment(), View.OnClickListener,OnCalendarPageChangeListener {
    private lateinit var binding: FragmentCalenderBinding

    var handler: Handler = Handler()

    private lateinit var viewModel: CalendarViewModel
    private var mContext: Context? = null
    private var events: List<EventDay>? = null
    var summaryGetText = ""
    private var mapLoadedMonths: HashMap<Int, Int>? = null
    var day :Int?=null
    var firstdayOfMonth  : Int? =null
    var lastdayOfMonth  : Int? =null
    var formatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
    var forwardbtnClick :Boolean =true
    var backbtnClick :Boolean =true
    var updatedDate : String? =null
    var click :Int = 0
    private var attendanceSummaries: List<CalendarData> =
        ArrayList<CalendarData>()
    val list = mutableListOf<CalendarDay>()
    var currentPageDate: Calendar? = null
    var year: Int? = null
    var month: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalenderBinding.inflate(inflater, container, false)
        val activity = this.activity as MainActivity?
        mContext = inflater.context

        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        setClickListeners(activity)


        binding.calendarView.layoutDirection = View.LAYOUT_DIRECTION_LTR;




        return binding.root
    }


    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.txtFromDate.setOnClickListener(this)
        binding.txtToDate.setOnClickListener(this)
        binding.ivSearchIcon.setOnClickListener(this)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvEventNursing.isSelected = true
        binding.tvEventStudyPermission.isSelected = true
        setDate()
        binding.rlFromDate.setOnClickListener(this)
        binding.rlToDate.setOnClickListener(this)
        binding.calendarView.setOnForwardPageChangeListener(this)
        binding.calendarView.setOnPreviousPageChangeListener(this)
        binding.llEvents.visibility = View.GONE
        events = ArrayList<EventDay>()
        (events as ArrayList<EventDay>).clear()

        binding.calendarView.setCalendarDayLayout(R.layout.calendar_cell)



//        calendarView.setVisibility(View.GONE);
//        calendarView.setVisibility(View.VISIBLE);
        try {
            val calendar_instance = Calendar.getInstance()
            calendar_instance.add(Calendar.MONTH, 0)
            binding.calendarView.setDate(calendar_instance)
            callCalenderAPI(calendar_instance)

        } catch (e: OutOfDateRangeException) {
            e.printStackTrace()
        }

        binding.calendarView!!.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedDayCalendar = eventDay.calendar
                var totalDiff =0
                var index = 0
                var hasBackgroundImage =
                    false // Initialize boolean variable to track background image presence
                val calendar = clickedDayCalendar
                val dayofClickEventDay = calendar.get(Calendar.DAY_OF_MONTH)
                val monthofClickEventDay = calendar.get(Calendar.MONTH) + 1 // Months in Calendar are 0-based
                val yearofClickEventDay = calendar.get(Calendar.YEAR)
                val localDateTime = LocalDateTime(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay, 0, 0)

// Formatting the LocalDateTime object
                val formatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
                val clickedDate = formatter.print(localDateTime)
                //val clickedDate: String = String.format("%04d-%02d-%02d", yearofClickEventDay, monthofClickEventDay, dayofClickEventDay)
                attendanceSummaries.forEachIndexed { indexOfEventDay ,it ->
                    /*            val summary: CalendarData =
                                    attendanceSummaries[i]*/
                    if (UserShardPrefrences.getLanguage(context).equals("0")) {
                        summaryGetText = it.textEn
                    } else {
                        summaryGetText = it.textAr
                    }
                    val str_fromDate: String = it.fromDate
                    var str_toDate: String = it.toDate
                    //DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
                    var from_date: LocalDate? = null
                    var to_date: LocalDate? = null
                    if (str_fromDate != null) {
                        if (str_toDate == null) {
                            str_toDate = str_fromDate
                        }
                        from_date = org.joda.time.LocalDate.parse(str_fromDate, formatter)
                        to_date = org.joda.time.LocalDate.parse(str_toDate, formatter)
                        var textEn = ""
                        var strTemp = ""
                        /*totalDiff = Days.daysBetween(from_date, from_date).days
                        for ( j in 0 .. totalDiff){
                            if (totalDiff >= 0 ){
                                if (LocalDate.parse(clickedDate, formatter).equals(from_date)){
                                    from_date=from_date
                                    break
                                }else{
                                    from_date=from_date!!.plusDays(1)

                                }

                            }
                        }*/
                        when (it.apptType) {
                            "1" -> {
                                binding.tvEvents.setTextColor(context!!.resources.getColor(R.color.event_Leaves))

                                if (checkIfLiesInFromAndToDates(
                                        from_date!!.toDate(),
                                        to_date.toDate(),
                                        clickedDayCalendar.time,
                                    )
                                    && isBackgroundSetForDate(
                                        list,
                                        LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),
                                        EventDay(clickedDayCalendar,R.drawable.leave_cal),
                                    )
                                    && it.fromDate != null && it.toDate != null &&
                                    LocalDate.parse(clickedDate, formatter).equals(from_date)


                                ) {
                                    textEn = (summaryGetText + ": " + LocalDate.parse(
                                        it.fromDate,
                                        formatter
                                    )).toString() + " - " + LocalDate.parse(
                                        it.toDate,
                                        formatter
                                    )
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        strTemp + textEn,
                                        R.drawable.app_icon,
                                        colorPrimary
                                    )
                                    true
                                    return@forEachIndexed

                                } else {
                                    false
                                }

                                   if (checkIfLiesInFromAndToDates(
                                           from_date!!.toDate(),
                                           to_date.toDate(),
                                           clickedDayCalendar.time,
                                       )
                                       && isBackgroundSetForDate(
                                           list,
                                           LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),
                                           EventDay(clickedDayCalendar,R.drawable.leave_cal),
                                       )
                                       && it.fromDate == null && it.toDate == null&&
                                       LocalDate.parse(clickedDate, formatter).equals(from_date)
                                   ) {
                                       textEn = ("$summaryGetText: " + LocalDate.parse(
                                           it.fromDate,
                                           formatter
                                       )).toString() + " - " + LocalDate.parse(
                                           it.toDate,
                                           formatter
                                       )
                                       SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                           context,
                                           strTemp + textEn,
                                           R.drawable.app_icon,
                                           colorPrimary
                                       )
                                       true

                                   } else {
                                       false
                                   }

                                   if (checkIfLiesInFromAndToDates(
                                           from_date!!.toDate(),
                                           to_date.toDate(),
                                           clickedDayCalendar.time,
                                       )
                                       && isBackgroundSetForDate(
                                           list,
                                           LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),
                                           EventDay(clickedDayCalendar,R.drawable.leave_cal),
                                       )
                                       && it.fromTime == null
                                       &&
                                       LocalDate.parse(clickedDate, formatter).equals(from_date)
                                   ) {
                                       textEn =
                                           summaryGetText + ": " + " " + " - " + LocalDate.parse(
                                               it.toDate,
                                               formatter
                                           )
                                       SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                           context,
                                           strTemp + textEn,
                                           R.drawable.app_icon,
                                           colorPrimary
                                       )
                                       true

                                   } else {
                                       false
                                   }
                                   if (checkIfLiesInFromAndToDates(
                                           from_date!!.toDate(),
                                           to_date.toDate(),
                                           clickedDayCalendar.time,
                                       )
                                       && isBackgroundSetForDate(
                                           list,
                                           LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),
                                           EventDay(clickedDayCalendar,R.drawable.leave_cal),
                                       )
                                       && it.toDate == null &&
                                       LocalDate.parse(clickedDate, formatter).equals(from_date)
                                   ) {
                                       textEn = (summaryGetText + ": " + LocalDate.parse(
                                           it.fromDate,
                                           formatter
                                       )).toString() + " - " + " "
                                       com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                           context,
                                           strTemp + textEn,
                                           com.sait.tawajudpremiumplusnewfeatured.R.drawable.app_icon,
                                           com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
                                       )
                                       true

                                   } else {
                                       false
                                   }

                            }


                            "2" -> {
                                binding.tvEvents.setTextColor(context!!.resources.getColor(R.color.event_Leaves))
                                if(checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                    && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),   EventDay(clickedDayCalendar,R.drawable.permission_cal))
                                    &&
                                    LocalDate.parse(clickedDate, formatter).equals(from_date)) {
                                    if(it.fromTime.isNotEmpty()&& it.toTime.isNotEmpty()) {
                                        textEn =
                                            ((summaryGetText + " (" + it.fromTime).toString() + " - " + it.toTime).toString() + ")"
                                    }else{
                                        textEn =
                                            (summaryGetText )
                                    }

                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        strTemp + textEn,
                                        R.drawable.app_icon,
                                        colorPrimary
                                    )
                                    true
                                    return@forEachIndexed
                                    // Terminate the loop

                                }
                                else{
                                    false
                                }
                            }

                            "5", "6" -> {
                                binding.tvEvents.setTextColor(context!!.resources.getColor(R.color.event_holiday))
                                if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                    && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(clickedDayCalendar,R.drawable.holiday_cal))
                                    && it.fromDate != null && it.toDate != null &&
                                    LocalDate.parse(clickedDate, formatter).equals(from_date)) {
                                    textEn = (summaryGetText + ": " + LocalDate.parse(
                                        it.fromDate,
                                        formatter
                                    )).toString() + " - " + LocalDate.parse(
                                        it.toDate,
                                        formatter
                                    )
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        strTemp + textEn,
                                        R.drawable.app_icon,
                                        colorPrimary
                                    )
                                    true
                                }
                                else{
                                    false
                                }
                                      if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                          && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(clickedDayCalendar,R.drawable.holiday_cal))
                                          && it.fromDate == null && it.toDate == null&&
                                          LocalDate.parse(clickedDate, formatter).equals(from_date)) {
                                              textEn = (summaryGetText + ": " + LocalDate.parse(
                                                  it.fromDate,
                                                  formatter
                                              )).toString() + " - " + LocalDate.parse(
                                                  it.toDate,
                                                  formatter
                                              )
                                          SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                              context,
                                              strTemp + textEn,
                                              R.drawable.app_icon,
                                              colorPrimary
                                          )
                                          true
                                          }
                                      else{
                                          false
                                      }
                                      if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                          && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(clickedDayCalendar,R.drawable.holiday_cal))
                                          && it.fromTime == null&&
                                          LocalDate.parse(clickedDate, formatter).equals(from_date)) {
                                              textEn =
                                                  summaryGetText + ": " + " " + " - " + LocalDate.parse(
                                                      it.toDate,
                                                      formatter
                                                  )
                                          SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                              context,
                                              strTemp + textEn,
                                              R.drawable.app_icon,
                                              colorPrimary
                                          )
                                          true
                                      }
                                      else{
                                          false
                                      }
                                      if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                          && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(clickedDayCalendar,R.drawable.holiday_cal))
                                          &&it.toDate == null&&
                                          LocalDate.parse(clickedDate, formatter).equals(from_date)) {
                                              textEn = (summaryGetText + ": " + LocalDate.parse(
                                                  it.fromDate,
                                                  formatter
                                              )).toString() + " - " + " "
                                          SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                              context,
                                              strTemp + textEn,
                                              R.drawable.app_icon,
                                              colorPrimary
                                          )
                                          true
                                      }
                                      else{
                                          false
                                      }

                            }

                            "7" -> {
                                binding.tvEvents.setTextColor(context!!.resources.getColor(R.color.event_absent))
                                if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                    && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(clickedDayCalendar,R.drawable.abcent_cal))
                                    && LocalDate.parse(clickedDate, formatter).equals(from_date))
                                {
                                    textEn = summaryGetText
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        strTemp + textEn,
                                        R.drawable.app_icon,
                                        colorPrimary
                                    )
                                    true
                                    return@forEachIndexed
                                }
                                else{
                                    false
                                }
                            }

                            "3","15", "14", "9", "8", "10" -> {
                                binding.tvEvents.setTextColor(context!!.resources.getColor(R.color.event_violations))
                                if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                    && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(clickedDayCalendar,R.drawable.violations_cal))
                                    && LocalDate.parse(clickedDate, formatter).equals(from_date) ){
                                    if(it.fromTime.isNotEmpty()&& it.toTime.isNotEmpty()){
                                        textEn =
                                            (((summaryGetText + ": " + " [" + it.fromTime).toString() + " - " + it.toTime).toString() + "]" + " (" + it.duration).toString() + ")"
                                    }else{
                                        textEn =
                                            (((summaryGetText + ": " + " (" + LocalDate.parse(
                                                it.fromDate,
                                                formatter
                                            ) + ")")))
                                    }
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        strTemp + textEn,
                                        R.drawable.app_icon,
                                        colorPrimary
                                    )
                                    true
                                    return@forEachIndexed

                                }
                                else{
                                    false
                                }
                            }


                            "11" -> {
                                binding.tvEvents.setTextColor(context!!.resources.getColor(R.color.event_rest_day))
                                if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                    && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(clickedDayCalendar,R.drawable.rest_cal))
                                    && LocalDate.parse(clickedDate, formatter).equals(from_date))
                                {
                                    textEn = (summaryGetText + ": " + LocalDate.parse(
                                        it.fromDate,
                                        formatter
                                    )).toString() + " - " + LocalDate.parse(
                                        it.toDate,
                                        formatter
                                    )
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        strTemp + textEn,
                                        R.drawable.app_icon,
                                        colorPrimary
                                    )
                                    true
                                    return@forEachIndexed
                                }
                                else{
                                    false
                                }
                            }


                            "13" -> {


                                if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                    && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(clickedDayCalendar,R.drawable.permission_pending_cal))
                                    && it.fromDate != null
                                    && LocalDate.parse(clickedDate, formatter).equals(from_date)) {
                                    textEn = (summaryGetText + ": " + LocalDate.parse(
                                        it.fromDate,
                                        formatter
                                    )).toString()
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        strTemp + textEn,
                                        R.drawable.app_icon,
                                        colorPrimary
                                    )
                                    true
                                    return@forEachIndexed

                                }
                                else{
                                    false
                                }
                            }


                            "12" -> {

                                if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                    && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(clickedDayCalendar,R.drawable.leave_p_cal))
                                    &&it.fromDate != null
                                    && LocalDate.parse(clickedDate, formatter).equals(from_date)) {
                                    textEn = (summaryGetText + ": " + LocalDate.parse(
                                        it.fromDate,
                                        formatter
                                    )).toString() + " - " + LocalDate.parse(
                                        it.toDate,
                                        formatter
                                    )
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        strTemp + textEn,
                                        R.drawable.app_icon,
                                        colorPrimary
                                    )
                                    true
                                    return@forEachIndexed
                                }
                                else{
                                    false
                                }

                            }
                            "16"->{
                                if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                    && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(eventDay.calendar,R.drawable.work_home_cal)) &&
                                    it.fromDate != null
                                    && LocalDate.parse(clickedDate, formatter).equals(from_date)) {
                                    textEn = (summaryGetText + ": " + LocalDate.parse(
                                        it.fromDate,
                                        formatter
                                    )).toString()
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        strTemp + textEn,
                                        R.drawable.app_icon,
                                        colorPrimary
                                    )
                                    true
                                }
                                else{
                                    false
                                }
                            }
                            "17"->{
                                if (checkIfLiesInFromAndToDates(from_date!!.toDate(), to_date.toDate(), clickedDayCalendar.time)
                                    && isBackgroundSetForDate(list, LocalDate(yearofClickEventDay, monthofClickEventDay, dayofClickEventDay),EventDay(clickedDayCalendar,R.drawable.work_home_pending_cal)) &&
                                    it.fromDate != null) {
                                    textEn = (summaryGetText + ": " + LocalDate.parse(
                                        it.fromDate,
                                        formatter
                                    )).toString()
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                        context,
                                        strTemp + textEn,
                                        R.drawable.app_icon,
                                        colorPrimary
                                    )
                                    true
                                }
                                else{
                                    false
                                }
                            }
                            else -> {
                                if (index == 1) {
                                    strTemp = binding.tvEvents.getText().toString() + "\n"
                                    index = 0
                                }
                                //                            tv_events.setText(strTemp + textEn);

                                false
                            }

                        }


                    }
                }
            }
        })

    }

    private fun getCurrentYear(): Int? {
        currentPageDate = binding.calendarView!!.currentPageDate
        year = currentPageDate!![Calendar.YEAR]
        return year
    }

    private fun getCurrentMonth(): Int? {
        currentPageDate = binding.calendarView!!.currentPageDate
        month = currentPageDate!![Calendar.MONTH] + 1
        return month
    }
    private fun getMonthFromDate() : Int{
        val parts = binding.txtFromDate.text.toString().split("-")
        val year = parts[0].toInt()
        val month = parts[1].toInt() // Adjust for zero-based month indexing
        val day = parts[2].toInt()
        return month
    }
    private fun getYearFromDate() : Int{
        val parts = binding.txtFromDate.text.toString().split("-")
        val year = parts[0].toInt()
        return year
    }
    private fun getYearFromToDate() : Int{
        val parts = binding.txtToDate.text.toString().split("-")
        val year = parts[0].toInt()
        return year
    }
    private fun getCurrentDate() :String?{
        month=Calendar.getInstance()[Calendar.MONTH]+1
        year =Calendar.getInstance()[Calendar.YEAR]
        day =Calendar.getInstance()[Calendar.DATE]
        return  "$year-${if (month!! < 10) "0$month" else month}-${if (day!! < 10) "0$day" else day}"
    }


    private fun callCalenderAPI(instance : Calendar) {


        addObserver(instance)
        postCalenderDetails()


    }
    fun getMonthNameInArabic(calendar: Calendar): String {
        // Set the locale to Arabic
        val arabicLocale = Locale("ar")
        val monthIndex = calendar.get(Calendar.MONTH) // Get the month index (0-based)
        val monthNames = DateFormatSymbols(arabicLocale).months
        return monthNames[monthIndex] // Return the Arabic month name
    }

    @SuppressLint("SuspiciousIndentation")
    private fun addObserver(instance: Calendar) {
        viewModel.calendarResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse

                            if (requestResponse != null) {


                                if (requestResponse.message!!.contains(
                                        mContext!!.resources.getString(
                                            R.string.response_no_record_found_txt
                                        )
                                    )
                                ) {

                                } else {


                                    if (requestResponse.data != null) {

                                        val employeeCalendarSummary: CalendarResponse =
                                            requestResponse

                                        attendanceSummaries =
                                            requestResponse.data

                                        val instance = Calendar.getInstance()

                                        //Load last Six Months Data Only: Remove Happy New Year Days
                                        val lastSixMonthsSummary = mutableListOf<CalendarData>()
                                        val addedDates = mutableSetOf<LocalDate>()
                                        addedDates.clear()
                                        for (employeeCalendarItem in attendanceSummaries) {
                                            if (employeeCalendarItem.apptType != null && employeeCalendarItem.apptType
                                                    .equals("5")
                                            ) {
                                                continue
                                            }
                                            val str_fromDate: String =
                                                employeeCalendarItem.fromDate
                                            var str_toDate: String = employeeCalendarItem.toDate
                                            var from_date: LocalDate?
                                            var to_date: LocalDate?
                                            var totalDiff: Int
                                            if (str_fromDate != null) {
                                                if (str_toDate == null) {
                                                    str_toDate = str_fromDate
                                                }
                                                from_date = LocalDate.parse(str_fromDate, formatter)
                                                to_date = LocalDate.parse(str_toDate, formatter)
                                                totalDiff = Days.daysBetween(from_date, to_date).days
                                                if (from_date !in addedDates) {
                                                    //addedDates.add(from_date)
                                                    //lastSixMonthsSummary.add(employeeCalendarItem)
                                                    var j = 0
                                                    while (j <= totalDiff) {
                                                        if (totalDiff >= 0 && !addedDates.contains(from_date)) {
                                                            val newItem = employeeCalendarItem.copy(
                                                                days = 0, duration = 0, flexibilePermissionDuration = 0,
                                                                fromDate = (from_date?.toString() + "T00:00:00") ?: "",
                                                                fromTime = if(employeeCalendarItem.fromTime != null && employeeCalendarItem.fromTime.isNotEmpty()){
                                                                    employeeCalendarItem.fromTime
                                                                }
                                                                else{" "}, isFlexible = false, toTime =if(employeeCalendarItem.toTime != null && employeeCalendarItem.toTime.isNotEmpty()){ employeeCalendarItem.toTime } else{""}
                                                            )
                                                            lastSixMonthsSummary.add(newItem)
                                                            addedDates.add(from_date!!)
                                                        } else {
                                                            if (j > 0 && !from_date!!.equals(to_date)) { // Check j is greater than 0 to avoid negative values
                                                                from_date = from_date!!.plusDays(1)
                                                                j--
                                                            }else
                                                            {
                                                                break
                                                            }
                                                        }
                                                        j++
                                                    }
                                                /*    for ( j in 0 ..  totalDiff){
                                                    if (totalDiff >= 0 && !addedDates.contains(from_date)){
                                                        val newItem = employeeCalendarItem.copy(days = 0, duration = 0, flexibilePermissionDuration = 0
                                                            , fromDate = (from_date?.toString() + "T00:00:00")
                                                                ?: "", fromTime = "", isFlexible = false, toTime = "")
                                                        lastSixMonthsSummary.add(newItem)
                                                        addedDates.add(from_date!!)
                                                    }else {
                                                        if (j > 0) {
                                                            from_date = from_date!!.plusDays(1)
                                                            j--
                                                        }
                                                    }
                                                }*/

                                                }
                                            }


                                        }
                                        //  attendanceSummaries.clear()
                                        attendanceSummaries = lastSixMonthsSummary
                                        // setBackgroundDay(lastSixMonthsSummary)


                                    } else {
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                            context,
                                            requestResponse.message.toString(),
                                            R.drawable.logo_vertical_b,
                                            colorPrimary
                                        )
/*
                                        SnackBarUtil.showSnackbar(
                                            context,
                                            requestResponse.message.toString(),
                                            false
                                        )*/
                                    }
                                    if (instance[Calendar.MONTH] +1 == getCurrentMonth() &&
                                        instance[Calendar.YEAR]== getCurrentYear()   ) {
                                        /*year = getCurrentYear()
                                        month = getCurrentMonth()!!.minus(1)
                                        firstdayOfMonth=getFirstDateofMonth()
                                        lastdayOfMonth=getLastDateofMonth()
                                        binding.txtFromDate.text= "$year-${if (month!! < 10) "0$month" else month}-${if (firstdayOfMonth!! < 10) "0$firstdayOfMonth" else firstdayOfMonth}"
                                        binding.txtToDate.text= "$year-${if (month!! < 10) "0$month" else month}-${if (lastdayOfMonth!! < 10) "0$lastdayOfMonth" else lastdayOfMonth}"
                                        */
                                        val newInstance = Calendar.getInstance()
                                        newInstance.add(instance[Calendar.MONTH],1)
                                        binding.calendarView!!.setDate(newInstance)
                                        addEvents(newInstance[Calendar.YEAR], newInstance[Calendar.MONTH] + 1)
                                    } else {
                                        instance.clear()
                                        val parts = updatedDate!!.split("-")
                                        val year = parts[0].toInt()
                                        val month = parts[1].toInt() // Adjust for zero-based month indexing
                                        val day = parts[2].toInt()
                                        val newSearchInstance = Calendar.getInstance()
                                        newSearchInstance.set(year,month-1,day)
                                        binding.calendarView!!.setDate(newSearchInstance)
                                        addEvents(newSearchInstance[Calendar.YEAR], newSearchInstance[Calendar.MONTH] + 1)
                                    }

                                    /*   val instance = Calendar.getInstance()
                                       binding.calendarView!!.setDate(instance)

                                       addEvents(instance[Calendar.YEAR], instance[Calendar.MONTH] + 1)
   */

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
                    else -> {}
                }
            }
        }
    }

    private fun getFirstDateofMonth(): Int? {
        currentPageDate = binding.calendarView!!.currentPageDate
        currentPageDate!!.set(Calendar.DAY_OF_MONTH,1)
        firstdayOfMonth = currentPageDate!![Calendar.DAY_OF_MONTH]
        return firstdayOfMonth
    }
    private fun getLastDateofMonth(): Int? {
        currentPageDate = binding.calendarView!!.currentPageDate
        currentPageDate!!.add(Calendar.MONTH, 1)
        currentPageDate!!.add(Calendar.DAY_OF_MONTH, -1)
        lastdayOfMonth = currentPageDate!![Calendar.DAY_OF_MONTH]
        return lastdayOfMonth
    }

    private  fun getDay_FromDate(date : String) :String?{
        month=Calendar.getInstance()[Calendar.MONTH]+1
        year =Calendar.getInstance()[Calendar.YEAR]
        day =Calendar.getInstance()[Calendar.DATE]
        return  "$year-${if (month!! < 10) "0$month" else month}-${if (day!! < 10) "0$day" else day}"
    }


    private fun postCalenderDetails() {
        val calendarRequest = CalendarRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            getFromDate()!!,
            getToDate()!!,
            UserShardPrefrences.getLanguage(mContext).toString()
        )


        viewModel.getCalendarData(
            mContext!!,
            calendarRequest
        )
    }


    fun checkIfLiesInFromAndToDates(fromDate: Date?, toDate: Date?, dateToCheck: Date): Boolean {
        return dateToCheck.compareTo(fromDate) == 0
    }

    fun getDayFromDate(dateString: String): Int {
        val dateTime = LocalDateTime.parse(dateString, formatter)
        // Get the day of the month
        return dateTime.dayOfMonth

    }

    private fun getToDate(): String? {
        return binding.txtToDate.text.toString()
    }

    private fun getFromDate(): String? {
        return binding.txtFromDate.text.toString()
    }
    /*
        private fun addEvents(year: Int, month: Int) {
            //check if this year_month is already loaded then skip reloading
            object : Thread() {
                override fun run() {
                    super.run()
                    if (mapLoadedMonths != null) {
                        for (kYear in mapLoadedMonths!!.keys) {
                            val kMonth: Int = mapLoadedMonths!!.get(kYear)!!
                            if (kYear == year && kMonth == month) {
                                return
                            }
                        }
                    } else {
                        mapLoadedMonths = HashMap<Int, Int>()
                    }
                    mapLoadedMonths!!.put(year, month)

                    // Iterate All Events
                    val _events: MutableList<EventDay> = java.util.ArrayList()
                    for (employeeCalendarItem in attendanceSummaries) {
                        val str_fromDate: String = employeeCalendarItem.fromDate
                        var str_toDate: String = employeeCalendarItem.toDate
                        var from_date: LocalDate
                        var to_date: LocalDate
                        var totalDiff: Int
                        if (str_fromDate != null) {
                            if (str_toDate == null) {
                                str_toDate = str_fromDate
                            }
                            from_date = LocalDate.parse(str_fromDate, formatter)
                            to_date = LocalDate.parse(str_toDate, formatter)
                            if (from_date.year === year && from_date.monthOfYear === month) {
                                totalDiff = Days.daysBetween(from_date, to_date).days
                                for (j in 0..totalDiff) {
                                    //Add all Events between from and To date Range ; but in current month range only
                                    val newDate: LocalDate = from_date.plusDays(j)
                                    val calendar = Calendar.getInstance()
                                    calendar.time = newDate.toDate()
                                    calendar.set(
                                        Calendar.DAY_OF_MONTH,
                                        getDayFromDate(employeeCalendarItem.fromDate)
                                    )
                                    // Check if the day already has a background resource assigned
                                    list.find { it.backgroundResource == R.drawable.permission_cal }?.apply {
                                        backgroundResource = null
                                    }
                                   val existingDay= list.filter {
                                        it.backgroundResource != R.drawable.holiday_cal &&
                                                it.backgroundResource != R.drawable.abcent_cal &&
                                                it.backgroundResource != R.drawable.violations_cal &&
                                                it.backgroundResource != R.drawable.rest_cal &&
                                                it.backgroundResource != R.drawable.leave_cal &&
                                                it.backgroundResource != R.drawable.study_cal &&
                                                it.backgroundResource != R.drawable.nursing_cal &&
                                                it.backgroundResource != R.drawable.work_home_cal &&
                                                it.backgroundResource != R.drawable.permission_pending_cal &&
                                                it.backgroundResource != R.drawable.leave_p_cal &&
                                                it.backgroundResource != R.drawable.work_home_pending_cal
                                    }
                                    */
/*val existingDay = list.find {

                                    if ( it.backgroundResource == R.drawable.permission_cal){
                                        it.backgroundResource = null
                                    }

                                    it.backgroundResource != R.drawable.holiday_cal
                                    it.backgroundResource != R.drawable.abcent_cal
                                    it.backgroundResource != R.drawable.violations_cal
                                    it.backgroundResource !=  R.drawable.rest_cal
                                    it.backgroundResource != R.drawable.leave_cal
                                    it.backgroundResource != R.drawable.study_cal
                                    it.backgroundResource != R.drawable.nursing_cal
                                    it.backgroundResource != R.drawable.work_home_cal
                                    it.backgroundResource != R.drawable.permission_pending_cal
                                    it.backgroundResource != R.drawable.leave_p_cal
                                    it.backgroundResource != R.drawable.work_home_pending_cal
                                }*//*

                                    // If no background resource is assigned, set it
                                if (!isBackgroundSetForDate(list, newDate, R.drawable.leave_p_cal)) {
                                    list.add(CalendarDay(calendar).apply {
                                        if (employeeCalendarItem.apptType == "2") {
                                            labelColor = R.color.grey_lighter
                                            //  backgroundResource = R.drawable.event_permissions_bg
                                            //   backgroundResource = R.drawable.p_cal
                                            backgroundResource = null
                                            backgroundResource = R.drawable.permission_cal
                                        } else
                                            if (employeeCalendarItem.apptType == "5" || employeeCalendarItem.apptType == "6") {
                                                labelColor = R.color.grey_lighter
                                                backgroundResource = null

                                                backgroundResource = R.drawable.holiday_cal
                                                //  selectedBackgroundResource = R.drawable.leave_letter

                                            } else
                                                if (employeeCalendarItem.apptType == "7") {
                                                    labelColor = R.color.grey_lighter
                                                    backgroundResource = null

                                                    backgroundResource = R.drawable.abcent_cal

                                                } else
                                                    if (employeeCalendarItem.apptType == "8" || employeeCalendarItem.apptType == "9"
                                                        || employeeCalendarItem.apptType == "10" || employeeCalendarItem.apptType == "14"
                                                        || employeeCalendarItem.apptType == "15"
                                                    ) {
                                                        labelColor = R.color.grey_lighter
                                                        backgroundResource = null

                                                        backgroundResource =
                                                            R.drawable.violations_cal
                                                        Log.e(
                                                            "leave_date",
                                                            employeeCalendarItem.fromDate + " " + "violation data"
                                                        )


                                                    } else
                                                        if (employeeCalendarItem.apptType == "11") {
                                                            labelColor = R.color.grey_lighter
                                                            backgroundResource = null

                                                            backgroundResource = R.drawable.rest_cal

                                                        } else
                                                            if (employeeCalendarItem.apptType == "1") {
                                                                labelColor = R.color.grey_lighter
                                                                backgroundResource = null
                                                                backgroundResource =
                                                                    R.drawable.leave_cal
                                                                Log.e(
                                                                    "leave_date",
                                                                    employeeCalendarItem.fromDate + " " + "leave date"
                                                                )


                                                            } else
                                                                if (employeeCalendarItem.apptType == "3") {
                                                                    labelColor =
                                                                        R.color.grey_lighter
                                                                    backgroundResource = null
                                                                    backgroundResource =
                                                                        R.drawable.nursing_cal

                                                                } else
                                                                    if (employeeCalendarItem.apptType == "4") {
                                                                        labelColor =
                                                                            R.color.grey_lighter
                                                                        backgroundResource = null
                                                                        backgroundResource =
                                                                            R.drawable.study_cal

                                                                    } else
                                                                        if (employeeCalendarItem.apptType == "12") {
                                                                            labelColor =
                                                                                R.color.grey_lighter
                                                                            backgroundResource =
                                                                                null

                                                                            backgroundResource =
                                                                                R.drawable.leave_p_cal
//Leave Request
                                                                            Log.e(
                                                                                "leave_date",
                                                                                employeeCalendarItem.fromDate + " " + "pending leave date"
                                                                            )
                                                                        } else


                                                                            if (employeeCalendarItem.apptType == "16") {
                                                                                labelColor =
                                                                                    R.color.grey_lighter
                                                                                backgroundResource =
                                                                                    null

                                                                                backgroundResource =
                                                                                    R.drawable.work_home_cal
//Remote Work
                                                                            } else
                                                                                if (employeeCalendarItem.apptType == "17") {
                                                                                    labelColor =
                                                                                        R.color.grey_lighter
                                                                                    backgroundResource =
                                                                                        null

                                                                                    backgroundResource =
                                                                                        R.drawable.work_home_pending_cal
//Remote Work pending
                                                                                } else
                                                                                    if (employeeCalendarItem.apptType == "13") {
                                                                                        labelColor =
                                                                                            R.color.grey_lighter
                                                                                        backgroundResource =
                                                                                            null

                                                                                        backgroundResource =
                                                                                            R.drawable.permission_pending_cal

                                                                                    }
                                    })
                                }
                               // list[j].selectedBackgroundResource.toString()
                              */
/* if (list.get(j).backgroundDrawable!!.equals(R.drawable.permission_cal) ){
                                   Toast.makeText(mContext,"permission cal repeated",Toast.LENGTH_SHORT)
                               }
*//*

                            }
                        }
                    }
                }

                binding.calendarView!!.post { binding.calendarView!!.setCalendarDays(list) }
            }
        }.start()
    }
*/
    private fun isBackgroundSetForDate(list: MutableList<CalendarDay>, date: LocalDate, backgroundResource: EventDay): Boolean {

        for (i in 0 until list.size) {
            val calendarDay = list[i]
            val calendar = calendarDay.calendar
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1 // Months in Calendar are 0-based
            val year = calendar.get(Calendar.YEAR)
            val calendarDate = LocalDate(year, month, day)

            if (calendarDate == date && EventDay(calendar,calendarDay.backgroundResource!!.toInt()) == backgroundResource) {
                return true
            } else {
                continue
            }
        }
        return false
    }

    private fun addEvents(year: Int, month: Int) {
        //check if this year_month is already loaded then skip reloading
        list.clear()
        object : Thread() {
            override fun run() {
                super.run()
                // binding.  calendarView!!.post {binding. calendarView!!.setEvents(java.util.ArrayList()) }
                if (mapLoadedMonths != null) {
                    for (kYear in mapLoadedMonths!!.keys) {
                        val kMonth: Int = mapLoadedMonths!!.get(kYear)!!
                        /* if (kYear == year && kMonth == month) {
                              return
                          }*/
                    }
                } else {
                    mapLoadedMonths = HashMap<Int, Int>()
                }
                mapLoadedMonths!!.put(year, month)

                // Iterate All Events
                val _events: MutableList<EventDay> = java.util.ArrayList()
                for (employeeCalendarItem in attendanceSummaries) {
                    val str_fromDate: String = employeeCalendarItem.fromDate
                    var str_toDate: String = employeeCalendarItem.toDate
                    var from_date: LocalDate
                    var to_date: LocalDate
                    var totalDiff: Int
                    if (str_fromDate != null) {
                        if (str_toDate == null) {
                            str_toDate = str_fromDate
                        }
                        from_date = LocalDate.parse(str_fromDate, formatter)
                        to_date = LocalDate.parse(str_toDate, formatter)
                        if (from_date.year === year && from_date.monthOfYear === month) {
                            totalDiff = Days.daysBetween(from_date, to_date).days
                            for (j in 0..totalDiff) {
                                //Add all Events between from and To date Range ; but in current month range only
                                val newDate: LocalDate = from_date.plusDays(j)
                                val parts = newDate.toString().split("-")
                                val year = parts[0].toInt()
                                val month = parts[1].toInt()
                                val day = parts[2].toInt()
                                val calendar = Calendar.getInstance()
                                calendar.set(year, month - 1, day)
                                /*calendar.time = newDate.toDate()
                                calendar.set(
                                    Calendar.DAY_OF_MONTH,
                                    getDayFromDate(employeeCalendarItem.fromDate)
                                )*/




                                Log.e(
                                    "getDayFromDate",
                                    getDayFromDate(employeeCalendarItem.fromDate).toString()
                                )

                                list.add(CalendarDay(calendar).apply {

                                    if (employeeCalendarItem.apptType == "2") {
                                        labelColor = R.color.grey_lighter

                                        //   backgroundResource = R.drawable.p_cal
                                        if (backgroundResource!=null){
                                            backgroundResource = R.drawable.permission_cal
                                        }else
                                        {
                                            backgroundResource = R.drawable.permission_cal
                                        }
                                    } else
                                        if (employeeCalendarItem.apptType == "5" || employeeCalendarItem.apptType == "6") {
                                            labelColor = R.color.grey_lighter
                                            if (backgroundResource!=null){
                                                backgroundResource = R.drawable.holiday_cal
                                            }else
                                            {
                                                backgroundResource = R.drawable.holiday_cal
                                            }

                                            //  selectedBackgroundResource = R.drawable.leave_letter

                                        } else
                                            if (employeeCalendarItem.apptType == "7") {
                                                labelColor = R.color.grey_lighter
                                                if (backgroundResource!=null){
                                                    backgroundResource = R.drawable.abcent_cal
                                                }else
                                                {
                                                    backgroundResource = R.drawable.abcent_cal
                                                }


                                            } else
                                                if (employeeCalendarItem.apptType == "8" || employeeCalendarItem.apptType == "9"
                                                    || employeeCalendarItem.apptType == "10" || employeeCalendarItem.apptType == "14"
                                                    || employeeCalendarItem.apptType == "15"
                                                ) {
                                                    labelColor = R.color.grey_lighter
                                                    if (backgroundResource!=null){
                                                        backgroundResource =
                                                            R.drawable.violations_cal
                                                    }else
                                                    {
                                                        backgroundResource = R.drawable.violations_cal
                                                    }

                                                    Log.e(
                                                        "leave_date",
                                                        employeeCalendarItem.fromDate + " " + "violation data"
                                                    )


                                                } else
                                                    if (employeeCalendarItem.apptType == "11") {
                                                        labelColor = R.color.grey_lighter
                                                        if (backgroundResource!=null){
                                                            backgroundResource = R.drawable.rest_cal
                                                        }else
                                                        {
                                                            backgroundResource = R.drawable.rest_cal
                                                        }


                                                    } else
                                                        if (employeeCalendarItem.apptType == "1") {
                                                            labelColor = R.color.grey_lighter
                                                            if (backgroundResource!=null){
                                                                backgroundResource = R.drawable.leave_cal
                                                            }else
                                                            {
                                                                backgroundResource = R.drawable.leave_cal
                                                            }
                                                            Log.e(
                                                                "leave_date",
                                                                employeeCalendarItem.fromDate + " " + "leave date"
                                                            )


                                                        } else
                                                            if (employeeCalendarItem.apptType == "3") {
                                                                labelColor =
                                                                    R.color.grey_lighter
                                                                if (backgroundResource!=null){
                                                                    backgroundResource = R.drawable.nursing_cal
                                                                }else
                                                                {
                                                                    backgroundResource = R.drawable.nursing_cal
                                                                }

                                                            } else
                                                                if (employeeCalendarItem.apptType == "4") {
                                                                    labelColor =
                                                                        R.color.grey_lighter
                                                                    if (backgroundResource!=null){
                                                                        backgroundResource = R.drawable.study_cal
                                                                    }else
                                                                    {
                                                                        backgroundResource = R.drawable.study_cal
                                                                    }

                                                                } else
                                                                    if (employeeCalendarItem.apptType == "12") {
                                                                        labelColor =
                                                                            R.color.grey_lighter
                                                                        if (backgroundResource!=null){
                                                                            backgroundResource = R.drawable.leave_p_cal
                                                                        }else
                                                                        {
                                                                            backgroundResource = R.drawable.leave_p_cal
                                                                        }

//Leave Request
                                                                        Log.e(
                                                                            "leave_date",
                                                                            employeeCalendarItem.fromDate + " " + "pending leave date"
                                                                        )
                                                                    } else


                                                                        if (employeeCalendarItem.apptType == "16") {
                                                                            labelColor =
                                                                                R.color.grey_lighter
                                                                            if (backgroundResource!=null){
                                                                                backgroundResource = R.drawable.work_home_cal
                                                                            }else
                                                                            {
                                                                                backgroundResource = R.drawable.work_home_cal
                                                                            }
//Remote Work
                                                                        } else
                                                                            if (employeeCalendarItem.apptType == "17") {
                                                                                labelColor =
                                                                                    R.color.grey_lighter
                                                                                if (backgroundResource!=null){
                                                                                    backgroundResource = R.drawable.work_home_pending_cal
                                                                                }else
                                                                                {
                                                                                    backgroundResource = R.drawable.work_home_pending_cal
                                                                                }
//Remote Work pending
                                                                            } else
                                                                                if (employeeCalendarItem.apptType == "13") {
                                                                                    labelColor =
                                                                                        R.color.grey_lighter
                                                                                    if (backgroundResource!=null){
                                                                                        backgroundResource = R.drawable.permission_pending_cal
                                                                                    }else
                                                                                    {
                                                                                        backgroundResource = R.drawable.permission_pending_cal
                                                                                    }

                                                                                }

//came issue
                                })
                                /*  _events.add(
                                                                    EventDay(
                                                                        calendar,
                                                                        returnEventImg(employeeCalendarItem.apptType)
                                                                    )
                                                                )*/


                            }


                        }
                    }
                }

                binding.calendarView!!.post { binding.calendarView!!.setCalendarDays(list) }
            }
        }.start()
    }



    /*int returnEventImg(String str) {
        int res;
        if (str.equals("PAK- Sick Leave") || str.equals("PAK- Personal short Leave")) {
            res = R.drawable.event_leave;
        } else if (str.equals("Youm-e- Ashura")) {
            res = R.drawable.event_holiday;
        } else if (str.equals("Absent")) {
            res = R.drawable.event_absent;
        } else if (str.equals("Early Out")) {
            res = R.drawable.event_violations;
        } else if (str.equals("Rest Day")) {
            res = R.drawable.event_rest_day;
        } else {
            res = R.drawable.locaicon;
        }


        return res;
    }*/
    fun returnEventImg(str: String): Int {
        val res: Int
        res = if (str == "1" || str == "2") {
            R.drawable.l_cal
        } else if (str == "6") {
            R.drawable.h_cal
        } else if (str == "7") {
            R.drawable.a_cal
        } else if (str == "9" || str == "8") {
            R.drawable.v_cal
        } else if (str == "11") {
            R.drawable.s_cal
        } else {
            R.drawable.event_none
        }
        return res
    }

    private fun setDate() {
        year = getCurrentYear()
        month = getCurrentMonth()
        firstdayOfMonth=getFirstDateofMonth()
        lastdayOfMonth=getLastDateofMonth()
        binding.txtFromDate.text= "$year-${if (month!! < 10) "0$month" else month}-${if (firstdayOfMonth!! < 10) "0$firstdayOfMonth" else firstdayOfMonth}"
        binding.txtToDate.text= getCurrentDate()

        /* val currentDate: String = DateTime_Op.getCurrentDateTime("yyyy-MM-dd")
            val nextYearDate: String = DateTime_Op.oneYearNext(currentDate)
            binding.txtToDate.setText(nextYearDate)
            val firstDateOfcurrentMonth: String = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")
            binding.txtFromDate.setText(firstDateOfcurrentMonth)*/
    }

    override fun onResume() {
        super.onResume()


        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.Calendar))

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

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.img_back -> (activity as MainActivity).onBackPressed()

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
                        val dateString = date.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        pickDate(
                            dateString[2].toInt(),
                            dateString[1].toInt(),
                            dateString[0].toInt(),
                            binding.txtFromDate
                        )
                    }
                } catch (e: Exception) {
                    e.message
                }
            }
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
                        val dateString = todate.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        pickDate(
                            dateString[2].toInt(),
                            dateString[1].toInt(),
                            dateString[0].toInt(),
                            binding.txtToDate
                        )
                    }
                } catch (e: java.lang.Exception) {
                    e.message
                }
            }
            R.id.txt_from_date ->{
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
                        val dateString = date.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        pickDate(
                            dateString[2].toInt(),
                            dateString[1].toInt(),
                            dateString[0].toInt(),
                            binding.txtFromDate
                        )
                    }
                } catch (e: Exception) {
                    e.message
                }
            }
            R.id.txt_to_date ->{
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
                        val dateString = todate.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        pickDate(
                            dateString[2].toInt(),
                            dateString[1].toInt(),
                            dateString[0].toInt(),
                            binding.txtToDate
                        )
                    }
                } catch (e: java.lang.Exception) {
                    e.message
                }
            }
            R.id.iv_search_icon -> {


                if (DateTime_Op.validateDates(
                        binding.txtFromDate.text.toString(),
                        binding.txtToDate.text.toString()
                    )
                ) {
                    val parts = binding.txtFromDate.text!!.split("-")
                    val year = parts[0].toInt()
                    val month = parts[1].toInt() // Adjust for zero-based month indexing
                    val day = parts[2].toInt()
                    val newSearchInstance = Calendar.getInstance()
                    newSearchInstance.set(year,month-1,day)
                    updatedDate=binding.txtFromDate.text.toString()
                    callCalenderAPI(newSearchInstance)
                } else {
                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                        mContext,
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
        val datePickerFragment =
            DatePickerFragment.newInstance(year, month, day, textView.id, "CalenderFragmentSelf")
        datePickerFragment.show(parentFragmentManager, "datePicker")
        /*     val now = Calendar.getInstance()
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




    override fun onChange() {
        year = getCurrentYear()
        month = getCurrentMonth()
        firstdayOfMonth=getFirstDateofMonth()
        lastdayOfMonth=getLastDateofMonth()
        if (Calendar.getInstance()[Calendar.MONTH]+1 != getCurrentMonth()
            || Calendar.getInstance()[Calendar.YEAR] != getCurrentYear()
        ) {
            if (month!! < getMonthFromDate() && year == getYearFromDate()){
                binding.txtFromDate.text =
                    "$year-${if (month!! < 10) "0$month" else month}-${if (firstdayOfMonth!! < 10) "0$firstdayOfMonth" else firstdayOfMonth}"
                updatedDate = binding.txtFromDate.text.toString()
            } else
                if (month!! > getMonthFromDate()&& year == getYearFromDate()){
                    binding.txtToDate.text =
                        "$year-${if (month!! < 10) "0$month" else month}-${if (lastdayOfMonth!! < 10) "0$lastdayOfMonth" else lastdayOfMonth}"
                    updatedDate = binding.txtToDate.text.toString()
                }
                else
                    if (month!! == getMonthFromDate()&& year == getYearFromDate()){
                        binding.txtFromDate.text =
                            "$year-${if (month!! < 10) "0$month" else month}-${if (firstdayOfMonth!! < 10) "0$firstdayOfMonth" else firstdayOfMonth}"
                        updatedDate = binding.txtFromDate.text.toString()
                    }
                    else
                        if (year != getYearFromDate()){
                            binding.txtFromDate.text =
                                "$year-${if (month!! < 10) "0$month" else month}-${if (firstdayOfMonth!! < 10) "0$firstdayOfMonth" else firstdayOfMonth}"
                            binding.txtToDate.text =
                                "$year-${if (month!! < 10) "0$month" else month}-${if (lastdayOfMonth!! < 10) "0$lastdayOfMonth" else lastdayOfMonth}"

                            updatedDate = binding.txtFromDate.text.toString()
                        }
            /* binding.txtToDate.text =
                 "$year-${if (month!! < 10) "0$month" else month}-${if (firstdayOfMonth!! < 10) "0$firstdayOfMonth" else firstdayOfMonth}"*/


        }else{
            binding.txtFromDate.text =
                "$year-${if (month!! < 10) "0$month" else month}-${if (firstdayOfMonth!! < 10) "0$firstdayOfMonth" else firstdayOfMonth}"
            binding.txtToDate.text = getCurrentDate()

            updatedDate =binding.txtToDate.text.toString()
        }

        /*else
        {
            if (month!! > getMonthFromToDate()){
                binding.txtToDate.text =
                    "$year-${if (month!! < 10) "0$month" else month}-${if (firstdayOfMonth!! < 10) "0$firstdayOfMonth" else firstdayOfMonth}"
                updatedDate = binding.txtToDate.text.toString()
            }
        }*/

        if(click%2== 0){

            callCalenderAPI(binding.calendarView.currentPageDate)
            click+=1
        }else
        {
            click +=1
        }

        /*    if (click%2==1 && forwardbtnClick && backbtnClick){
                callCalenderAPI(binding.calendarView.currentPageDate)
                //click -=1

            }
*/

    }

}
