package com.sait.tawajudpremiumplusnewfeatured

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentUserinfoBinding
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences


class UserInfoFragment : BaseFragment(),OnClickListener {
    private var _binding: FragmentUserinfoBinding? = null
    private val binding get() = _binding!!
    var handler: Handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUserinfoBinding.inflate(inflater, container, false)
        val activity = this.activity as MainActivity?
       // setTopbar(activity)
        if(UserShardPrefrences.getLanguage(activity).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        setClickListeners(activity)
        setData()

        binding.txtCustomerName.isSelected = true;
        binding.txtAdminEmail.isSelected = true;
        return binding.root

    }

    private fun setData() {


        if(UserShardPrefrences.getisHR(requireActivity()) || UserShardPrefrences.getisAdmin(requireActivity())){
            binding.lAlldetails.visibility = View.VISIBLE
        }
        else{
            binding.lAlldetails.visibility = View.GONE

        }


        binding.txtCustomerName.text = UserShardPrefrences.getName(activity!!)
        binding.txtCustomerShortName.text = UserShardPrefrences.getShortName(activity!!)
        binding.txtPackage.text = UserShardPrefrences.getPackage(activity!!)
        binding.txtAdminEmail.text = UserShardPrefrences.getEmail(activity!!)
        binding.txtReleaseNo.text = UserShardPrefrences.getReleaseNo(activity!!)
        binding.txtUsersAllowed.text = UserShardPrefrences.getNoUsers(activity!!)
        binding.txtDomainName.text = UserShardPrefrences.getDomain(activity!!)
        binding.txtAdminMobile.text = UserShardPrefrences.getMobile(activity!!)

        val startDate: List<String> =  UserShardPrefrences.getLicenseStartDate(activity!!)!!.split(" ")
        binding.txtLicenseStartDate.text = startDate[0]
        val endDate: List<String> =  UserShardPrefrences.getLicenseEndDate(activity!!)!!.split(" ")
        binding.txtLicenseEndDate.text = endDate[0]


    }

    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgInfo.setOnClickListener(this)
        activity?.binding!!.layout.imgAlert.setOnClickListener(this)
        activity?.binding!!.layout.imgBack.setOnClickListener(this)

    }

    private fun setTopbar(activity: MainActivity?) {
        (activity as MainActivity).show_BackButton_title(resources.getString(R.string.info))
     //   activity?.show_BackButton_title("Information")
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).hide_app_title()


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).show_BackButton_title(resources.getString(R.string.info))
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).hide_app_title()
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
    override fun onPause() {
        super.onPause()

    }

    override fun onClick(p0: View?) {

        when(p0!!.id){
            R.id.img_info->navigateToUserInfo()
            R.id.img_alert -> navigateToNotifications()
R.id.img_back -> (activity as MainActivity).onBackPressed()

        }
    }

    private fun navigateToNotifications() {

      //  Toast.makeText(activity,"zsdfghjkltygjh",Toast.LENGTH_SHORT).show()

    }

    private fun navigateToUserInfo() {

    }

}