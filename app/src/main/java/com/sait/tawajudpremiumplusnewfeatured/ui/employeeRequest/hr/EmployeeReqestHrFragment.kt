package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.tabs.TabLayout
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.ViewPagerAdapter
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentHrEmpRequestBinding
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class EmployeeReqestHrFragment: BaseFragment() , View.OnClickListener {

    private var _binding: FragmentHrEmpRequestBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    var handler: Handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        _binding = FragmentHrEmpRequestBinding.inflate(inflater, container, false)
        val activity = this.activity as MainActivity?
        mContext = inflater.context
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        setClickListeners(activity)
        setAdapters()


        return binding.root
    }


    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title( mContext!!.resources.getString(R.string.employee_request_txt))
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

    private fun setAdapters() {
        val adapter = activity?.let { ViewPagerAdapter(it.supportFragmentManager) }
        adapter!!.addFragment(FragmentHrPendingRequests(), mContext!!.resources.getString(R.string.pending))
        adapter!!.addFragment(FragmentHrApprovedRequest(),  mContext!!.resources.getString(R.string.approved))
        binding.hrPager.adapter = adapter
        binding.hrTabLayout.setupWithViewPager(binding.hrPager)
        binding.hrTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab selected event
                if(tab!!.position == 1){
                    // Handle tab selection
                    val selectedPosition = tab.position

                    // Get the existing fragment at the selected position
                    val selectedFragment = adapter?.getItem(selectedPosition)

                    // Detach the existing fragment
                    if (selectedFragment != null) {
                        fragmentManager!!.beginTransaction().detach(selectedFragment).commit()
                    }

                    // Perform any cleanup or additional logic if needed

                    // Attach the fragment back
                    if (selectedFragment != null) {
                        fragmentManager!!.beginTransaction().attach(selectedFragment).commit()
                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselected event
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselected event

            }
        })

    }


    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.img_back -> (activity as MainActivity).onBackPressed()



        }

    }




}