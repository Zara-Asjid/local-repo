package com.sait.tawajudpremiumplusnewfeatured.ui.violations.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.databinding.RowViolationUpdateitemBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.listener.ViolationItemClickListener
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationData
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.util.*

class ViolationUpdateAdapter(
    private var violationList: ArrayList<ViolationData>,
    private val violationItemClickListener: ViolationItemClickListener,
    private var mContext: Context

) : RecyclerView.Adapter<ViolationUpdateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = RowViolationUpdateitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return violationList.size
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val violationData = violationList[position]

        //  holder.txtaction.text = violationData.description

        holder.txt_from_time.text = violationData.permStart

        if (violationData.permEnd != null && !violationData.permEnd.isEmpty()) {
            holder.txt_to_time.text = violationData.permEnd

        }
        holder.txtdate.text =
            LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.moveDate))



        if (violationData.type.equals("V", ignoreCase = true)) {
            holder.fromTime.text = mContext.resources.getString(R.string.from_time_colon)
            holder.toTime.text = mContext.resources.getString(R.string.to_time_txt_colon)
            holder.txtRequestType.text =
                mContext.resources.getString(R.string.update_permission_txt)
            holder.txtPermRequestType.text =
                mContext.resources.getString(R.string.update_permission_txt)


            holder.txt_from_time.text = violationData.permStart
            holder.txt_to_time.text = violationData.permEnd


            if(UserShardPrefrences.getLanguage(mContext).equals("0")) {

                holder.txtaction.text =
                    mContext.resources.getString(R.string.permission_colon_txt) + " " + violationData.permType
            }
            else{
                holder.txtaction.text =
                    mContext.resources.getString(R.string.permission_colon_txt) + " " + violationData.permTypeAr
            }

        } else
            if (violationData.type.equals("A", ignoreCase = true)) {
                holder.fromTime.text = mContext.resources.getString(R.string.from_date_colon_txt)
                holder.toTime.text = mContext.resources.getString(R.string.to_date_colon_txt)
                holder.txtRequestType.text =
                    mContext.resources.getString(R.string.update_leave_request_txt)
                holder.txtPermRequestType.text =
                    mContext.resources.getString(R.string.update_leave_request_txt)


                holder.txt_from_time.text = DateTime_Op.getDateFormat(violationData.permStartDate)
                holder.txt_to_time.text = DateTime_Op.getDateFormat(violationData.permEndDate)

                if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
                    holder.txtaction.text =
                        mContext.resources.getString(R.string.leave_colon_txt) + " " + violationData.permType
                }
                else{
                    holder.txtaction.text =
                        mContext.resources.getString(R.string.leave_colon_txt) + " " + violationData.permTypeAr
                }

            } else
                if (violationData.type.equals("P", ignoreCase = true)) {
                    holder.fromTime.text = mContext.resources.getString(R.string.period_txt)
                    holder.l_To_date.visibility = View.INVISIBLE
                    // holder.fromTime.text = mContext.resources.getString(R.string.from_date_colon_txt)
                    holder.toTime.text = mContext.resources.getString(R.string.to_date_colon_txt)
                    holder.txtRequestType.text =
                        mContext.resources.getString(R.string.update_permission_txt)
                    holder.txtPermRequestType.text =
                        mContext.resources.getString(R.string.update_permission_txt)

                    holder.txt_from_time.text =
                        LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permStartDate))
                    holder.txt_to_time.text =
                        LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permEndDate))

                    if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
                        holder.txtaction.text =
                            mContext.resources.getString(R.string.permission_colon_txt) + " " + violationData.permType
                    }
                    else{
                        holder.txtaction.text =
                            mContext.resources.getString(R.string.permission_colon_txt) + " " + violationData.permTypeAr
                    }


                    if (violationData.isForPeriod) {

                        holder.txt_from_time.text = LocaleHelper.arabicToEnglish(
                            DateTime_Op.getDateFormat(violationData.permStartDate) + " - " + DateTime_Op.getDateFormat(
                                violationData.permEndDate
                            )
                        )

                    } else {

                        holder.txt_from_time.text =
                            LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permStartDate))
                    }
                    if (!violationData.isfullDay) {
                        if (!violationData.isFlexible) {
                            holder.txt_duration.text =
                                mContext.resources.getString(R.string.duration_txt)
                            holder.txt_from_time_new.text =
                                violationData.permStart + " - " + violationData.permEnd
                            holder.l_totime.visibility = View.GONE
                            holder.l_duration.visibility = View.GONE
                        } else {
                            holder.txt_duration.text =
                                mContext.resources.getString(R.string.duration_txt)
                            holder.txt_from_time_new.text = violationData.duration
                            holder.l_totime.visibility = View.GONE
                            holder.l_duration.visibility = View.GONE
                        }
                    } else {
                        holder.txt_duration.text =
                            mContext.resources.getString(R.string.duration_txt)
                        holder.txt_from_time_new.text =
                            mContext.resources.getString(R.string.full_day_txt)
                        holder.l_totime.visibility = View.GONE
                        holder.l_duration.visibility = View.GONE
                    }

                    /*
                                if (!violationData.isfullDay) {
                                    holder.l_time.visibility = View.VISIBLE
                                    holder.txt_from_time_new.text = violationData.permStart
                                    holder.txt_to_time_new.text = violationData.permEnd
                                    // holder.txtduration.text = violationData.duration


                                    if (violationData.permStart != null && violationData.permStart.isNotEmpty()
                                        && violationData.permEnd != null && violationData.permEnd.isNotEmpty()
                                    ) {


                                        val timeString1 = violationData.permStart
                                        val parts1 = timeString1.split(":")
                                        val hours1 = parts1[0].toInt()
                                        val minutes1 = parts1[1].toInt()
                                        println("Hours: $hours1, Minutes: $minutes1")


                                        val timeString2 = violationData.permEnd
                                        val parts2 = timeString2.split(":")
                                        val hours2 = parts2[0].toInt()
                                        val minutes2 = parts2[1].toInt()
                                        println("Hours: $hours2, Minutes: $minutes2")

                                        holder.txtduration.text = calculateDuration(hours1, hours2, minutes1, minutes2)
                                    }
                                } else {
                                    holder.l_time.visibility = View.GONE
                                }
                    */
                } else
                    if (violationData.type.equals("L", ignoreCase = true)) {

                        holder.l_manul_entry.visibility = View.GONE
                        holder.fromTime.text = mContext!!.resources.getString(R.string.period_txt)
                        holder.l_To_date.visibility = View.INVISIBLE

                        //   holder.fromTime.text = mContext.resources.getString(R.string.from_date_colon_txt)
                        holder.toTime.text =
                            mContext.resources.getString(R.string.to_date_colon_txt)
                        holder.txtRequestType.text =
                            mContext.resources.getString(R.string.update_leave_request_txt)
                        holder.txtPermRequestType.text =
                            mContext.resources.getString(R.string.update_leave_request_txt)
/*
                        if(violationData.isForPeriod){

                        }
                        else{
                            holder.txt_from_time.text = LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permStartDate))

                        }*/
                        holder.txt_from_time.text =
                            LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permStartDate)) + " - " + LocaleHelper.arabicToEnglish(
                                DateTime_Op.getDateFormat(violationData.permEndDate)
                            )

                        /* holder.txt_from_time.text = LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permStartDate))
                  holder.txt_to_time.text =  LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permEndDate))*/
                        /*  holder.txt_from_time.text = DateTime_Op.getDateFormat(violationData.permStartDate)
                          holder.txt_to_time.text =  DateTime_Op.getDateFormat(violationData.permEndDate)*/

                        if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
                            holder.txtaction.text =
                                mContext.resources.getString(R.string.leave_colon_txt) + " " + violationData.permType
                        }
                        else{
                            holder.txtaction.text =
                                mContext.resources.getString(R.string.leave_colon_txt) + " " + violationData.permTypeAr
                        }
                        holder.l_To_date.visibility = View.INVISIBLE

                        /*   if(!violationData.isfullDay){
                               if(!violationData.isFlexible){
                                   holder.txt_duration.text = "Duration"
                                   holder.txt_from_time_new.text =violationData.permStart+" - "+violationData.permEnd
                                   holder.l_totime.visibility = View.GONE
                                   holder.l_duration.visibility = View.GONE
                               }
                               else{
                                   holder.txt_duration.text = "Duration"
                                   holder.txt_from_time_new.text =violationData.duration
                                   holder.l_totime.visibility = View.GONE
                                   holder.l_duration.visibility = View.GONE
                               }
                           }

                           else{
                               holder.txt_duration.text ="Duration"

                               holder.txt_from_time_new.text ="Full day"

                               holder.l_totime.visibility = View.GONE
                               holder.l_duration.visibility = View.GONE
                           }*/



                        if (!violationData.isfullDay) {
                            holder.l_time.visibility = View.VISIBLE

                            holder.txt_from_time_new.text = violationData.permStart
                            holder.txt_to_time_new.text = violationData.permEnd
                            // holder.txtduration.text = violationData.duration


                            if (violationData.permStart != null && violationData.permStart.isNotEmpty()
                                && violationData.permEnd != null && violationData.permEnd.isNotEmpty()
                            ) {
                      /*          val timeString1 = violationData.permStart
                                val parts1 = timeString1.split(":")
                                val hours1 = parts1[0].toInt()
                                val minutes1 = parts1[1].toInt()
                                println("Hours: $hours1, Minutes: $minutes1")


                                val timeString2 = violationData.permEnd
                                val parts2 = timeString2.split(":")
                                val hours2 = parts2[0].toInt()
                                val minutes2 = parts2[1].toInt()
                                println("Hours: $hours2, Minutes: $minutes2")

                                holder.txtduration.text =
                                    calculateDuration(hours1, hours2, minutes1, minutes2)*/
                                holder.txtduration.text = violationData.permStart
                            }
                        } else {
                            holder.l_time.visibility = View.GONE
                        }
                        holder.l_time.visibility = View.GONE

                    } else
                        if (violationData.type.equals(
                                "M",
                                ignoreCase = true
                            ) || violationData.type.equals("MS", ignoreCase = true)
                        ) {
                            holder.l_manul_entry.visibility = View.VISIBLE
                            holder.txtRequestType.text =
                                mContext.resources.getString(R.string.update_manual_entry_request_txt)
                            holder.txtPermRequestType.text =
                                mContext.resources.getString(R.string.update_manual_entry_request_txt)

                            holder.fromTime.text = mContext.resources.getString(R.string.time_txt)
                            holder.toTime.text = mContext.resources.getString(R.string.reason)
                            holder.txt_from_time.text =
                                LocaleHelper.arabicToEnglish(FormatingTime(violationData.inTime))
                            holder.txt_to_time.text = violationData.permType
                            holder.l_To_date.visibility = View.VISIBLE
                            holder.txtRequestdate.text =
                                mContext.resources.getString(R.string.transaction_date)
                            // holder.txt_to_time.text = FormatingTime(violationData.moveDate)
                            holder.txtdate.text =
                                LocaleHelper.arabicToEnglish(FormatingDate(violationData.permStartDate).toString())
                            holder.txt_manual_date.text =
                                LocaleHelper.arabicToEnglish(FormatingDate(violationData.moveDate).toString())
                            holder.txt_manual_txt.text =
                                mContext.resources.getString(R.string.request_date_txt)
                            if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                holder.txtaction.text =
                                    mContext.resources.getString(R.string.manual_entry_colon_txt) + " " + violationData.permType
                            }
                            else {

                                holder.txtaction.text =
                                    mContext.resources.getString(R.string.manual_entry_colon_txt) + " " + violationData.permTypeAr
                            }
                                holder.l_time.visibility = View.GONE

                        }


        if (violationData.fK_StatusId == null || violationData.fK_StatusId === -1) {
            holder.txt_permission_status.text = mContext.resources.getString(R.string.none_txt)
        }
        if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
            holder.txt_permission_status.text = violationData.description
        }
        else{
            holder.txt_permission_status.text = violationData.descriptionAr

        }

/*
        if (violationData.fK_StatusId == null || violationData.fK_StatusId == -1 || violationData.fK_StatusId == 0) {
            holder.txt_permission_status.text = mContext.resources.getString(R.string.none_txt)
        } else {
            when (violationData.fK_StatusId) {
                1 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_pending)

                2 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_approved_by_manager)

                3 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_approved_by_hr)

                4 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_rejected_by_manager)

                5 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_rejected_by_hr)

                6 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_approved_by_gm)

                7 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_rejected_by_gm)

                8 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_auto_approved)

                9 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_approved_by_1st_manager)

                10 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_deleted)

                11 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_permission_converted_to_leave)

                12 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_approved_by_2nd_manager)

                13 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_rejected_by_2nd_manager)

            }
        }
*/
        holder.img_voilation.visibility = View.GONE
        //    holder.txt_permission_status.text = violationData.employeeId.toString()


        if (violationData.description.contains("Approved By Human Resource")) {
            holder.img_voilation.setImageResource(R.drawable.icon_approve)
        } else if (violationData.description.contains("Delay:") ||
            violationData.description
                .contains("التأخير:")
        ) {
            holder.img_voilation.setImageResource(R.drawable.icon_delay)
        } else if (violationData.description.contains("Early Out:") || violationData.description
                .contains("الخروج المبكر:")
        ) {
            holder.img_voilation.setImageResource(R.drawable.icon_delay)
        } else if (violationData.description.contains("Out Time:") || violationData.description
                .contains("مدة الخروج:")
        ) {
            holder.img_voilation.setImageResource(R.drawable.icon_out)
        } else if (violationData.description.contains("Rejected By Human Resource")) {
            holder.img_voilation.setImageResource(R.drawable.icon_absent)
        } else if (violationData.description.contains("Pending for Manager Approval")) {


            if (violationData.type == "P") {
                holder.img_voilation.setImageResource(R.drawable.permission_violation)
            } else if (violationData.type == "L") {
                holder.img_voilation.setImageResource(R.drawable.leave_violation)
            } else if (violationData.type == "M") {
                holder.img_voilation.setImageResource(R.drawable.manual_violation)
            } else {
                holder.img_voilation.setImageResource(R.drawable.icon_pending)
            }


        } else {
            val description: String = violationData.description
        }


        /*

                holder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut


                // Drag From Right
                holder.swipeLayout.addDrag(
                    SwipeLayout.DragEdge.Right,
                    holder.swipeLayout.findViewById<View>(R.id.bottom_wrapper)
                )


                // Handling different events when swiping


                // Handling different events when swiping
                holder.swipeLayout.addSwipeListener(object : SwipeListener {
                    override fun onClose(layout: SwipeLayout) {
                        //when the SurfaceView totally cover the BottomView.
                    }

                    override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {
                        //you are swiping.
                    }

                    override fun onStartOpen(layout: SwipeLayout) {}
                    override fun onOpen(layout: SwipeLayout) {
                        //when the BottomView totally show.
                    }

                    override fun onStartClose(layout: SwipeLayout) {}
                    override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {
                        //when user's hand released.
                    }
                })
        */


        if (violationData.description.equals("Approved By Human Resource", true)) {
        }


        if (violationData.fK_StatusId == 1 || violationData.fK_StatusId == 10) {
            holder.per_icon_pending.visibility = View.VISIBLE
        } else {
            holder.per_icon_pending.visibility = View.GONE
        }
        holder.per_icon_pending.setOnClickListener {


            violationItemClickListener.onPermissionItemClick(
                violationData,
                holder.adapterPosition
            )


        }
        /*
                holder.bottom_wrapper.setOnClickListener {
                    violationItemClickListener.onViolationSwipeItemClick(
                        violationData,
                        holder.adapterPosition
                    )
                }*/


    }

    fun FormatingDate(date: String?): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        var parse: Date? = null
        try {
            parse = sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var dateFormated: String? = null
        if (parse != null) {
            val instance = Calendar.getInstance()
            instance.time = parse
            dateFormated = DateFormat.format("dd/MM/yyyy", parse) as String
        }
        return dateFormated
    }

    fun FormatingTime(Time: String): String {
        val displayFormatTime = SimpleDateFormat("HH:mm")
        val parseFormatTime = SimpleDateFormat("hh:mma")
        var time: Date? = null
        try {
            time = parseFormatTime.parse(Time)
        } catch (e: ParseException) {
            e.printStackTrace()
            try {
                time =
                    displayFormatTime.parse(Time.split("T".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[1])
            } catch (ex: ParseException) {
                ex.printStackTrace()
            }
        }
        return displayFormatTime.format(time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDuration(hours1: Int, hours2: Int, minutes1: Int, minutes2: Int): String {
        val startTime = LocalTime.of(hours1, minutes1) // 08:52
        val endTime = LocalTime.of(hours2, minutes2) // 16:08

        val duration: Duration = Duration.between(startTime, endTime)

        val hours = duration.toHours()
        val minutes = (duration.seconds % 3600) / 60

        println("Duration between $startTime and $endTime is $hours hours and $minutes minutes.")
        val durationfinal = "$hours:$minutes"
        return durationfinal
    }

    class ViewHolder(view: RowViolationUpdateitemBinding) : RecyclerView.ViewHolder(view.root) {
        val txtaction: TextView = view.txtaction
        val txt_from_time: TextView = view.txtFromTime
        val txt_to_time: TextView = view.txtToTime
        val txtdate: TextView = view.txtdate
        val per_icon_pending: RelativeLayout = view.lRequestManual
        val txt_permission_status: TextView = view.txtPermissionStatus
        val rlItemlayout: RelativeLayout = view.rlItemlayout
        val img_voilation: ImageView = view.imgVoilation
        val txtPermRequestType: TextView = view.txtPermRequest
        val l_time: LinearLayoutCompat = view.lTime
        val txt_from_time_new: TextView = view.txtFromTimeNew
        val txt_to_time_new: TextView = view.txtToTimeNew
        val txtduration: TextView = view.txtduration


        val fromTime: TextView = view.fromTime
        val toTime: TextView = view.toTime

        val txtRequestType: TextView = view.txtRequestType
        val txt_duration: TextView = view.txtDuration
        val l_totime: LinearLayout = view.lTotime
        val l_duration: LinearLayout = view.lDuration

        val l_from_date: LinearLayout = view.lFromDate
        val l_To_date: LinearLayout = view.lToDate
        val txt_manual_date: TextView = view.txtManualDate
        val txt_manual_txt: TextView = view.txtManualTxt
        val txtRequestdate: TextView = view.txtRequestdate
        val l_manul_entry: LinearLayoutCompat = view.lManulEntry


    }
}