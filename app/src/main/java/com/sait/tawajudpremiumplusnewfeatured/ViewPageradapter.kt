package com.sait.tawajudpremiumplusnewfeatured

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter (supportFragmentManager: FragmentManager):
    FragmentStatePagerAdapter(supportFragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    private val fragmentList = ArrayList<Fragment?>() // Nullable to allow lazy loading
    private val fragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]!!
    }


    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = fragmentTitleList[position]

    fun addFragment(fragment: Fragment?, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
        notifyDataSetChanged() // Make sure ViewPager knows the data changed
    }

    // Add fragment only when tab is selected
    fun loadFragmentAt(position: Int, fragment: Fragment) {
        if (fragmentList.size > position && fragmentList[position] == null) {
            fragmentList[position] = fragment
            notifyDataSetChanged()
        }
    }




}