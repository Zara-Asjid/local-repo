package com.sait.tawajudpremiumplusnewfeatured.ui.announcements.adapter;

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.databinding.RowAnnouncementItemBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementData
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences.isItemRead
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences.markItemAsRead
import java.util.Calendar

class AnnouncementAdapter(
    private var employeeList: ArrayList<AnnouncementData>,
    private var mContext: Context? = null
) : RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {

    private var filteredList: ArrayList<AnnouncementData> = employeeList
    private var sharedPreferences :SharedPreferences? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = RowAnnouncementItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val announcementData = filteredList[position]

        holder.txtAnnouncementdate.text = announcementData.announcementDate
        holder.txtContent.text = announcementData.content_En
        holder.txtTitle.text = announcementData.title_En


        holder.txtContent.visibility = if (announcementData.isExpand) View.VISIBLE else View.GONE

        // Set expand/collapse button icon
        holder.img_expand.setImageResource(if (announcementData.isExpand) R.drawable.right_sq else R.drawable.down_sq)


    /*    holder.rel_layout.setBackgroundColor(
            if (announcementData.isRead) {
                ContextCompat.getColor(mContext!!, colorPrimary)
            } else {
                ContextCompat.getColor(mContext!!, R.color.red)
            }
        )*/
        val parts = announcementData.announcementDate!!.split("/")
        val month = parts[1].toInt()
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

        if (isItemRead(mContext!!, position,month)) {

            holder.rel_layout.setBackgroundResource(R.drawable.verylight_grey_rounded_rectangle)
          /*  holder.txtTitle.setTextColor(mContext!!.getColor(R.color.grey_light))
            holder.txtAnnouncementdate.setTextColor(mContext!!.getColor(R.color.grey_light))*/
        } else {
            holder.rel_layout.setBackgroundResource(R.drawable.lightgreen_rounded_rectangle)

        }

      /*  if(announcementData.isRead){
            holder.txtTitle.setTextColor(mContext!!.getColor(R.color.grey_dark))
            holder.txtAnnouncementdate.setTextColor(mContext!!.getColor(R.color.grey_dark))
        }
        else{
            holder.txtTitle.setTextColor(mContext!!.getColor(R.color.event_absent))
            holder.txtAnnouncementdate.setTextColor(mContext!!.getColor(R.color.event_absent))
        }*/
      sharedPreferences = mContext!!.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        holder.rel_layout.setOnClickListener {

            if (month == currentMonth){
                toggleContentVisibility(position)
                markAsRead(position)
                markItemAsRead(mContext!!, position,month)
                sharedPreferences!!.edit().putBoolean("item_${position}_month_$month", true).apply()
                getUnreadItemCount()
            } else {
                toggleContentVisibility(position)
                getUnreadItemCount()
                sharedPreferences!!.edit().apply {
                    putBoolean("item_${position}_month_$month", true)
                    apply()
                }
            }

        }


    }





    fun getUnreadItemCount(): Int {
        var unreadCount = 0
        sharedPreferences = mContext!!.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        for ((position, item) in filteredList.withIndex()) {
            val parts = item.announcementDate!!.split("/")
            val month = parts[1].toInt()
            if (month==currentMonth){
                if (!sharedPreferences!!.getBoolean("item_${position}_month_$month", false)) {
                    unreadCount += 1
                    UserShardPrefrences.saveCountAnnouncementsUnRead(mContext,unreadCount.toString())
                }
                if (unreadCount == 0)
                {
                    UserShardPrefrences.saveCountAnnouncementsUnRead(mContext,0.toString())
                }
            }else{
               unreadCount= UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!)!!.toInt()
            }
        }
        return unreadCount
    }

    private fun toggleContentVisibility(position: Int) {
        val announcementData = filteredList[position]
        announcementData.isExpand = !announcementData.isExpand

        notifyItemChanged(position)
    }

    private fun markAsRead(position: Int) {
        val currentItem = filteredList[position]
        currentItem.isRead = true

        // Notify any listeners or update UI as needed
    }
    class ViewHolder(private val viewBinding: RowAnnouncementItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val txtAnnouncementdate = viewBinding.txtAnnouncementdate
        val txtContent = viewBinding.txtContent
        val txtTitle = viewBinding.txtTitle
        val img_expand= viewBinding.imgExpand
val rel_layout = viewBinding.relLayout



    }

}