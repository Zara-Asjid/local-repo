package com.sait.tawajudpremiumplusnewfeatured.ui.reports.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.databinding.RowSelfreportItemsBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.listener.ReportSelfItemClickListener
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.models.Reports_Self_Sevice_Data
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class SelfReportAdapter(
    private var selfReportList: ArrayList<Reports_Self_Sevice_Data>,
    private val reportSelfFragmentClickListener: ReportSelfItemClickListener,
    private var mContext: Context? = null

) : RecyclerView.Adapter<SelfReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            RowSelfreportItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return selfReportList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val violationData = selfReportList[position]


        if (UserShardPrefrences.getLanguage(mContext)!! == "0") {

            if(violationData.desc_En.contains("Self Service")) {
                holder.descTxt.text = violationData.desc_En.replace(" (Self Service)", "")
            }
            else  if(violationData.desc_En.contains("Manager")) {

                holder.descTxt.text = violationData.desc_En.replace(" (Manager)", "")

            }
            else{
                holder.descTxt.text = violationData.desc_En

            }
        } else {
            if(violationData.desc_Ar.contains("(الخدمات الذاتية)")) {
                holder.descTxt.text = violationData.desc_Ar.replace(" (الخدمات الذاتية)", "")
            }
            else  if(violationData.desc_Ar.contains("(المدير)")) {

                holder.descTxt.text = violationData.desc_Ar.replace(" (المدير)", "")

            }
            else{
                holder.descTxt.text = violationData.desc_Ar

            }
        }


        val imageUrl = "https://sgi.software/TawajudAPIs/images/logo.png"


       // DateTime_Op.removeApiSegment(UserShardPrefrences.getBaseUrl(mContext!!).toString()+"/images/"+violationData.reportimage)
//UserShardPrefrences.getBaseUrl(mContext).toString()
      //  Glide.with(mContext!!).load(imageUrl).into(holder.img)


     //   Glide.with(mContext!!).load("https://sgi.software/TawajudAPIs/images/logo.png").into(holder.img);

       if (violationData.desc_En.contains("Employee Daily") ||violationData.desc_Ar.contains("التقرير اليومي للموظف")) {
            holder.img.setBackgroundResource(R.drawable.daily_report1)
        }
       else if (violationData.desc_En .contains("Violation") || violationData.desc_Ar.contains("تقرير المخالفات") ) {
            holder.img.setBackgroundResource(R.drawable.violation)
        }
       else if (violationData.desc_En .contains("Employee In Out Transactions")||violationData.desc_Ar .contains("تقرير دخول وخروج الموظف")) {
            holder.img.setBackgroundResource(R.drawable.attendance_transaction_repot_icon)
        }
       else  if (violationData.desc_En .contains("Detailed Transactions") ||violationData.desc_En .contains("تقرير الحركات المفصلة") ) {            holder.img.setBackgroundResource(R.drawable.detail_report)
        }
       else  if (violationData.desc_En .contains("Employee Summary")||violationData.desc_En .contains("تقرير الموظف الملخص")) {
           holder.img.setBackgroundResource(R.drawable.summary_icon)
       }
        else{
            Glide.with(mContext!!).load("https://goo.gl/gEgYUd").into(holder.img);
        }

        /*  Glide.with(mContext)
              .load(violationData.img)
              .into(imageView);
  */



        holder.linearItemlayout.setOnClickListener {
            reportSelfFragmentClickListener.onReportItemClick(holder.adapterPosition)
        }



    }

    class ViewHolder(view: RowSelfreportItemsBinding) : RecyclerView.ViewHolder(view.root) {
        val descTxt: TextView = view.txtSelfreportItem

        val img: ImageView = view.imgSelfreportItem

        val linearItemlayout: LinearLayout = view.linearLayout
    }
}