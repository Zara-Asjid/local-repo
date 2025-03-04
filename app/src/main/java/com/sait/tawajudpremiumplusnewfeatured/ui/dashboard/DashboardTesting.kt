package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentReportsBinding
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class DashboardTesting: BaseFragment(), View.OnClickListener {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null

    private var currentFragmentTag: String? = null
    private val childFragManager by lazy { childFragmentManager }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        mContext = inflater.context

        setTabLayout()
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
                    tab?.let { loadFragment(it.position) }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }

        // Load first tab by default
        loadFragment(0)
    }

    private fun loadFragment(position: Int) {
        val fragmentTag = when (position) {
            0 -> "SelfFragment"
            1 -> "ManagerFragment"
            2 -> "AdminFragment"
            else -> return
        }

        // Prevent reloading the same fragment
        if (currentFragmentTag == fragmentTag) return

        val fragment = when (position) {
            0 -> DashboardFragment_self()
            1 -> DashboardFragment_Manager()
            2 -> DashboardFragment_Admin()
            else -> return
        }

        val transaction = childFragManager.beginTransaction()
        childFragManager.fragments.forEach { transaction.hide(it) } // Hide previous fragments

        // Check if fragment exists, otherwise add it
        val existingFragment = childFragManager.findFragmentByTag(fragmentTag)
        if (existingFragment != null) {
            transaction.show(existingFragment)
        } else {
            transaction.add(R.id.fragmentContainer, fragment, fragmentTag)
        }

        transaction.commit()
        currentFragmentTag = fragmentTag
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> (activity as MainActivity).onBackPressed()
        }
    }
}