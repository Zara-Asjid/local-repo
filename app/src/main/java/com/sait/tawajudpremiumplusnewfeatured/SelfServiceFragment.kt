package com.sait.tawajudpremiumplusnewfeatured

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.tabs.TabLayout
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentSelfServiceBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.RequestsFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.ViolationsFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.ViolationsPendingFragment
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences


class SelfServiceFragment : BaseFragment() , View.OnClickListener {

    private var _binding: FragmentSelfServiceBinding? = null
    private val binding get() = _binding!!
    var handler: Handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        _binding = FragmentSelfServiceBinding.inflate(inflater, container, false)
        val activity = this.activity as MainActivity?
        if(UserShardPrefrences.getLanguage(activity!!).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        setClickListeners(activity)
        setAdapters()

        return binding.root
    }



    override fun onResume() {
        super.onResume()


        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(requireActivity().resources.getString(R.string.self_service))

        if (GlobalVariables.from_background) {
            handler.post(checkInternetRunnable)
        }



    }

    private val checkInternetRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun run() {
            val application = requireActivity().application as TawajudApplication

            if (application.hasInternetConnection()) {
                Log.e("background_app", "Home fragment OnResume called")
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
        val adapter = activity?.let { ViewPagerAdapter(it.supportFragmentManager)

        }

      /*  val violationsFragment = ViolationsFragment()
        val args_violations = Bundle()
        args_violations.putString("from_date",  binding.txtFromDate.text.toString())
        args_violations.putString("to_date",  binding.txtToDate.text.toString())
        violationsFragment.arguments = args_violations


        val requestsFragment = RequestsFragment()
        val args_requests = Bundle()
        args_requests.putString("from_date",  binding.txtFromDate.text.toString())
        args_requests.putString("to_date",  binding.txtToDate.text.toString())
        requestsFragment.arguments = args_requests
*/
        GlobalVariables.updateRequest = false

        adapter!!.addFragment(ViolationsFragment(), resources.getString(R.string.violations))
        adapter!!.addFragment(ViolationsPendingFragment(), resources.getString(R.string.applied))
        adapter!!.addFragment(RequestsFragment(), resources.getString(R.string.requests))




      /*  if (binding.tabLayout.tabCount == 1) {
          //  val cardView = findViewById<MaterialCardView>(R.id.card_view)
            val layoutParams = binding.cardView.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.cardView.layoutParams = layoutParams
        } else {
            // Set card width back to wrap_content if there are multiple tabs
            //val cardView = findViewById<MaterialCardView>(R.id.card_view)
            val layoutParams = binding.cardView.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            binding.cardView.layoutParams = layoutParams
        }*/

// Setup TabLayout with ViewPager
     //   val adapter = MyPagerAdapter(supportFragmentManager)


        binding.pager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.pager)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab selected event
                if(tab!!.position.equals(1)){
                    // Handle tab selection

                    val selectedPosition = tab.position

                    // Get the existing fragment at the selected position
                    val selectedFragment = adapter.getItem(selectedPosition)

                    // Detach the existing fragment
                    fragmentManager!!.beginTransaction().detach(selectedFragment).commit()

                    // Perform any cleanup or additional logic if needed

                    // Attach the fragment back
                    fragmentManager!!.beginTransaction().attach(selectedFragment).commit()

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
    override fun onPause() {
        super.onPause()
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