package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.adapters

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.listener.PendingLeaveApprovalListener_Manager
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.pending_HR_leaves_data
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingHRManualEntryData
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingHRPermissionData
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Pending_Adapter_Hr(
    private var ListofLeaveRequest: ArrayList<pending_HR_leaves_data>?,
    private var ListofPermissionRequest: ArrayList<PendingHRPermissionData>,
    private var ListofManualEntryRequest: ArrayList<PendingHRManualEntryData>,
    private val Listener: PendingLeaveApprovalListener_Manager,
    private var mContext: Context
) : RecyclerSwipeAdapter<Pending_Adapter_Hr.SimpleViewHolder>() {

    private var pendingPermissionRequestList: ArrayList<PendingHRPermissionData>? = null
    private var pendingLeaveRequestList: ArrayList<pending_HR_leaves_data>? = null
    private var pendingManualRequestList: ArrayList<PendingHRManualEntryData>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.emp_req_pending_item, parent, false)
        return SimpleViewHolder(view)
    }

    init {
        this.pendingLeaveRequestList = ListofLeaveRequest
        this.pendingManualRequestList = ListofManualEntryRequest
        this.pendingPermissionRequestList = ListofPermissionRequest
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

    override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
        if (pendingLeaveRequestList!!.size != 0) {

            val items: pending_HR_leaves_data = pendingLeaveRequestList!![position]
            viewHolder.txt_type.text = mContext.resources.getString(R.string.leave_type_txt)
            viewHolder.txt_totime.text = mContext.resources.getString(R.string.to_date_txt)
            viewHolder.txt_fromtime.text = mContext.resources.getString(R.string.from_date_txt)

            viewHolder.l_from_time.visibility = View.VISIBLE
            viewHolder.l_to_time.visibility = View.VISIBLE
            viewHolder.iv_approve.visibility = View.GONE
            viewHolder.l_time.visibility = View.GONE
            viewHolder.l_reason.visibility = View.GONE


            if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
                viewHolder.tv_action.text = items.employeeNo + " " + items.employeeName
                viewHolder.txt_permission.text = items.leaveName
                viewHolder.txt_status.text = items.statusName

            }
            else{

                viewHolder.tv_action.text = items.employeeNo + " " + items.employeeArabicName
                viewHolder.txt_permission.text = items.leaveArabicName
                viewHolder.txt_status.text = items.statusNameArabic

            }


            viewHolder.txt_req_date.text =
                LocaleHelper.arabicToEnglish(FormatingDate(items.requestDate)!!)
            viewHolder.txt_from_time.text =
                LocaleHelper.arabicToEnglish(FormatingDate(items.fromDate)!!)
            viewHolder.txt_to_time.text =
                LocaleHelper.arabicToEnglish(FormatingDate(items.toDate)!!)


        }

        if (pendingPermissionRequestList!!.size != 0) {

            val items: PendingHRPermissionData = pendingPermissionRequestList!![position]
            viewHolder.txt_type.text = mContext.resources.getString(R.string.permission_txt)
            viewHolder.iv_approve.visibility = View.GONE
            viewHolder.l_time.visibility = View.GONE
            viewHolder.l_reason.visibility = View.GONE

            if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
                viewHolder.tv_action.text = items.employeeNo + " " + items.employeeName
                viewHolder.txt_permission.text = items.permName
                viewHolder.txt_status.text = items.statusName
            }
            else{
                viewHolder.tv_action.text = items.employeeNo + " " + items.employeeArabicName
                viewHolder.txt_permission.text = items.permArabicName
                viewHolder.txt_status.text = items.statusNameArabic
            }





            if (items.days.toDouble() > 1) {
                viewHolder.txt_reqdate.text = mContext!!.resources.getString(R.string.period_txt)
                viewHolder.txt_req_date.text =
                    LocaleHelper.arabicToEnglish(FormatingDate(items.permDate)!!) + " - " + LocaleHelper.arabicToEnglish(
                        FormatingDate(items.permEndDate)!!
                    )

            } else {
                viewHolder.txt_reqdate.text = mContext!!.resources.getString(R.string.period_txt)
                viewHolder.txt_req_date.text = LocaleHelper.arabicToEnglish(FormatingDate(items.permDate)!!)
            }


            if (!items.isFullDay) {
                if (!items.isFlexible) {
                    viewHolder.l_from_time.visibility = View.VISIBLE
                    viewHolder.l_to_time.visibility = View.GONE
                    viewHolder.txt_fromtime.text =
                        mContext!!.resources.getString(R.string.duration_txt)


                  //  if (items.permDate != null && items.permEndDate != null && items.permEndDate.isNotEmpty() && items.permDate.isNotEmpty()) {
                      //  LocaleHelper.arabicToEnglish(FormatingDate(items.permDate)!!) + " - " + LocaleHelper.arabicToEnglish(FormatingDate(items.permEndDate)!!)
                    if (items.permDate != null  &&  items.permDate.isNotEmpty()) {

                        viewHolder.txt_from_time.text =
                            FormatingTime(items.fromTime) + " - " + FormatingTime(items.toTime)
                    } else {


                        viewHolder.l_from_time.visibility = View.VISIBLE
                        viewHolder.l_to_time.visibility = View.GONE
                        viewHolder.txt_fromtime.text =
                            mContext!!.resources.getString(R.string.duration_txt)
                        viewHolder.txt_from_time.text = items.totalTime


                    }


                } else {

                    viewHolder.txt_fromtime.text =
                        mContext!!.resources.getString(R.string.duration_txt)

                    viewHolder.txt_from_time.text = items.totalTime
                    viewHolder.l_from_time.visibility = View.VISIBLE
                    viewHolder.l_to_time.visibility = View.GONE
                }



            } else {

                viewHolder.txt_fromtime.text =
                    mContext!!.resources.getString(R.string.duration_txt)

                viewHolder.txt_from_time.text =
                    mContext!!.resources.getString(R.string.full_day_txt)
                viewHolder.l_from_time.visibility = View.VISIBLE
                viewHolder.l_to_time.visibility = View.GONE
            }
        }
        if (pendingManualRequestList!!.size != 0) {
            viewHolder.iv_approve.visibility = View.GONE
            viewHolder.l_to_time.visibility = View.GONE
            viewHolder.l_from_time.visibility = View.VISIBLE
            val itemPendingManualEntry: PendingHRManualEntryData =
                pendingManualRequestList!![position]
            val partMoveDate: List<String> = itemPendingManualEntry.createD_DATE.split("T")
            val partINTIME: List<String> = itemPendingManualEntry.moveTime.split("T")
            val partDate = partINTIME[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val partTime =partINTIME[1].split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                viewHolder.tv_action.setText(itemPendingManualEntry.employeeNo + " " + itemPendingManualEntry.employeeName)

                viewHolder.txt_type.text = mContext.resources.getString(R.string.reason)
                viewHolder.txt_permission.text = itemPendingManualEntry.reasonName
                viewHolder.txt_reqdate.text = mContext.resources.getString(R.string.date)
                viewHolder.txt_req_date.text =
                    LocaleHelper.arabicToEnglish(FormatingDate(itemPendingManualEntry.moveDate)!!)
                viewHolder.txt_fromtime.text = mContext.resources.getString(R.string.time_txt)
            //    viewHolder.txt_from_time.text = partTime[0] + ":" + partTime[1]
                viewHolder.txt_from_time.text = LocaleHelper.arabicToEnglish(FormatingTime(itemPendingManualEntry.moveTime))
                viewHolder.txt_status.text = itemPendingManualEntry.statusName
            } else {


               // viewHolder.tv_action.setText(itemPendingManualEntry.employeeNo + " " + itemPendingManualEntry.employeeArabicName)

                viewHolder.txt_type.text = mContext.resources.getString(R.string.reason)
              //  viewHolder.txt_permission.text = itemPendingManualEntry.reasonArabicName
                viewHolder.txt_reqdate.text = mContext.resources.getString(R.string.date)
                viewHolder.txt_req_date.text =
                    LocaleHelper.arabicToEnglish(FormatingDate(itemPendingManualEntry.moveDate)!!)
                viewHolder.txt_fromtime.text = mContext.resources.getString(R.string.time_txt)
              //  viewHolder.txt_from_time.text = partDate[0] + ":" + partDate[1]
                viewHolder.txt_from_time.text = LocaleHelper.arabicToEnglish(FormatingTime(itemPendingManualEntry.moveTime))
            }
        }


            viewHolder.l_approve.setOnClickListener {
                when {
                    pendingPermissionRequestList != null && position < pendingPermissionRequestList!!.size -> {
                        Listener.onApproveItemClick(position, "P")
                    }
                    pendingManualRequestList != null && position < pendingManualRequestList!!.size -> {
                        Listener.onApproveItemClick(position, "M")
                    }
                    pendingLeaveRequestList != null && position < pendingLeaveRequestList!!.size -> {
                        Listener.onApproveItemClick(position, "L")
                    }
                }


            }
            viewHolder.l_reject.setOnClickListener {
                when {
                    pendingPermissionRequestList != null && position < pendingPermissionRequestList!!.size -> {
                        Listener.onRejectItemClick(position, "P")
                    }
                    pendingManualRequestList != null && position < pendingManualRequestList!!.size -> {
                        Listener.onRejectItemClick(position, "M")
                    }
                    pendingLeaveRequestList != null && position < pendingLeaveRequestList!!.size -> {
                        Listener.onRejectItemClick(position, "L")
                    }
                }
            }
    }




        override fun getItemCount(): Int {
            var size = 0
            if (pendingLeaveRequestList!!.size != 0) {
                size += pendingLeaveRequestList!!.size
            }
            if (pendingPermissionRequestList!!.size != 0) {
                size += pendingPermissionRequestList!!.size
            }
            if (pendingManualRequestList!!.size != 0) {
                size += pendingManualRequestList!!.size
            }

            return size
        }

        //  ViewHolder Class
        override fun getSwipeLayoutResourceId(position: Int): Int {
            return R.id.swipe
        }


        interface ClickListeners {
            fun onRowClick(view: View?, position: Int)
        }

        class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tv_action: TextView
            var txt_permission: TextView
            var txt_status: TextView
            var txt_req_date: TextView
            var txt_reqdate: TextView
            var txt_to_time: TextView
            var txt_from_time: TextView
            var txt_time: TextView
            var txt_reason: TextView


            var l_approve: LinearLayout
            var l_reject: LinearLayout

            var iv_approve: ImageView
            var l_reason: LinearLayout
            var l_time: LinearLayout
            var l_to_time: LinearLayout
            var l_from_time: LinearLayout
            var txt_type: TextView
            var txt_fromtime: TextView
            var txt_totime: TextView

            init {
                l_approve = itemView.findViewById(R.id.l_approve)
                l_reject = itemView.findViewById(R.id.l_reject)
                iv_approve = itemView.findViewById(R.id.iv_approve)
                l_reason = itemView.findViewById(R.id.l_reason)
                l_time = itemView.findViewById(R.id.l_time)
                l_to_time = itemView.findViewById(R.id.l_to_time)
                l_from_time = itemView.findViewById(R.id.l_from_time)

                txt_fromtime = itemView.findViewById(R.id.txt_fromtime)
                txt_totime = itemView.findViewById(R.id.txt_totime)

                txt_type = itemView.findViewById(R.id.txt_type)
                tv_action = itemView.findViewById<TextView>(R.id.tv_action)
                txt_permission = itemView.findViewById<TextView>(R.id.txt_permission)
                txt_status = itemView.findViewById<TextView>(R.id.txt_status)
                txt_req_date = itemView.findViewById<TextView>(R.id.txt_req_date)
                txt_from_time = itemView.findViewById<TextView>(R.id.txt_from_time)
                txt_to_time = itemView.findViewById<TextView>(R.id.txt_to_time)
                txt_time = itemView.findViewById<TextView>(R.id.txt_time)
                txt_reason = itemView.findViewById<TextView>(R.id.txt_reason)
                txt_reqdate = itemView.findViewById(R.id.txt_reqdate)


            }


        }



}