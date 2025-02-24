package com.sait.tawajudpremiumplusnewfeatured.ui.violations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.databinding.RowViolationItemBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.listener.ViolationItemClickListener
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationData
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class ViolationAdapter(
    private var violationList: ArrayList<ViolationData>,
    private val violationItemClickListener: ViolationItemClickListener,
    private var mContext: Context

) : RecyclerView.Adapter<ViolationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = RowViolationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return violationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val violationData = violationList[position]

        if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
            holder.txtaction.text = violationData.description
        }
        else{
            holder.txtaction.text = violationData.descriptionAr
        }

     /*   holder.txt_from_time.text = DateTime_Op.getDateFormat(violationData.permStart)
        holder.txt_to_time.text = violationData.permEnd.toString()
        holder.txtdate.text = DateTime_Op.getDateFormat(violationData.moveDate)*/

        holder.txtdate.text = LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.moveDate))
        holder.img_voilation.visibility=View.GONE

        if (violationData.type.equals("V", ignoreCase = true)) {
            holder.fromTime.text = mContext.resources.getString(R.string.from_time_txt)
            holder.toTime.text = mContext.resources.getString(R.string.to_time)
            holder.txtRequestType.text = mContext.resources.getString(R.string.permission_request)
            holder.txtPermRequestType.text = mContext.resources.getString(R.string.permission_request)

            holder.txt_from_time.text = violationData.permStart
            holder.txt_to_time.text =   violationData.permEnd

        }
      else if (violationData.type.equals("A", ignoreCase = true)) {
            holder.fromTime.text = mContext.resources.getString(R.string.from_date_colon_txt)
            holder.toTime.text = mContext.resources.getString(R.string.to_date_colon_txt)
            holder.txtRequestType.text =  mContext.resources.getString(R.string.leave_request_txt)
            holder.txtPermRequestType.text = mContext.resources.getString(R.string.leave_request_txt)


            holder.txt_from_time.text = LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permStartDate))
            holder.txt_to_time.text = LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permEndDate))

        }
      else  if (violationData.type.equals("P", ignoreCase = true)) {
            holder.fromTime.text = mContext.resources.getString(R.string.from_date_colon_txt)
            holder.toTime.text = mContext.resources.getString(R.string.to_date_colon_txt)
            holder.txtRequestType.text =  mContext.resources.getString(R.string.permission_request)
            holder.txtPermRequestType.text = mContext.resources.getString(R.string.permission_request)

            holder.txt_from_time.text = LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permStartDate))
            holder.txt_to_time.text = DateTime_Op.getDateFormat(violationData.permEndDate)
        }
       else if (violationData.type.equals("L", ignoreCase = true)) {
            holder.fromTime.text = mContext.resources.getString(R.string.from_date_colon_txt)
            holder.toTime.text = mContext.resources.getString(R.string.to_date_colon_txt)
            holder.txtRequestType.text = mContext.resources.getString(R.string.leave_request_txt)
            holder.txtPermRequestType.text = mContext.resources.getString(R.string.leave_request_txt)

            holder.txt_from_time.text = LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permStartDate))
            holder.txt_to_time.text = LocaleHelper.arabicToEnglish(DateTime_Op.getDateFormat(violationData.permEndDate))
        }

            else if (violationData.type.equals("M",ignoreCase = true)||violationData.type.equals("MS",ignoreCase = true)) {
            holder.txtRequestType.text = mContext.resources.getString(R.string.manual_entry_request)
            holder.txtPermRequestType.text = mContext.resources.getString(R.string.manual_entry_request)

            holder.fromTime.text = mContext.resources.getString(R.string.time_txt)
            holder.toTime.text = mContext.resources.getString(R.string.reason)
            holder.txt_from_time.text = violationData.permStart
            holder.txt_to_time.text = violationData.permType
        }


        if (violationData.fK_StatusId == null || violationData.fK_StatusId === -1) {
            holder.txt_permission_status.text = mContext.resources.getString(R.string.none_txt)
        }


        if (violationData.fK_StatusId == null || violationData.fK_StatusId == -1 || violationData.fK_StatusId==0) {
            holder.txt_permission_status.text = mContext.resources.getString(R.string.none_txt)
        } else {
            when (violationData.fK_StatusId) {
                1 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.pending).lowercase()

                2 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_approved_by_manager)

                3 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_approved_by_hr)

                4 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_rejected_by_manager)

                5 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_rejected_by_hr)

                6 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_approved_by_gm)

                7 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_rejected_by_gm)

                8 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_auto_approved)

                9 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_approved_by_1st_manager)

                10 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_deleted)

                11 -> holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_permission_converted_to_leave)

                12 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_approved_by_2nd_manager)

                13 ->  holder.txt_permission_status.text =
                    mContext.resources.getString(R.string.ss_rejected_by_2nd_manager)

            }
        }




        if (violationData.description.contains("Approved By Human Resource")) {
            holder.img_voilation.setImageResource(R.drawable.icon_approve)
        } else if (violationData.description.contains("Delay:") ||
            violationData.description
                .contains("التأخير:")
        ) {
            holder.img_voilation.setImageResource(R.drawable.icon_delay)
        } else if (violationData.description.contains("Early Out:") ||violationData.description
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
            holder.img_voilation.setImageResource(R.drawable.icon_pending)
        } else {
            val description: String = violationData.description
        }



       // holder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut


 /*       // Drag From Right
        holder.swipeLayout.addDrag(
            SwipeLayout.DragEdge.Right,
            holder.swipeLayout.findViewById<View>(R.id.bottom_wrapper)
        )*/


        // Handling different events when swiping


        // Handling different events when swiping
/*        holder.swipeLayout.addSwipeListener(object : SwipeListener {
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
        })*/


        if (violationData.description.equals("Approved By Human Resource",true)) {
        }




        holder.permission_icon.setOnClickListener{
            violationItemClickListener.onPermissionItemClick(violationData,holder.adapterPosition)
        }




    }

    class ViewHolder(view: RowViolationItemBinding) : RecyclerView.ViewHolder(view.root) {
        val txtaction: TextView = view.txtaction
        val txt_from_time: TextView = view.txtFromTime
        val txt_to_time: TextView = view.txtToTime
        val txtdate: TextView = view.txtdate
        val txt_permission_status: TextView = view.txtPermissionStatus
        val rlItemlayout: RelativeLayout = view.rlItemlayout
        val img_voilation :ImageView= view.imgVoilation
        val permission_icon: LinearLayoutCompat= view.lRequestManual

        val fromTime: TextView = view.fromTime
        val toTime: TextView = view.toTime

        val txtRequestType: TextView = view.txtRequestType
        val txtPermRequestType :TextView=view.txtPermRequest



    }
}