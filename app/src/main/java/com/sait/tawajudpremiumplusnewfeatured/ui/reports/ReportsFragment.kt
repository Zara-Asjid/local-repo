package com.sait.tawajudpremiumplusnewfeatured.ui.reports

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.tabs.TabLayout
import com.sait.tawajudpremiumplusnewfeatured.ViewPagerAdapter
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentReportsBinding
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class ReportsFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private var handler: Handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        mContext = inflater.context

        val activity = this.activity as MainActivity?
        if (UserShardPrefrences.getLanguage(mContext).equals("1")) {
            activity?.binding?.layout?.imgBack?.rotation = 180f
        }

        setClickListeners(activity)
        setAdapters()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val activity = this.activity as MainActivity
        activity.show_BackButton()
        activity.hide_alert()
        activity.hide_info()
        activity.hide_userprofile()
        activity.show_app_title(mContext!!.getString(R.string.reports_txt))

        if (GlobalVariables.from_background) {
            GlobalVariables.from_background = false
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
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun onRefresh() {
        // Your refresh logic here
    }

    private fun setAdapters() {
        val adapter = activity?.let { ViewPagerAdapter(it.supportFragmentManager) }

        adapter?.addFragment(Reports_SelfFragment(), mContext!!.getString(R.string.self_reports))

        if (UserShardPrefrences.getisManager(mContext)) {
            adapter?.addFragment(Reports_ManagerFragment(), mContext!!.getString(R.string.manager_reports))
        }

        if (UserShardPrefrences.getisAdmin(mContext)) {
            adapter?.addFragment(Reports_AdminFragment(), mContext!!.getString(R.string.admin_reports))
        }

        binding.pager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.pager)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // No need to detach/attach fragments here
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding?.layout?.imgBack?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> (activity as MainActivity).onBackPressed()
        }
    }
}
