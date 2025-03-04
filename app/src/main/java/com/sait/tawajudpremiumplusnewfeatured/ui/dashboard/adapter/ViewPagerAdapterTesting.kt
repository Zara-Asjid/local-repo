package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.DashboardFragment_Admin
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.DashboardFragment_Manager
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.DashboardFragment_self

class ViewPagerAdapterTesting(mcontext : Context, supportFragmentManager: FragmentManager):
    FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList = ArrayList<Fragment?>() // Nullable to allow lazy loading
    private val fragmentTitleList = ArrayList<String>()
    private val mContext = mcontext
   /* override fun getItem(position: Int): Fragment {
        // Load the fragment only when the tab is selected
        return when (fragmentTitleList[position]) {
            mContext!!.getString(R.string.self_dashboard_txt) -> DashboardFragment_self()
            mContext!!.getString(R.string.manager_dashboard_txt) -> DashboardFragment_Manager()
            mContext!!.getString(R.string.admin_dashboard_txt) -> DashboardFragment_Admin()
            else -> throw IllegalStateException("Unknown tab selected")
        }
    }*/
   override fun getItem(position: Int): Fragment {
       return fragmentList[position] ?: Fragment() // Avoids creating unneeded fragments
   }

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = fragmentTitleList[position]

    fun addTab(title: String) {
        fragmentTitleList.add(title)
        fragmentList.add(Fragment())
        notifyDataSetChanged()
    }

    fun addFragment(fragment: Fragment?, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
        notifyDataSetChanged()
    }

    // Add fragment only when tab is selected
    fun loadFragmentAt(position: Int, fragment: Fragment) {
        if (fragmentList.size > position) {
            fragmentList[position] = fragment
            notifyDataSetChanged()
        }
    }
}


