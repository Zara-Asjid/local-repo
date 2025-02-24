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
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.FragmentHrApprovedRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.ApproveLeaveData
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryData
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionData
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Approve_Adapter_Hr(
    private var ListofLeaveRequest: ArrayList<ApproveLeaveData>,
    private var ListofPermissionRequest: ArrayList<ApprovePermissionData>,
    private var ListofManualEntryRequest: ArrayList<ApproveManualEntryData>,
    private val Listener: FragmentHrApprovedRequest,
    private var mContext: Context
) : RecyclerSwipeAdapter<Approve_Adapter_Hr.SimpleViewHolder>() {

    private var approvePermissionRequestList: ArrayList<ApprovePermissionData>? = null
    private var approveLeaveRequestList: ArrayList<ApproveLeaveData>? = null
    private var approveManualRequestList: ArrayList<ApproveManualEntryData>? = null


    init {
        this.approveLeaveRequestList = ListofLeaveRequest
        this.approveManualRequestList = ListofManualEntryRequest
        this.approvePermissionRequestList = ListofPermissionRequest
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

/*
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
                        .toTypedArray()[0])
            } catch (ex: ParseException) {
                ex.printStackTrace()
            }
        }
        return displayFormatTime.format(`time`)
    }
*/


    fun FormatingTime(input: String): String {
        // Display format to output only the time in HH:mm format
        val displayFormatTime = SimpleDateFormat("HH:mm")

        // Correct parsing format matching the input date-time format
        val parseFormatTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        var time: Date? = null
        try {
            // Parse the input string based on the ISO 8601 format
            time = parseFormatTime.parse(input)
        } catch (e: ParseException) {
            e.printStackTrace()  // Log the exception if parsing fails
        }

        // Format and return the time if parsing was successful, otherwise return an error message or default time
        return if (time != null) {
            displayFormatTime.format(time)
        } else {
            "Invalid time"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleViewHolder {
        val view: View
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.emp_approved_items, parent, false)

        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {



        if (approveLeaveRequestList!!.size != 0) {


            val items: ApproveLeaveData = approveLeaveRequestList!![position]

            viewHolder.txt_type.text = mContext.resources.getString(R.string.leave_type_txt)
            viewHolder.txt_totime.text =mContext.resources.getString(R.string.to_date_txt)
            viewHolder.txt_fromtime.text =mContext.resources.getString(R.string.from_date_txt)

            viewHolder.l_from_time.visibility = View.VISIBLE
            viewHolder.l_to_time.visibility = View.VISIBLE
            viewHolder.iv_approve.visibility = View.VISIBLE
            viewHolder.l_time.visibility = View.GONE
            viewHolder.l_reason.visibility = View.GONE


            if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
                viewHolder.tv_action.text = items.employeeNo+" "+items.employeeName
                viewHolder.txt_permission.text = items.leaveName
                viewHolder.txt_status.text = items.statusName

            }
            else{
                viewHolder.tv_action.text = items.employeeNo+" "+items.employeeArabicName
                viewHolder.txt_permission.text = items.leaveArabicName
                viewHolder.txt_status.text = items.statusNameArabic

            }

            viewHolder.txt_req_date.text = LocaleHelper.arabicToEnglish(FormatingDate(items.requestDate)!!)
            viewHolder.txt_from_time.text = LocaleHelper.arabicToEnglish(FormatingDate(items.fromDate)!!)
            viewHolder.txt_to_time.text = LocaleHelper.arabicToEnglish(FormatingDate(items.toDate)!!)






        }

        if (approvePermissionRequestList!!.size != 0) {





            val items: ApprovePermissionData = approvePermissionRequestList!![position]

                    viewHolder.txt_type.text = mContext.resources.getString(R.string.permission_txt)
                    viewHolder.iv_approve.visibility = View.VISIBLE
                    viewHolder.l_time.visibility = View.GONE
                    viewHolder.l_reason.visibility = View.GONE


            if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
                viewHolder.tv_action.text = items.employeeNo+" "+items.employeeName
                viewHolder.txt_permission.text = items.permName
                viewHolder.txt_status.text = items.statusName
            }

            else {

                viewHolder.tv_action.text = items.employeeNo + " " + items.employeeArabicName
                viewHolder.txt_permission.text = items.permArabicName
                viewHolder.txt_status.text = items.statusNameArabic
            }





                        viewHolder.txt_req_date.text = FormatingDate(items.permDate)


                    if(!items.isFullDay){
                        viewHolder.l_from_time.visibility = View.VISIBLE
                        viewHolder.l_to_time.visibility = View.VISIBLE
                        viewHolder.txt_from_time.text =LocaleHelper.arabicToEnglish(FormatingTime(items.fromTime))
                        viewHolder.txt_to_time.text = LocaleHelper.arabicToEnglish(FormatingTime(items.toTime))

                    }
                    else{
                        viewHolder.l_from_time.visibility = View.GONE
                        viewHolder.l_to_time.visibility = View.GONE
                    }




        }
        if (approveManualRequestList!!.size != 0) {




                viewHolder.iv_approve.visibility = View.VISIBLE
                viewHolder.l_to_time.visibility = View.GONE
                viewHolder.l_from_time.visibility = View.VISIBLE
            val itemPendingManualEntry: ApproveManualEntryData = approveManualRequestList!![position]
                val partMoveDate: List<String> = itemPendingManualEntry.createD_DATE.split("T")
                val partINTIME: List<String> = itemPendingManualEntry.moveTime.split("T")
                val partDate = partINTIME[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                    viewHolder.tv_action.text = itemPendingManualEntry.employeeNo+" "+itemPendingManualEntry.employeeName


                    viewHolder.txt_type.text = mContext.resources.getString(R.string.reason)
                    viewHolder.txt_permission.text = itemPendingManualEntry.reasonName
                    viewHolder.txt_reqdate.text = mContext.resources.getString(R.string.date)
                    viewHolder.txt_req_date.text =LocaleHelper.arabicToEnglish( FormatingDate(itemPendingManualEntry.moveDate)!!)
                    viewHolder.txt_fromtime.text=mContext.resources.getString(R.string.time_txt)
                   // viewHolder.txt_from_time.text = partDate[1] + ":" + partDate[0]

                    viewHolder.txt_from_time.text = LocaleHelper.arabicToEnglish(FormatingTime(itemPendingManualEntry.moveTime))
                    viewHolder.txt_status.text = itemPendingManualEntry.statusName
                } else {


                    viewHolder.tv_action.text = itemPendingManualEntry.employeeNo+" "+itemPendingManualEntry.employeeArabicName

                    viewHolder.txt_type.text = mContext.resources.getString(R.string.reason)
                    viewHolder.txt_permission.text = itemPendingManualEntry.reasonArabicName
                    viewHolder.txt_reqdate.text = mContext.resources.getString(R.string.date)
                    viewHolder.txt_req_date.text = LocaleHelper.arabicToEnglish(FormatingDate(itemPendingManualEntry.moveDate)!!)
                    viewHolder.txt_fromtime.text=mContext.resources.getString(R.string.time_txt)
                    viewHolder.txt_from_time.text = LocaleHelper.arabicToEnglish(FormatingTime(itemPendingManualEntry.moveTime))
                    viewHolder.txt_status.text = itemPendingManualEntry.statusNameArabic


            }

        }






    }


    override fun getItemCount(): Int {
        var size = 0
        if (approveLeaveRequestList!!.size != 0) {
            size += approveLeaveRequestList!!.size
        }
        if (approvePermissionRequestList!!.size != 0) {
            size += approvePermissionRequestList!!.size
        }
        if (approveManualRequestList!!.size != 0) {
            size += approveManualRequestList!!.size
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
        var txt_reqdate:TextView
        var txt_to_time: TextView
        var txt_from_time:TextView
        var txt_time: TextView
        var txt_reason: TextView




        var iv_approve: ImageView
        var l_reason:LinearLayout
        var l_time:LinearLayout
        var l_to_time:LinearLayout
        var l_from_time:LinearLayout
        var txt_type:TextView
        var txt_fromtime:TextView
        var txt_totime:TextView
        init {

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