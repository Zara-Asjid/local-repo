package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentDashboardAllBinding
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class DashboardTesting : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentDashboardAllBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null

    private lateinit var customViewPager: CustomViewPager

    private val fragments: List<Fragment> = listOf(
        DashboardFragment_self(),
        DashboardFragment_Manager(),
        DashboardFragment_Admin()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardAllBinding.inflate(inflater, container, false)
        mContext = inflater.context

        customViewPager = binding.customViewPager.apply {
            setUp(childFragmentManager, R.id.customViewPager, fragments)  // Use customViewPager as the container
            tabChangeListener = { position ->
                binding.tabLayout.getTabAt(position)?.select()
            }
        }

        setTabLayout()
        setClickListeners(activity)

        return binding.root
    }

    private fun setTabLayout() {
        binding.tabLayout.apply {
            addTab(newTab().setText(getString(R.string.self_dashboard_txt)))
            if (UserShardPrefrences.getisManager(mContext)) {
                addTab(newTab().setText(getString(R.string.manager_dashboard_txt)))
            }
            if (UserShardPrefrences.getisAdmin(mContext)) {
                addTab(newTab().setText(getString(R.string.admin_dashboard_txt)))
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        customViewPager.navigateTo(it.position)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClickListeners(activity: Activity?) {
        val mainActivity = activity as? MainActivity
        mainActivity?.binding?.layout?.imgBack?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> (activity as MainActivity).onBackPressed()
        }
    }
}
