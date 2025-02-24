package com.sait.tawajudpremiumplusnewfeatured

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentProfileBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.AnnouncementsFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.home.HomeFragmentNew
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmpDetailsRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmployeeDetailsResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.employeeDetails.EmployeeViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences


class ProfileFragment : BaseFragment(), OnClickListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null
    private lateinit var viewModel_employee: EmployeeViewModel
    private var employee_no: String? = null
    var handler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel_employee = ViewModelProvider(this)[EmployeeViewModel::class.java]

        val activity = this.activity as MainActivity?
        mContext = inflater.context

        //  setTopbar(activity)
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }

        setClickListeners(activity)

        if (arguments != null) {
            employee_no = arguments?.getString("employee no")
            callEmployeeDetailsAPI(employee_no!!.toInt())
            binding.txtLogout.visibility = View.GONE

        } else {
            callEmployeeDetailsAPI(UserShardPrefrences.getUserInfo(mContext).fKEmployeeId)
            binding.txtLogout.visibility = View.VISIBLE

        }

        return binding.root

    }

    private fun callEmployeeDetailsAPI(employee_no: Int) {
        addObserver_employee()
        postEmployeeDetails(employee_no)
    }


    private fun postEmployeeDetails(employee_no: Int) {
        val employeeRequest = EmpDetailsRequest(
            employee_no,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            UserShardPrefrences.getUniqueId(mContext).toString()
        )
        viewModel_employee.getEmployeeData(mContext!!, employeeRequest)
    }

    private fun addObserver_employee() {
        viewModel_employee.employeeResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse

                            if (requestResponse != null) {
                                        setUserProfileData(requestResponse )


                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.caution,resources.getColor(R.color.red))

                            //SnackBarUtil.showSnackbar(context, message, false)

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }

    private fun setUserProfileData(requestResponse: EmployeeDetailsResponse) {
        binding.txtUsername.text = requestResponse.data.employeeName
        binding.txtUsernamearabic.text = requestResponse.data.employeeArabicName
        binding.txtEmpno.text=requestResponse.data.employeeNo
        binding.txtEmpCardNo.text=requestResponse.data.employeeCardNo
        binding.txtUserid.text=requestResponse.data.userID
        binding.txtEmail.text=requestResponse.data.email
        binding.txtRole.text=requestResponse.data.role
        binding.txtSchedule.text=requestResponse.data.scheduleName
        binding.txtJoinDate.text=requestResponse.data.joinDate
        binding!!.txtLetter.text =requestResponse.data.email.firstOrNull()?.toString()?.toUpperCase() ?: ""
        if (UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!).toString()!="0"&&
            UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!).toString()!="") {
            (activity as MainActivity).binding.layout.txtAnnouncementCount.visibility = View.VISIBLE
            (activity as MainActivity).binding.layout.txtAnnouncementCount.text =
                UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!).toString()
        }
        Glide.with(mContext!!).load(requestResponse.data.empImageURL).into(binding.imgUser);


        if(UserShardPrefrences.getLanguage(mContext).equals("0")){
            binding.txtDesignation.text = requestResponse.data.designationName
            binding.txtEntity.text=requestResponse.data.entityName
            binding.txtGrade.text=requestResponse.data.gradeName
            binding.txtManagerName.text=requestResponse.data.managerName
        }
        else{
            binding.txtDesignation.text = requestResponse.data.designationArabicName
            binding.txtEntity.text=requestResponse.data.entityArabicName
            binding.txtGrade.text=requestResponse.data.gradeArabicName
            binding.txtManagerName.text=requestResponse.data.managerArabicName
        }

    }


    private fun callstopCountdownTimerMethod() {
        val homeFragment = HomeFragmentNew()
        homeFragment?.stopHandler()
        homeFragment?.stopCountdownTimer()

    }

    private fun logout() {
        val alert = AlertDialog.Builder(
            requireContext()
        )
        val inflater_changepwd = LayoutInflater.from(context)
        val inflate: View = inflater_changepwd.inflate(R.layout.dialog_logout, null)
        alert.setView(inflate)
        val show_logout = alert.show()
        inflate.findViewById<View>(R.id.b_yes).setOnClickListener {

            if (show_logout != null) {

                callstopCountdownTimerMethod()
                UserShardPrefrences.setuserLogin(context, false)
                UserShardPrefrences.clearUserInfo(context)
                Log.d("MyApp", "167:profile")
                if (hasActiveNotifications()) {
                    cancelAllNotifications()
                    //stopService()
                }
                WorkManager.getInstance(mContext!!.applicationContext)
                    .cancelAllWork()
                PrefUtils.cancelLocationJob(mContext!!.applicationContext,1)
                startActivity(
                    Intent(
                        context,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                requireActivity().finish()
                show_logout.dismiss()
            }


        }

        inflate.findViewById<View>(R.id.b_no).setOnClickListener { show_logout?.dismiss() }
    }

    private fun stopService() {
        val serviceIntent = Intent(mContext, LocationService::class.java)
        mContext!!.stopService(serviceIntent)
    }


    private fun setClickListeners(activity: MainActivity?) {
        activity?.binding!!.layout.imgInfo.setOnClickListener(this)
        activity?.binding!!.layout.imgAlert.setOnClickListener(this)
        activity?.binding!!.layout.imgBack.setOnClickListener(this)

        binding.txtLogout.setOnClickListener(this)

    }
    private fun hasActiveNotifications(): Boolean {
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNotifications = notificationManager.activeNotifications
            return activeNotifications.isNotEmpty()
        }

        // For devices below API level 23, this method will assume notifications might exist
        // Alternatively, maintain your own flag/state to track posted notifications.
        return true
    }

    private fun cancelAllNotifications() {
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNotifications = notificationManager.activeNotifications
            for (statusBarNotification in activeNotifications) {
                notificationManager.cancel(statusBarNotification.id)
            }
        } else {
            notificationManager.cancelAll()
        }
    }
    private fun setTopbar(activity: MainActivity?) {
        (activity as MainActivity).show_BackButton_title(resources.getString(R.string.profile))
        (activity as MainActivity).show_alert()
        (activity as MainActivity).show_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).hide_app_title()


    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).show_BackButton_title(resources.getString(R.string.profile))
        (activity as MainActivity).show_alert()
        (activity as MainActivity).show_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).hide_app_title()


        if (GlobalVariables.from_background) {
            handler.post(checkInternetRunnable)
        }

    }

    private fun onRefresh() {
        if (arguments != null) {
            employee_no = arguments?.getString("employee no")
            callEmployeeDetailsAPI(employee_no!!.toInt())
            binding.txtLogout.visibility = View.GONE
        } else {
            callEmployeeDetailsAPI(UserShardPrefrences.getUserInfo(mContext).fKEmployeeId)
            binding.txtLogout.visibility = View.VISIBLE

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

    private fun setUserData() {
        binding.txtUsername.text = UserShardPrefrences.getUserInfo(context).employeeName
        binding.txtUsernamearabic.text = UserShardPrefrences.getUserInfo(context).employeeArabicName
        binding.txtDesignation.text = UserShardPrefrences.getUserInfo(context).role
        binding.txtEmpno.text = UserShardPrefrences.getUserInfo(context).employeeNo
        binding.txtEmail.text = UserShardPrefrences.getUserInfo(context).userEmail
        binding.txtUserid.text = UserShardPrefrences.getUserInfo(context).id.toString()
        binding.txtRole.text = UserShardPrefrences.getUserInfo(context).role
        binding.txtSchedule.text = UserShardPrefrences.getUserInfo(context).scheduleName



        binding.txtManagerName.text = UserShardPrefrences.getUserInfo(mContext).role
        binding.txtEntity.text = UserShardPrefrences.getEntityname(mContext!!)
        binding.txtJoinDate.text = UserShardPrefrences.getJoinDate(mContext!!)
        binding.txtGrade.text = UserShardPrefrences.getGrade(mContext!!)
        binding.txtEmpCardNo.text = UserShardPrefrences.getEmpCardId(mContext!!)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onPause() {
        super.onPause()

    }

    override fun onClick(p0: View?) {

        when (p0!!.id) {
            R.id.img_info -> navigateToUserInfo()
            R.id.img_alert -> navigateToNotifications()
            R.id.img_back -> (activity as MainActivity).onBackPressed()

            R.id.txt_logout -> logout()

        }
    }


    private fun navigateToNotifications() {
        replaceFragment(AnnouncementsFragment(), R.id.flFragment, true)

    }

    private fun navigateToUserInfo() {
        replaceFragment(UserInfoFragment(), R.id.flFragment, true)

    }

}