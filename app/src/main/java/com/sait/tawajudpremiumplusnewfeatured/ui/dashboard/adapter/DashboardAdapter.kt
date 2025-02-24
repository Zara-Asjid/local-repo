package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.databinding.DashboardItemViewBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardData
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class DashboardAdapter(
    private val dashboardList: ArrayList<DashboardData>,
    private val mContext: Context? = null

) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            DashboardItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }




    override fun getItemCount(): Int {
        return dashboardList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dashboarddata= dashboardList[position]
        holder.txtDescrip.text=dashboarddata.descrip
        holder.numbrtxt.text=dashboarddata.number.toString()
      when(LocaleHelper.getLanguage(mContext!!))
      {
          "ar"->{
              if (UserShardPrefrences.getCurrentTheme(mContext) == R.style.AppTheme_brown)
              {
                  holder.ll_view.setBackgroundResource(R.drawable.dashboard_br_bg_ar)
              }else
              if (UserShardPrefrences.getCurrentTheme(mContext) == R.style.AppTheme_green)
              {
                   holder.ll_view.setBackgroundResource(R.drawable.dashboard_gr_bg_ar)
              }
              else
              if (UserShardPrefrences.getCurrentTheme(mContext).equals(R.style.AppTheme_skyblue))
              {
                  holder.ll_view.setBackgroundResource(R.drawable.dashboard_blu_bg_ar)
              }
          }
          "en"->{
              if (UserShardPrefrences.getCurrentTheme(mContext) == R.style.AppTheme_brown)
              {
                  holder.ll_view.setBackgroundResource(R.drawable.dashboard_bg_br)
              }
              else
              if (UserShardPrefrences.getCurrentTheme(mContext) == R.style.AppTheme_green)
              {
                      holder.ll_view.setBackgroundResource(R.drawable.dashboard_bg)
                  }
              else
              if (UserShardPrefrences.getCurrentTheme(mContext).equals(R.style.AppTheme_skyblue))
              {
                          holder.ll_view.setBackgroundResource(R.drawable.dashboard_bg_b)
                      }
          }
      }
    }


    class ViewHolder(view: DashboardItemViewBinding) : RecyclerView.ViewHolder(view.root) {

        val txtDescrip: TextView = view.descripTxt
        val numbrtxt: TextView = view.numberTxt
        val ll_view :LinearLayoutCompat=view.llView
        val rlItemlayout: LinearLayoutCompat = view.swipe


    }
}