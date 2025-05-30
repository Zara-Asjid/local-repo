package com.sait.tawajudpremiumplusnewfeatured.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.text.Spannable
import android.text.SpannableString
import android.text.format.DateFormat
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.mvvm_application.util.extension.toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.ProfileFragment
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.SelfServiceFragment
import com.sait.tawajudpremiumplusnewfeatured.UserInfoFragment
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentHomeBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.EmployeeReqestHRManagerFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.AdminEmployeeFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.AdminServicesFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.AnnouncementsFragment
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.b_custom_notification
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementData
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.viewmodels.AnnouncementViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.AttendanceFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.calendar.CalendarFragmentSelf
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.DashboardFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.*
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.currentDelay.CurrentDelayViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.employeeDetailsRegisterId.EmployeeRegisterViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.location.LocationViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.saveEntrytransaction.SaveEntryTransactionViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.transaction.TransactionViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.transaction_temp.TransactionTempViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.ManagerEmployeeFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.ReportsFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.RequestsFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.SummaryFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationData
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.b_delay
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.Keyboard_Op
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.GlobalHandler
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit
import android.location.Address
import android.location.Criteria
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ProcessLifecycleOwner
import com.sait.tawajudpremiumplusnewfeatured.LocationService
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.WorkLocations.WorkLocationViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.CheckInListener
import com.sait.tawajudpremiumplusnewfeatured.util.LocationSettingsReceiver
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.adapter.AnnouncementAdapter
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.DashboardTesting
import com.sait.tawajudpremiumplusnewfeatured.util.LocationServiceWorker
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.isSpoofed
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.isVpnActive
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.parseDoubleSafely

class HomeFragmentNew : BaseFragment(), OnClickListener,
    OnRefreshListener, CheckInListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationService: LocationService
    private lateinit var dFormat: DecimalFormat
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private lateinit var arrListCurrentDelay: ArrayList<ViolationData>
    private var permissionDeniedToastShown :Boolean=false
    private var b_face_valid: Boolean = false
    private var lastSettingsNavigationTime: Long = 0
    var isWithinRadius = false
    private var unReadCount : Int ?= null
    private lateinit var arrListViolation: ArrayList<AnnouncementData>

    // val handlernew = Handler(Looper.getMainLooper())

    val handlernew = Handler(Looper.getMainLooper())
    val handlerClick = Handler(Looper.getMainLooper())

    val delaynew: Long = 4 * 1000 // 4 seconds in milliseconds

    var runnable: Runnable? = null

    var handler: Handler = Handler()
    val delay: Long = 2 * 60 * 1000 // 2 minutes in milliseconds
    var countDown_Ticker: CountDownTimer? = null
    var countdown_mints: Long = 0

    private var unreadCount : Int ?=null

    private var mContext: Context? = null
    private lateinit var latitudear: String
    private lateinit var longitudear: String
    var latitude // latitude
            = 0.0
    var longitude // longitude
            = 0.0

    private lateinit var locationSettingsReceiver: LocationSettingsReceiver

    private lateinit var viewModel: LocationViewModel
    private lateinit var viewModel_announcements: AnnouncementViewModel
    private lateinit var viewModel_workLocations: WorkLocationViewModel

    private lateinit var viewModel_transaction_temp: TransactionTempViewModel
    private lateinit var viewModel_transaction: TransactionViewModel

    private lateinit var viewModel_employee: EmployeeRegisterViewModel
    private lateinit var viewModel_currentDelay: CurrentDelayViewModel
    private lateinit var viewModel_SaveEntry: SaveEntryTransactionViewModel

    private var b_start: Boolean = false
    private var b_enable_transaction: Boolean = false

    private var b_hasMobilePunch: Boolean = false
    private var b_mustPunchPhysical: Boolean = false
    private var b_allowMobileOutpunch: Boolean = false
    private var b_validateSecondIN: Boolean = false
    private var b_allowOnlyFirstINForCarPark: Boolean = false
    private var b_verification: Boolean = true
    private var b_carParkTimerNotification: Boolean = false


    private var workLocationId: Int = 0
    private var str_move_time: String? = null

    private var reasonId: Int = 0

    private var reasonStr_previous: String? = null
    private var reasonStr_current: String? = null


    private var allowedDistance: Int = 0
    private var mobilePunchConsiderDuration: Int = 0

    private var allowedGPSFlag: Int = 0
    var locationManager: LocationManager? = null

    var gpsCoordinates: String? = null

    var workLoc = Location("")
    val currentLocation = Location("")
    var str_work_loc: String? = null
    var finalDateMonth: String? = null
    var initialDateMonth: String? = null

    private var b_hasLastTransaction: Boolean = false
    private var b_hasLastTransaction_temp: Boolean = false
    private var b_autoSignIn: Boolean = false
    private var b_autoSignOut: Boolean = false



    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationUpdateHandler: Handler

    private var announcementAdapter: AnnouncementAdapter? = null
    private lateinit var arrListAnnouncements: ArrayList<AnnouncementData>
    var arrListWorkLocation = ArrayList<String>()

    private var b_show_out_office_dialog: Boolean = false
    private var b_from_temp_trans: Boolean = false
    private  val CHANNEL_ID = "location_service_channel"
    private var autocheck_in : Boolean=false


    var distance: Float = 0.0F
    @RequiresApi(Build.VERSION_CODES.O)
    private val myTask = Runnable {
        Log.e("After","This will be executed after 5 seconds")
        b_delay = true
        if (UserShardPrefrences.getMulipleLoc(context)==true
            && isAdded && !UserShardPrefrences.getUniqueId(context).toString().isEmpty()) {
             callWorkLocationAPI()
        }
               // callAutoSignInOut()
               // locationRelatedData()
    }
    private val checkInternetRunnable = object : Runnable {
        @SuppressLint("NewApi")
        @RequiresApi(Build.VERSION_CODES.M)
        override fun run() {
            val application = requireActivity().application as TawajudApplication

            if (application.hasInternetConnection()) {
                Log.e("internet:234", application.hasInternetConnection().toString())
                onRefresh()
                b_delay = false
                if (!b_delay) {
                    GlobalHandler.postDelayedTask(myTask, 3 * 1000)
                }
            } else {

                Log.e("internet:242", application.hasInternetConnection().toString())

                // Repeat the check after 1 second if there is no internet connection
                handler.postDelayed(this, 1000)
            }
        }
    }


  /*  private val checkInternetRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun run() {
            val application = requireActivity().application as TawajudApplication

            if (application.hasInternetConnection()) {
                Log.e("background_app", "Home fragment OnResume called")
                if (UserShardPrefrences.getMulipleLoc(context)==true) {
                    callWorkLocationAPI()
                }
                b_delay = false
                if (!b_delay) {
                    GlobalHandler.postDelayedTask(myTask, 5 * 1000)
                }
            } else {
                // Repeat the check after 1 second if there is no internet connection
                handler.postDelayed(this, 1000)
            }
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    private var requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
      /*  val fineLocationGranted = permissions.equals(Manifest.permission.ACCESS_FINE_LOCATION) ?: false
        val coarseLocationGranted = permissions.equals(Manifest.permission.ACCESS_COARSE_LOCATION) ?: false
        val backgroundLocationGranted = permissions.equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION) ?: false*/
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
      //  val backgroundLocationGranted = permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true
        val notificationsGranted = permissions[Manifest.permission.POST_NOTIFICATIONS] == true

        when {
            fineLocationGranted -> {
                // The user granted the fine location permission
                locationRelatedData()
                Log.e("currentLocation:249", locationCurrent.toString())


                Handler(Looper.getMainLooper()).postDelayed({
                    if (UserShardPrefrences.getMulipleLoc(context)==true) {
                        callWorkLocationAPI()
                    }
                }, 3000)


            }
            coarseLocationGranted -> {
                // The user granted the coarse location permission
                locationRelatedData()

                //  displayLocationToast(locationCurrent!!)
                Log.e("currentLocation:258", locationCurrent.toString())

                Handler(Looper.getMainLooper()).postDelayed({
                    if (UserShardPrefrences.getMulipleLoc(context)==true) {
                        callWorkLocationAPI()
                    }
                }, 3000)

            }
/*
            backgroundLocationGranted -> {
                // The user granted the background location permission
              //  displayLocationToast(locationCurrent!!)
                locationRelatedData()
                Log.e("currentLocation:267", locationCurrent.toString())

                if (UserShardPrefrences.getMulipleLoc(context)==true) {
                    callWorkLocationAPI()
                }
            }
*/
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)-> {
                // The user selected "Allow only this time" or denied the permission but didn't select "Don't ask again"
                toast("Location permission is required for the app to function correctly.")
            }
            else -> {
                if (!permissionDeniedToastShown) {
                    // The user denied the permission and checked "Don't ask again"
                    //toast("Location permission denied. Please enable it from the app settings.")
                    permissionDeniedToastShown = true

                }else{
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastSettingsNavigationTime > 3000) { // 3-second debounce time
                        lastSettingsNavigationTime = currentTime

                        if (notificationsGranted && !fineLocationGranted) {
                            openAppSettings()
                            toast("Location permission is required for the app to function correctly.")
                        } else if (!notificationsGranted && !fineLocationGranted) {
                            requestLocationPermission()
                        } else {
                            requestLocationPermission()
                        }
                    }

           /*     if(notificationsGranted && !fineLocationGranted){
                   openAppSettings()
                    toast("Location permission is required for the app to function correctly.")

                }else
                    if(!notificationsGranted && !fineLocationGranted){
                        requestLocationPermission()
                    }else
                {
                  requestLocationPermission()
                }
                  //  requestLocationPermissionsNew()
                             //toast("Location permission denied. Please enable it from the app settings.")
*/
                }



            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun callAutoSignInOut() {
/*
            if (allowedGPSFlag == 1 || allowedGPSFlag == 2) {
                Log.e("b_enable_transaction", "564")
                if (distance < UserShardPrefrences.getAllowedDistance(mContext)!!.toDouble()) {

                    Log.e("near_allowedDistance:294",  UserShardPrefrences.getAllowedDistance(mContext).toString())


                        if (b_autoSignIn && !b_hasLastTransaction && binding.txtInOut.text.equals("")) {
                            inClicked()
                        }
                } else {

                    if (b_autoSignOut && b_hasLastTransaction && binding.txtInOut.text.contains("IN")) {
                        outClicked()
                    }


                }

            }
*/

    }


    private val myTaskTransaction = Runnable {
        Log.e("After", "This will be executed after 3 seconds")
        callLastTransactionAPI(false)

    }

    @RequiresApi(Build.VERSION_CODES.O)



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        // Initialize ViewModels here
        viewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        viewModel_transaction = ViewModelProvider(this)[TransactionViewModel::class.java]
        viewModel_employee = ViewModelProvider(this)[EmployeeRegisterViewModel::class.java]
        viewModel_SaveEntry = ViewModelProvider(this)[SaveEntryTransactionViewModel::class.java]
        viewModel_currentDelay = ViewModelProvider(this)[CurrentDelayViewModel::class.java]
        viewModel_announcements = ViewModelProvider(this)[AnnouncementViewModel::class.java]
        viewModel_transaction_temp = ViewModelProvider(this)[TransactionTempViewModel::class.java]
        viewModel_workLocations = ViewModelProvider(this)[WorkLocationViewModel::class.java]

        // Initialize your LocationService
       // locationService = LocationService()

    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.lHorizontal.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Disallow SwipeRefreshLayout to intercept touch events

                    binding.swipeRefreshHome.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // Allow SwipeRefreshLayout to intercept touch events
                    binding.swipeRefreshHome.requestDisallowInterceptTouchEvent(false)

                }
            }
            false
        }

        setDate()


        arrListAnnouncements = arrayListOf()
        arrListWorkLocation = arrayListOf()

        val activity = this.activity as MainActivity?


        appversionChecks()


        mContext = requireActivity()
        setToolbar_Data(activity)
        setClickListeners(activity)
        //   locationRelatedData()
        //chceck the of onResume
        binding.txtViolation.isSelected = true;
        binding.txtInOut.isSelected = true;
        activity?.binding!!.layout.txtUserdesignation.isSelected = true
        activity?.binding!!.layout.txtUsername.isSelected = true
        binding.txtWelcome.isSelected= true
        Keyboard_Op.hide(mContext)

     /*   b_autoSignIn=  UserShardPrefrences.getNotificationData(mContext)[2].isActive
        b_autoSignOut=  UserShardPrefrences.getNotificationData(mContext)[1].isActive*/


        b_autoSignIn=  false
        b_autoSignOut=  false

        if (UserShardPrefrences.getisManager(mContext)||UserShardPrefrences.getisAdmin(mContext) ||UserShardPrefrences.getisHR(mContext)) {

            binding.lManger.visibility = View.VISIBLE
        } else {
            binding.lManger.visibility = View.GONE

        }

        if (UserShardPrefrences.getisManager(mContext)||UserShardPrefrences.getisHR(mContext)) {

            binding.lEmpRequests.visibility = View.VISIBLE
        } else {
            binding.lEmpRequests.visibility = View.GONE

        }


        if (UserShardPrefrences.getisAdmin(mContext)) {

            binding.lAdminServices.visibility = View.VISIBLE
        } else {
            binding.lAdminServices.visibility = View.GONE
        }
        if (hasActiveNotifications()) {
            cancelAllNotifications()
        }
        callLocationDetailsAPI(true)
        //  callWorkLocationAPI()
        //  callLastTransactionAPI()
         callEmployeeDetailsAPI(true)
        callCurrentDelayDetailsAPI(true)

        if (UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!) != null  &&
            UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!)!!.isNotEmpty() || UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!).toString() != ""&&
            UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!)!!.isNotEmpty()
        ) {


            if (UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!).toString() != "0") {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.VISIBLE

                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.text =
                    UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!).toString()
            } else
                if(UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!).equals("0")|| UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!).isNullOrEmpty()){
                    (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                        View.GONE
                }
        } else {
            callAnnouncementsAPI()
        }


        if(!b_delay) {
            GlobalHandler.postDelayedTask(myTask, 3 * 1000)
        }




       // startLocationService()


        /*createNotificationChannel()
        showLocalNotification()
*/
       //  showNotification(mContext!!, CHANNEL_ID, "Alert", "Please do transaction before it expires")
    }


    fun checkForChange(currentValue: String): Boolean {
        val hasChanged = currentValue != reasonStr_previous
        reasonStr_previous = currentValue
        return hasChanged
    }
    @SuppressLint("NotificationPermission")
    private fun showNotification(context: Context, channelId: String, title: String, message: String) {
        val notificationId = 1

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the NotificationChannel on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Channel Description"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.app_icon) // Replace with your app's notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }



    private fun setDate() {

        finalDateMonth = DateTime_Op.getFinalDateMonth("yyyy-MM-dd")
        initialDateMonth = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")

    }
    private fun callAnnouncementsAPI() {

        addObserver_announcements()
        getAnnouncementsDetails()
    }
    fun startHandler() {
        b_start = true
        handlernew.postDelayed(myRunnable, delaynew)
        Log.d("MyApp", "startHandler called")
    }
    fun stopHandler() {
        handlernew.removeCallbacks(myRunnable)
        Log.d("MyApp", "stopHandler called")
        b_start = false
        b_from_temp_trans = false
    }

    private val myRunnable = object : Runnable {

        override fun run() {

            try {
                if (activity != null) {
                    if (UserShardPrefrences.isUserLogin(requireActivity()) != null && UserShardPrefrences.isUserLogin(
                            context
                        )
                    ) {
                        if (UserShardPrefrences.isUserLogin(context) && b_start && !b_hasLastTransaction) {


                            if (GlobalVariables.from_background) {
                                GlobalHandler.postDelayedTask(myTaskTransaction, 3 * 1000)
                            }
                            else{
                                callLastTransactionAPI(false)
                            }


                            Log.d("LastTransaction:355", "false")
                        }
                        handlernew.postDelayed(this, delaynew)

                    }
                }

            } catch (e: Exception) {
                // Handle the exception

                Log.d("MyApp", e.toString())

            }

        }
    }
    private fun addObserver_announcements() {
        viewModel_announcements.announcementResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { managerEmpResponse ->
                            val managerEmpResponse = managerEmpResponse
                            if (managerEmpResponse.data != null && managerEmpResponse.data.isNotEmpty()) {


                                if (managerEmpResponse.message!!.contains(mContext!!.resources.getString(
                                        R.string.response_no_record_found_txt))) {

                                } else {



                                    if (managerEmpResponse.data != null && managerEmpResponse.data.isNotEmpty()) {
                                        arrListAnnouncements.clear()
                                        arrListAnnouncements.addAll(managerEmpResponse.data)
                                        if (arrListAnnouncements.size > 0) {

                                            announcementAdapter = mContext?.let { AnnouncementAdapter(arrListAnnouncements, mContext!!) }

                                            unreadCount  = announcementAdapter!!.getUnreadItemCount()
                                            if (unreadCount == 0) {
                                                UserShardPrefrences.saveCountAnnouncementsUnRead(
                                                    requireActivity(),
                                                    unreadCount.toString()
                                                )
                                                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                                                    View.GONE
                                                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.text =
                                                    arrListAnnouncements.size.toString()
                                            }else{
                                                UserShardPrefrences.saveCountAnnouncementsUnRead(
                                                    requireActivity(),
                                                    unreadCount.toString()
                                                )
                                                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                                                    View.VISIBLE
                                                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.text =
                                                    unreadCount.toString()

                                            }

                                        }
                                        else {
                                            (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                                                View.GONE

                                        }}

                                    //    setAdapterAnnouncements()

                                }

                            }
                        }
                    }


                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
                                message,
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )

                            //  SnackBarUtil.showSnackbar(context, message, false)

                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }


    private fun getAnnouncementsDetails() {
        val managerEmpRequest = AnnouncementRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            0,
            finalDateMonth!!,
            initialDateMonth!!
        )
        Log.e("commonRequest", managerEmpRequest.toString())

        viewModel_announcements.getAnnouncementData(mContext!!, managerEmpRequest)


    }
    private var locationCurrent: Location? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("LongLogTag")
    private fun locationRelatedData() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Initialize locationRequest
        locationRequest =
            LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1 * 1000) // Update interval in milliseconds (every 1 seconds)
        // .setInterval(1 * 1000 * 60 * 1)  // 1 mins

        // Initialize locationCallback

        Log.e("b_enable_transaction:459", "b4 LocationCallback")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationCurrent = locationResult.lastLocation

               /* for (location in locationResult.locations) {
                    checkDistanceFromLocation(location)
                }
*/
                Log.e("SpoofLoc", "the location is spoof : ${
                    isVpnActive(mContext!!)
                }")
                if (isAdded)
                {

                    Log.e("b_enable_transaction:467", "afr isAdded")

                    displayLocationToast(locationCurrent!!)
                } else {
                  /*  if (UserShardPrefrences.getLanguage(mContext).equals("1") &&
                        (allowedGPSFlag == 1 || allowedGPSFlag == 2 ||allowedGPSFlag==3)) {
                        binding.lDistance.visibility = View.GONE
                        binding.spoofingDetectionLl.visibility = View.VISIBLE
                        binding.txtSpoofDetect.text =
                            LocaleHelper.arabicToEnglish(
                                requireContext().resources.getString(
                                    R.string.spoof_detect_txt
                                )
                            )

                    }else{
                        if (allowedGPSFlag==1||allowedGPSFlag==2){
                            binding.lDistance.visibility = View.GONE
                            binding.spoofingDetectionLl.visibility = View.VISIBLE
                            binding.txtSpoofDetect.text =
                                requireContext().resources.getString(
                                    R.string.spoof_detect_txt
                                )
                        }else{
                            binding.spoofingDetectionLl.visibility = View.GONE
                        }


                    }*/

                        Log.e("b_enable_transaction:472", "else isAdded")

                }

            }
        }

        locationUpdateHandler = Handler(Looper.getMainLooper())

        // Check and request location permissions
        if (checkLocationPermission()) {
            //&&isGPSEnabled(requireContext())
            startLocationUpdates()

        } else {
            requestLocationPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED /*&& ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED*/)

    }

    @SuppressLint("ObsoleteSdkInt", "InlinedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }
        else
        {
            requestPermissionLauncher.launch(
                arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }
    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 and above, request both FINE_LOCATION and BACKGROUND_LOCATION
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // For Android 9 and below, only request FINE_LOCATION
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }*/
    }




    @SuppressLint("SuspiciousIndentation", "LongLogTag")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayLocationToast(location: Location) {

        Log.e("b_enable_transaction", "508")
        if (UserShardPrefrences.getLanguage(mContext).equals("1")) {
            dFormat = DecimalFormat("#,##0.000000")
            latitude = parseDoubleSafely(LocaleHelper.arabicToEnglish(location.latitude.toString()))
            longitude = parseDoubleSafely(LocaleHelper.arabicToEnglish(location.longitude.toString()))
           /* latitude =
                java.lang.Double.valueOf(LocaleHelper.arabicToEnglish(location.latitude.toString()))
            longitude =
                java.lang.Double.valueOf(LocaleHelper.arabicToEnglish(location.longitude.toString()))*/
        } else {
            dFormat = DecimalFormat("#.######")
            latitude = java.lang.Double.valueOf(dFormat.format(location.latitude))
            longitude = java.lang.Double.valueOf(dFormat.format(location.longitude))

        }

        sunRiseSet(latitude, longitude)
        Log.e("current_Location", "$latitude,$longitude")


        currentLocation!!.latitude = latitude
        currentLocation!!.longitude = longitude
        Log.e("currentLocation:829", currentLocation.toString())


        if (allowedGPSFlag == 3) {

            binding.lDistance.visibility = View.GONE
            binding.txtAnyWhere.visibility = View.GONE
            binding.txtLatLong.visibility = View.VISIBLE
            binding.txtLocName.visibility = View.VISIBLE
            //binding.spoofingDetectionLl.visibility = View.GONE
            if (latitude != null && longitude != null) {


                binding.txtLatLong.text = "$latitude,$longitude"
                binding.txtLocName.text = getLocationName(mContext!!, latitude, longitude)
            } else {

                binding.txtLatLong.text = mContext!!.resources.getString(R.string.not_available_txt)
                binding.txtLocName.text = mContext!!.resources.getString(R.string.not_available_txt)

            }
        } else {
            binding.lDistance.visibility = View.VISIBLE
            binding.txtAnyWhere.visibility = View.GONE
            binding.txtLatLong.visibility = View.VISIBLE
            binding.txtLocName.visibility = View.VISIBLE
           // binding.spoofingDetectionLl.visibility = View.GONE

        }


            if (UserShardPrefrences.getGpsCoordinates(mContext) != null && UserShardPrefrences.getGpsCoordinates(
                    mContext
                )!!.isNotEmpty() && allowedGPSFlag != 3
            ) {

                Log.e("b_enable_transaction", "531")
                binding.lDistance.visibility = View.VISIBLE
                binding.txtDistance.visibility = View.VISIBLE
                str_work_loc = UserShardPrefrences.getGpsCoordinates(mContext)
                val parts: List<String> = str_work_loc!!.split(",")
                workLoc.latitude = parts[0].toDouble()
                workLoc.longitude = parts[1].toDouble()
                  distance = getDistanceinMeter(workLoc, currentLocation!!)- UserShardPrefrences.getAllowedDistance(mContext)!!
                Log.e("distance", distance.toString())

                Log.e("b_enable_transaction", "540")
                Log.e("currentLocation:866", currentLocation.toString())

                //   var distance: Float = 3000f


                if (UserShardPrefrences.getLanguage(mContext).equals("1")) {
                    if (distance>0){
                        binding.txtDistance.text =
                            mContext!!.resources.getString(R.string.distance_txt) + LocaleHelper.arabicToEnglish(
                                String.format("%.2f", distance)
                            ).replace(",", ".", false) + requireContext().resources.getString(
                                R.string.meters_txt
                            )

                        Log.e("distance_972",String.format("%.2f", distance))


                    }else {
                            binding.txtDistance.text =
                                mContext!!.resources.getString(R.string.distance_txt) +LocaleHelper.arabicToEnglish(
                                    String.format("%.2f", 0.0F)
                                ).replace(",", ".", false) + requireContext().resources.getString(
                                    R.string.meters_txt
                                )
                        Log.e("distance_982",String.format("%.2f", distance))


                    }
                    /*   binding.txtDistance.text =
                        mContext!!.resources.getString(R.string.distance_txt) + LocaleHelper.arabicToEnglish(
                            String.format("%.2f", distance)
                        ).replace(",", ".", false) + requireContext().resources.getString(
                            R.string.meters_txt
                        )*/
                    Log.e("b_enable_transaction", "553")
                } else {
                    if (distance>0){
                        binding.txtDistance.text =
                            mContext!!.resources.getString(R.string.distance_txt) + String.format(
                                "%.2f", distance
                            ) + requireContext().resources.getString(
                                R.string.meters_txt
                            )

                        Log.e("distance_1002",String.format("%.2f", distance))


                    }else {
                            binding.txtDistance.text =
                                mContext!!.resources.getString(R.string.distance_txt) + String.format(
                                    "%.2f", 0.0F
                                ) + requireContext().resources.getString(
                                    R.string.meters_txt
                                )
                        Log.e("distance_1012",String.format("%.2f", distance))



                    }
                 /*   binding.txtDistance.text =
                        mContext!!.resources.getString(R.string.distance_txt) + String.format(
                            "%.2f", distance
                        ) + requireContext().resources.getString(
                            R.string.meters_txt
                        )*/
                    Log.e("b_enable_transaction", "558")
                }

                Log.e("b_enable_transaction_allowedGPSFlag:564", allowedGPSFlag.toString())

                if (allowedGPSFlag == 1 || allowedGPSFlag == 2) {
                    Log.e("b_enable_transaction $b_enable_transaction", "564")
                    if (distance < UserShardPrefrences.getAllowedDistance(mContext)!!.toDouble()) {


                        b_enable_transaction = true



                        Log.e("b_enable_transaction:566", b_enable_transaction.toString())


                    } else {

                        b_enable_transaction = false


                        Log.e("b_enable_transaction:574", b_enable_transaction.toString())


                    }

                }

                Log.e("b_enable_transaction", "599")

            } else {
                Log.e("b_enable_transaction", "601")
                binding.txtDistance.visibility = View.GONE
            }


    }



    fun englishToArabic(englishNumber: String): String {
        val arabicDigits = arrayOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
        val englishDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

        var arabicNumber = englishNumber
        for (i in arabicDigits.indices) {
            arabicNumber = arabicNumber.replace(englishDigits[i], arabicDigits[i])
        }

        return arabicNumber
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                mContext!!, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED


        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        }
        else {
            // Permissions are not granted, request them
            requestLocationPermission()
            Toast.makeText(
                requireContext(),
                "Location permissions are required for this app.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun callCurrentDelayDetailsAPI(b: Boolean) {
        arrListCurrentDelay = arrayListOf()
        addObserver_currentDelay(b)
        postCurrentDelay()

    }

    private fun postCurrentDelay() {
        val currDelayRequest = CurrentDelayRequest(
            UserShardPrefrences.getUserInfo(context).fKEmployeeId,
            LocaleHelper.arabicToEnglish(DateTime_Op.getCurrentDateTime("yyyy-MM-dd").toString()),
            LocaleHelper.arabicToEnglish(DateTime_Op.getCurrentDateTime("yyyy-MM-dd").toString()),
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )
        viewModel_currentDelay.getCurrentDelayData(mContext!!, currDelayRequest)
    }

    private fun addObserver_currentDelay(b: Boolean) {
        // arrListCurrentDelay.clear()
        if(view!=null) {
            viewModel_currentDelay.currentDelayResponse.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {


                            (activity as MainActivity).hideProgressBar()

                            response.data?.let { currDelayResponse ->
                                val currentDelayResponse = currDelayResponse

                                if (currentDelayResponse != null) {


                                    if (currentDelayResponse.message.equals(
                                            mContext!!.resources.getString(
                                                R.string.no_record_found_txt
                                            )
                                        )
                                    ) {/*  SnackBarUtil.showSnackbar(
                                          context,
                                          currentDelayResponse.message,
                                          false
                                      )*/
                                        binding.llNodelays.visibility = View.VISIBLE
                                        binding.txtWelcome.text =
                                            resources.getString(R.string.hi) + " " + UserShardPrefrences.getUserInfo(
                                                context
                                            ).employeeName
                                        binding.llDelays.visibility = View.GONE


                                    } else {
                                        if (currDelayResponse.data != null) {
                                            arrListCurrentDelay.clear()
                                            binding.llNodelays.visibility = View.GONE
                                            binding.llDelays.visibility = View.VISIBLE

                                            for (i in 0 until currentDelayResponse.data.size) {
                                                arrListCurrentDelay.addAll(
                                                    listOf(
                                                        currentDelayResponse.data[i]
                                                    )
                                                )
                                            }
                                            convertDateFormat(arrListCurrentDelay[0].inTime)
                                            binding.llNodelays.visibility = View.GONE
                                            binding.llDelays.visibility = View.VISIBLE


                                            if (arrListCurrentDelay[0].description.contains("Delay")) {
                                                binding.txtViolation.text =
                                                    mContext!!.resources.getString(R.string.you_have_delay_txt)

                                                /* binding.timeDelays.text =
                                             convertDateFormat(arrListCurrentDelay[0].inTime) + " - " + convertDateFormat(
                                               arrListCurrentDelay[0].moveDate
                                             )*/
                                                binding.timeDelays.text =
                                                    arrListCurrentDelay[0].permStart + " - " + arrListCurrentDelay[0].permEnd


                                                binding.txtDurationTime.text =
                                                    mContext!!.resources.getString(R.string.duration_home_txt)

                                                binding.durationTxt.text =
                                                    arrListCurrentDelay[0].duration.toString()

                                                binding.lRequestPermission.visibility = View.VISIBLE
                                                binding.lRequestManual.visibility = View.GONE


                                            } else if (arrListCurrentDelay[0].description.contains("Missing In")) {
                                                binding.txtViolation.text =
                                                    mContext!!.resources.getString(R.string.you_have_missing_in_txt)
                                                binding.timeDelays.text =
                                                    LocaleHelper.arabicToEnglish(
                                                        DateTime_Op.getDateFormat(
                                                            arrListCurrentDelay[0].moveDate
                                                        )
                                                    )
                                                binding.txtDurationTime.text =
                                                    mContext!!.resources.getString(
                                                        R.string.out_time_txt
                                                    )


                                                //   binding.durationTxt.text =DateTime_Op.simpleTimeConversion(arrListCurrentDelay[0].outTime)
                                                binding.durationTxt.text =
                                                    LocaleHelper.arabicToEnglish(
                                                        DateTime_Op.simpleTimeConversion(
                                                            arrListCurrentDelay[0].inTime
                                                        )
                                                    )


                                                binding.lRequestPermission.visibility = View.GONE
                                                binding.lRequestManual.visibility = View.VISIBLE
                                            } else if ((arrListCurrentDelay[0].description.contains(
                                                    "Missing Out"
                                                ))
                                            ) {
                                                binding.txtViolation.text =
                                                    mContext!!.resources.getString(R.string.missed_out_transaction_txt)

                                                binding.timeDelays.text =
                                                    LocaleHelper.arabicToEnglish(
                                                        DateTime_Op.getDateFormat(
                                                            arrListCurrentDelay[0].moveDate
                                                        )
                                                    )

                                                binding.txtDurationTime.text =
                                                    mContext!!.resources.getString(
                                                        R.string.in_time_txt
                                                    )
                                                // binding.durationTxt.text =DateTime_Op.simpleTimeConversion(arrListCurrentDelay[0].inTime)
                                                binding.durationTxt.text =
                                                    LocaleHelper.arabicToEnglish(
                                                        DateTime_Op.simpleTimeConversion(
                                                            arrListCurrentDelay[0].inTime
                                                        )
                                                    )

                                                binding.lRequestPermission.visibility = View.GONE
                                                binding.lRequestManual.visibility = View.VISIBLE


                                            } else if (arrListCurrentDelay[0].description.contains("Early Out")) {

                                                binding.txtViolation.text =
                                                    mContext!!.resources.getString(
                                                        R.string.early_out_txt
                                                    ) + LocaleHelper.arabicToEnglish(
                                                        DateTime_Op.getDateFormat(
                                                            arrListCurrentDelay[0].moveDate
                                                        )
                                                    )


                                                val date = LocaleHelper.arabicToEnglish(
                                                    DateTime_Op.getDateFormat(
                                                        arrListCurrentDelay[0].moveDate
                                                    )
                                                )

                                                // Find the index of the date in the complete text
                                                val indexOfDate =
                                                    binding.txtViolation.text.indexOf(date)

                                                // Create a SpannableString from the complete text
                                                val spannableString =
                                                    SpannableString(binding.txtViolation.text)

                                                // Apply the bold style to the specified range (from index of the date to the end of the date)
                                                spannableString.setSpan(
                                                    StyleSpan(Typeface.BOLD),
                                                    indexOfDate,
                                                    indexOfDate + date.length,
                                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                                )

                                                // Set the SpannableString to the TextView
                                                binding.txtViolation.text = spannableString


                                                binding.timeDelays.text =
                                                    arrListCurrentDelay[0].permStart + " - " + arrListCurrentDelay[0].permEnd


                                                binding.txtDurationTime.text =
                                                    mContext!!.resources.getString(
                                                        R.string.duration_home_txt
                                                    )

                                                binding.durationTxt.text =
                                                    arrListCurrentDelay[0].duration.toString()

                                                binding.lRequestPermission.visibility = View.VISIBLE
                                                binding.lRequestManual.visibility = View.GONE


                                            } else if (arrListCurrentDelay[0].description.contains("Out Time")) {
                                                binding.txtViolation.text =
                                                    mContext!!.resources.getString(R.string.out_duration_txt) + LocaleHelper.arabicToEnglish(
                                                        DateTime_Op.getDateFormat(
                                                            arrListCurrentDelay[0].moveDate,
                                                        )
                                                    )

                                                /* binding.timeDelays.text =
                                             convertDateFormat(arrListCurrentDelay[0].inTime) + " - " + convertDateFormat(
                                                 arrListCurrentDelay[0].moveDate
                                             )*/
                                                val date = LocaleHelper.arabicToEnglish(
                                                    DateTime_Op.getDateFormat(
                                                        arrListCurrentDelay[0].moveDate
                                                    )
                                                )

                                                // Find the index of the date in the complete text
                                                val indexOfDate =
                                                    binding.txtViolation.text.indexOf(date)

                                                // Create a SpannableString from the complete text
                                                val spannableString =
                                                    SpannableString(binding.txtViolation.text)

                                                // Apply the bold style to the specified range (from index of the date to the end of the date)
                                                spannableString.setSpan(
                                                    StyleSpan(Typeface.BOLD),
                                                    indexOfDate,
                                                    indexOfDate + date.length,
                                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                                )

                                                // Set the SpannableString to the TextView
                                                binding.txtViolation.text = spannableString


                                                binding.timeDelays.text =
                                                    arrListCurrentDelay[0].permStart + " - " + arrListCurrentDelay[0].permEnd

                                                binding.txtDurationTime.text =
                                                    mContext!!.resources.getString(
                                                        R.string.duration_home_txt
                                                    )

                                                binding.durationTxt.text =
                                                    arrListCurrentDelay[0].duration.toString()

                                                binding.lRequestPermission.visibility = View.VISIBLE
                                                binding.lRequestManual.visibility = View.GONE


                                            } else if (arrListCurrentDelay[0].description.contains("Non Violation")) {


                                                binding.llNodelays.visibility = View.VISIBLE
                                                binding.txtWelcome.text =
                                                    resources.getString(R.string.hi) + " " + UserShardPrefrences.getUserInfo(
                                                        context
                                                    ).employeeName
                                                binding.llDelays.visibility = View.GONE
                                            }

                                        }


                                    }
                                }

                            }
                        }

                        is Resource.Error -> {
                            (activity as MainActivity).hideProgressBar()
                            response.message?.let { message ->
                                //  toast(message)
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context, message, R.drawable.caution, resources.getColor(
                                        R.color.red
                                    )
                                )

                            }
                        }

                        is Resource.Loading -> {
                            if (b) {
                                (activity as MainActivity).showProgressBar()
                            }
                        }
                    }
                }
            }
        }
    }

    fun convertDateFormat(inputDate: String): String {
        // Define input and output date formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())

        try {
            // Parse the input date string
            val date = inputFormat.parse(inputDate)

            // Extract the time portion
            val time = outputFormat.format(date)

            return time
        } catch (e: Exception) {
            e.printStackTrace()
            return "" // Return an empty string in case of any error
        }
    }

    private fun callEmployeeDetailsAPI(b: Boolean) {
        addObserver_employee(b)
        postEmployeeDetails()
    }

    private fun postEmployeeDetails() {
        val employeeRequest = EmpDetailsRequest(
            UserShardPrefrences.getUserInfo(context).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            UserShardPrefrences.getUniqueId(mContext).toString()


        )
        viewModel_employee.getEmployeeRegisterData(mContext!!, employeeRequest)
    }

    private fun addObserver_employee(b: Boolean) {
        if(view!=null) {
            viewModel_employee.employeeResponse.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {


                            (activity as MainActivity).hideProgressBar()

                            response.data?.let { requestResponse ->
                                val requestResponse = requestResponse

                                if (requestResponse != null) {
//new
                                    if (requestResponse.data.error == null) {


                                        UserShardPrefrences.saveJoinDate(
                                            mContext, requestResponse.data.joinDate
                                        )
                                        UserShardPrefrences.saveEmpCardId(
                                            mContext, requestResponse.data.employeeCardNo
                                        )
                                        // Glide.with(mContext!!).load(requestResponse.data.empImageURL).into( (activity as MainActivity)?.binding!!.layout.imgUser);
                                        (activity as MainActivity)?.binding!!.layout.txtLetter.text =
                                            requestResponse.data.email.firstOrNull()?.toString()
                                                ?.toUpperCase() ?: ""


                                        if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                            UserShardPrefrences.saveEntityname(
                                                mContext, requestResponse.data.entityName
                                            )
                                            UserShardPrefrences.saveGrade(
                                                mContext, requestResponse.data.gradeName
                                            )

                                            (activity as MainActivity)?.binding!!.layout.txtUserdesignation.text =
                                                requestResponse.data.designationName


                                        } else {
                                            UserShardPrefrences.saveEntityname(
                                                mContext, requestResponse.data.entityArabicName
                                            )
                                            UserShardPrefrences.saveGrade(
                                                mContext, requestResponse.data.gradeArabicName
                                            )

                                            UserShardPrefrences.saveManagerId(
                                                mContext, requestResponse.data.managerID.toLong()
                                            )
                                            (activity as MainActivity)?.binding!!.layout.txtUserdesignation.text =
                                                requestResponse.data.designationArabicName


                                        }


                                    } else {
                                        UserShardPrefrences.setUniqueId(context,null)
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,
                                            requestResponse.data.error,
                                            R.drawable.app_icon,
                                            resources.getColor(
                                                R.color.red
                                            ),
                                            SnackBarUtil.OnClickListenerNew {

                                                UserShardPrefrences.setuserLogin(context, false)
                                                UserShardPrefrences.clearUserInfo(context)
                                                activity?.finishAffinity()


                                            })

                                        /*
                                                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,requestResponse.data.error,R.drawable.caution,resources.getColor(R.color.red),
                                                                            )
                                    */
                                    }


                                }


                            }
                        }

                        is Resource.Error -> {
                            (activity as MainActivity).hideProgressBar()
                            response.message?.let { message ->
                                //  toast(message)
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context, message, R.drawable.caution, resources.getColor(
                                        R.color.red
                                    )
                                )

                            }
                        }

                        is Resource.Loading -> {
                            if (b) {
                                (activity as MainActivity).showProgressBar()
                            }
                        }
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun callLocationDetailsAPI(b: Boolean) {
        addObserver(b)
        postLocationDetails()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun callWorkLocationAPI() {

        if(checkLocationPermission()&&isLocationEnabled()) {

            Log.e("internet:1487", requireActivity().application.hasInternetConnection().toString())

            addObserver_WorkLocation()
            postWorkLocation()
        }
else{
            if(checkLocationPermission()&&!isLocationEnabled()) {


                SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.enable_device_location),R.drawable.caution,resources.getColor(R.color.red),
                    SnackBarUtil.OnClickListenerNew {
                        openLocationSettings(mContext!!)

                    })
            }
        }

    }

    private fun postWorkLocation() {
        val workLocationRequest = WorkLocationRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),0,DateTime_Op.getCurrentDateTime("yyyy-MM-dd").toString(),
            DateTime_Op.getCurrentDateTime("yyyy-MM-dd").toString()

        )
        viewModel_workLocations.getWorkLocationData(mContext!!, workLocationRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("LongLogTag")
    private fun addObserver_WorkLocation() {

        if(view!=null){
            viewModel_workLocations.workLocationResponse.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            (activity as MainActivity).hideProgressBar()

                            response.data?.let { workLocationResponse ->
                                if (workLocationResponse.data != null && workLocationResponse.data.isNotEmpty()) {
                                    if (!workLocationResponse.message!!.contains(mContext!!.resources.getString(R.string.response_no_record_found_txt))) {

                                        arrListWorkLocation.clear()
                                        // arrListWorkLocation.addAll(workLocationResponse.data)




                                        // Check if the target location is within allowed distance of any location in workLocationResponse.data
                                        for (workLocation in workLocationResponse.data) {
                                            val location =
                                                parseCoordinates(workLocation.gpsCoordinates)
                                            val allowedDistance = workLocation.allowedDistance
                                            //   24.4275089, 54.5399144
                                            //24.497174, 54.367298
                                            /*  currentLocation!!.latitude = 24.4275089
                                              currentLocation!!.longitude = 54.5399144*/
                                            if (location != null) {
                                                val currentLat = currentLocation.latitude
                                                val currentLong = currentLocation.longitude
                                                Log.e("currentLocation:1477", currentLocation.toString())

                                                // Check if latitude and longitude are not empty or zero
                                                if (currentLat != 0.0 && currentLong != 0.0) {
                                                    distance = location.distanceTo(currentLocation)
                                                    if (distance <= allowedDistance) {
                                                        isWithinRadius = true
                                                        b_enable_transaction = true
                                                        if (UserShardPrefrences.getMulipleLoc(context)==true){
                                                            if (UserShardPrefrences.getAllowedGPSFlag(context) != 3) {
                                                                callLocationService(
                                                                    workLocation.allowedDistance,
                                                                    workLocation.gpsCoordinates,
                                                                    0
                                                                )
                                                            }
                                                        }
                                                        break
                                                    }
                                                }

                                                // Log distance for debugging
                                                Log.d(
                                                    "Distance Check",
                                                    "From ${location.latitude}, ${location.longitude} to ${currentLocation.latitude}, ${currentLocation.longitude}: $distance meters (allowed: $allowedDistance meters)"
                                                )
                                                Log.e("currentLocation:1503", currentLocation.toString())


                                            }
                                        }

                                        if (isWithinRadius) {
                                            Log.d("Location Check", "Target location is within allowed distance of at least one location.")
                                            // Additional logic if within allowed distance
                                            b_enable_transaction = true
                                            callLocationDetailsAPI(false)
                                            locationRelatedData()
                                            if (checkLocationPermission()&&isLocationEnabled()) {
                                                nearestLocation(workLocationResponse.data)
                                            }
                                            else{
                                                if(checkLocationPermission()&&!isLocationEnabled()) {
                                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.enable_device_location),R.drawable.caution,resources.getColor(R.color.red),
                                                        SnackBarUtil.OnClickListenerNew {
                                                            openLocationSettings(mContext!!)

                                                        })
                                                }

                                            }

                                            callAutoSignInOut()

                                        } else {
                                            Log.d("Location Check", "Target location is not within allowed distance of any location.")
                                            if(checkLocationPermission()&&isLocationEnabled()) {
                                                nearestLocation(workLocationResponse.data)
                                            }
                                            else{
                                                if(checkLocationPermission()&&!isLocationEnabled()) {
                                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.enable_device_location),R.drawable.caution,resources.getColor(R.color.red),
                                                        SnackBarUtil.OnClickListenerNew {
                                                            openLocationSettings(mContext!!)

                                                        })
                                                }

                                            }

                                            // Additional logic if not within allowed distance
                                        }

                                    }
                                }
                            }
                        }

                        is Resource.Error -> {
                            (activity as MainActivity).hideProgressBar()
                            response.message?.let { message ->
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context,
                                    message,
                                    R.drawable.caution,
                                    resources.getColor(R.color.red)
                                )
                            }
                        }

                        is Resource.Loading -> {
                            (activity as MainActivity).showProgressBar()
                        }
                    }
                }
            }
        }




    }

    private fun openLocationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun nearestLocation(data: List<WorkLocationData>) {
        var minDistance = Float.MAX_VALUE
        var nearestWorkLocation: WorkLocationData? = null
      //  b_enable_transaction = false
        Log.e("currentLocation:1616", currentLocation.longitude.toString())
        Log.e("currentLocation:1617", currentLocation.latitude.toString())

        if (currentLocation.latitude == 0.0 || currentLocation.longitude == 0.0) {
            Log.e("currentLocation:1618", currentLocation.toString())
        }
      else {
            distance =0.0F
            Log.e("currentLocation:1621", currentLocation.toString())
            for (workLocation in data) {
                val location =
                    parseCoordinates(workLocation.gpsCoordinates)
                distance =
                    location!!.distanceTo(currentLocation)  // Calculate the distance
                Log.e("currentLocation:1575", currentLocation.toString())

                /*  str_work_loc = UserShardPrefrences.getGpsCoordinates(mContext)
            val parts: List<String> = str_work_loc!!.split(",")
            workLoc.latitude = parts[0].toDouble()
            workLoc.longitude = parts[1].toDouble()*/

                if (distance < minDistance) {
                    minDistance = distance
                    nearestWorkLocation = workLocation


                    Log.e("near_workLocation", workLocation.workLocationName)
                    Log.e("near_distance", distance.toString())
                    Log.e("near_mindistance", minDistance.toString())
                    Log.e("near_nearestWorkLocation", nearestWorkLocation.toString())
                    Log.e("near_workLocation", workLocation.toString())
                    Log.e("near_allowedDistance", workLocation.allowedDistance.toString())


                    UserShardPrefrences.setAllowedDistance(
                        mContext, workLocation.allowedDistance
                    )
                    Log.e(
                        "near_allowedDistance:1678",
                        UserShardPrefrences.getAllowedDistance(mContext).toString()
                    )

                    workLocationId = workLocation.workLocationId
                    gpsCoordinates = workLocation.gpsCoordinates!!
                        .split(",") // Split the string by comma
                        .map { it.trim().toDouble() }.toString()

                    /*  workLoc.latitude = gpsCoordinates!![0]
                workLoc.longitude = gpsCoordinates!![1]*/
                    binding.txtLatLong.visibility = View.VISIBLE
                    binding.txtLatLong.text = workLocation.gpsCoordinates

                    UserShardPrefrences.saveGpsCoordinates(
                        mContext, workLocation.gpsCoordinates
                    )
                    binding.txtLocName.visibility = View.VISIBLE

                    if (UserShardPrefrences.getLanguage(mContext)
                            .equals("1")
                    ) {
                        binding.txtLocName.text =
                            workLocation.workLocationArabicName
                        binding.txtDistance.text =
                            mContext!!.resources.getString(R.string.distance_txt) + LocaleHelper.arabicToEnglish(
                                String.format("%.2f", distance)
                            ).replace(",", ".", false) + requireContext().resources.getString(
                                R.string.meters_txt
                            )
                    Log.e("distance_1793",String.format("%.2f", distance))
                    } else {
                        binding.txtLocName.text =
                            workLocation.workLocationName
                        binding.txtDistance.text =
                            mContext!!.resources.getString(R.string.distance_txt) + String.format(
                                "%.2f", distance
                            ) + requireContext().resources.getString(
                                R.string.meters_txt
                            )

                        Log.e("distance_1804",String.format("%.2f", distance))

                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callCheckIn_OutAPI(reason_id_manual: Int) {
        addObserver_check_in_out()
        callSaveManualEntry(reason_id_manual)
    }

    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun addObserver_check_in_out() {
        if (view != null) {
            viewModel_SaveEntry.saveEntrytransactionResponse.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            (activity as? MainActivity)?.hideProgressBar()

                            response.data?.let { requestResponse ->
                                val requestResponse = requestResponse
                                Log.e("requestResponse_check_in_out", response.data.toString())
                                if (requestResponse != null) {

                                    Log.e(
                                        "requestResponse_check_in_out",
                                        requestResponse.message.toString()
                                    )

                                    if (requestResponse.message.equals(
                                            requireContext().resources.getString(
                                                R.string.response_error_txt
                                            )
                                        )
                                    ) {
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                            requireContext(),
                                            requestResponse.data.msg,
                                            R.drawable.caution,
                                            requireContext().resources.getColor(R.color.red)
                                        )
                                    } else {
                                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                            requireContext(),
                                            requestResponse.data.msg,
                                            R.drawable.app_icon,
                                            colorPrimary
                                        )

                                        if (b_mustPunchPhysical && !b_hasLastTransaction) {
                                            if (allowedGPSFlag != 1) {
                                                Log.e(
                                                    "b_enable_transaction_allowedGPSFlag:1087",
                                                    allowedGPSFlag.toString()
                                                )

                                                callLastTransactionAPI(true)
                                                Log.d("LastTransaction:1155", "true")

                                            } else {
                                                PrefUtils.cancelLocationJob(mContext!!.applicationContext,1)
                                                UserShardPrefrences.setLastCallTime(mContext,0L)
                                                WorkManager.getInstance(mContext!!.applicationContext)
                                                    .cancelAllWork()
                                                UserShardPrefrences.setinHome(mContext,true)
                                                UserShardPrefrences.setinAttendance(mContext,false)
                                                b_custom_notification = false
                                                callLastTransactionTemp()
                                            }
                                        } else {
                                            callLastTransactionAPI(true)
                                            Log.d("LastTransaction:1162", "true")

                                        }
                                    }
                                }
                            }
                        }

                        is Resource.Error -> {
                            (activity as? MainActivity)?.hideProgressBar()
                            response.message?.let { message ->
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    requireContext(),
                                    message,
                                    R.drawable.caution,
                                    requireContext().resources.getColor(R.color.red)
                                )
                            }
                        }

                        is Resource.Loading -> {
                            (activity as? MainActivity)?.showProgressBar()
                        }
                    }
                }
            }
        }
    }


    private fun postLocationDetails() {

        val locationRequest = CommonRequest(
            UserShardPrefrences.getUserInfo(context).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )
        viewModel.getLocationData(mContext!!, locationRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callSaveManualEntry(reason_id_manual: Int) {


        val saveManualEntryRequest = SaveManualEntryRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            Instant.now().toString(),
            if (b_hasLastTransaction) {
                reason_id_manual
            } else {
                reasonId
            },

            Instant.now().toString(),
            "",
            "$latitude,$longitude",
            workLocationId,
            UserShardPrefrences.getAllowedGPSFlag(mContext),
            b_mustPunchPhysical,
            UserShardPrefrences.getUniqueId(mContext).toString(),
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),




            )
        Log.e("saveManualEntryRequest", saveManualEntryRequest.toString())

        viewModel_SaveEntry.getSaveEntryTransactionData(mContext!!, saveManualEntryRequest)


    }

    private fun callLastTransactionAPI(b_last_trans: Boolean) {/*  if (countDown != null) {
              countDown.cancel()
          }
  */

        addObserver_lasttransaction(b_last_trans)
        postTransactionDetails()


    }

    private fun postTransactionDetails() {


        if (UserShardPrefrences.isUserLogin(context)) {
            val transactionRequest = CommonRequest(
                UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
                UserShardPrefrences.getLanguage(mContext)!!.toInt()
            )
            viewModel_transaction.getTransactionData(mContext!!, transactionRequest)
        }


    }

    private fun postTransactionDetails_Temp() {
        val transactionRequest = CommonRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            //172,
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )
        viewModel_transaction_temp.getTransactionData(mContext!!, transactionRequest)

    }

    private fun callLastTransactionTemp() {
        addObserver_transaction_Temp()
        postTransactionDetails_Temp()

    }


    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addObserver(b: Boolean) {

        if(view!=null){
            viewModel.locationResponse.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            (activity as MainActivity).hideProgressBar()

                            response.data?.let { requestResponse ->
                                val requestResponse = requestResponse

                                if (requestResponse != null) {





                                    if(UserShardPrefrences.getMulipleLoc(context)==false) {

                                        if (requestResponse.workLocationId.toString() != "0") {
                                            UserShardPrefrences.saveWorkLocationId(
                                                mContext, requestResponse.workLocationId.toString()
                                            )

                                        }

                                        if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                            if (requestResponse.workLocationName != null) {
                                                UserShardPrefrences.saveWorkLocationName(
                                                    mContext, requestResponse.workLocationName
                                                ).toString()
                                            }
                                        } else {
                                            if (requestResponse.workLocationArabicName != null) {
                                                UserShardPrefrences.saveWorkLocationName(
                                                    mContext, requestResponse.workLocationArabicName
                                                ).toString()
                                            }
                                        }

                                        if (requestResponse.gpsCoordinates != null) {
                                            UserShardPrefrences.saveGpsCoordinates(
                                                mContext, requestResponse.gpsCoordinates
                                            )

                                            gpsCoordinates = requestResponse.gpsCoordinates!!
                                                .split(",") // Split the string by comma
                                                .map { it.trim().toDouble() }.toString()

                                        }

                                    }


                                    if (requestResponse.allowedGPSFlag != null) {

                                        allowedGPSFlag = requestResponse.allowedGPSFlag
                                        Log.e(
                                            "b_enable_transaction_allowedGPSFlag:1256",
                                            allowedGPSFlag.toString()
                                        )

                                        UserShardPrefrences.setAllowedGPSFlag(
                                            mContext, requestResponse.allowedGPSFlag
                                        )
                                    }

                                    UserShardPrefrences.setMustPhysicalPunch(
                                        mContext, requestResponse.mustPunchPhysical
                                    )

                                    UserShardPrefrences.setAllowedDistance(
                                        mContext, requestResponse.allowedDistance
                                    )
                                    b_hasMobilePunch = requestResponse.hasMobilePunch

                                    if (b_hasMobilePunch) {
                                        binding.switchInOut.visibility = View.VISIBLE
                                    } else {
                                        binding.switchInOut.visibility = View.GONE
                                        binding.lAttendance.visibility=View.GONE
                                    }
                                    b_mustPunchPhysical = requestResponse.mustPunchPhysical
                                    b_allowMobileOutpunch = requestResponse.allowMobileOutpunch
                                    b_validateSecondIN = requestResponse.validateSecondIN
                                    b_allowOnlyFirstINForCarPark =
                                        requestResponse.allowOnlyFirstINForCarPark
                                    mobilePunchConsiderDuration =
                                        requestResponse.mobilePunchConsiderDuration
                                    allowedDistance = requestResponse.allowedDistance
                                    allowedGPSFlag = requestResponse.allowedGPSFlag
                                    Log.e(
                                        "b_enable_transaction_allowedGPSFlag:1288",
                                        allowedGPSFlag.toString()
                                    )



                                    if(UserShardPrefrences.getMulipleLoc(context)==false) {
                                        workLocationId = requestResponse.workLocationId

                                        if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                            if (requestResponse.workLocationName != null && requestResponse.workLocationName.isNotEmpty()) {
                                                binding.txtLocName.text =
                                                    requestResponse.workLocationName
                                                UserShardPrefrences.saveWorkLocationName(
                                                    mContext, requestResponse.workLocationName
                                                ).toString()

                                            }
                                        } else {
                                            if (requestResponse.workLocationArabicName != null && requestResponse.workLocationArabicName.isNotEmpty()) {
                                                binding.txtLocName.text =
                                                    requestResponse.workLocationArabicName
                                                UserShardPrefrences.saveWorkLocationName(
                                                    mContext, requestResponse.workLocationArabicName
                                                ).toString()

                                            }


                                        }
                                        binding.txtLatLong.text = requestResponse.gpsCoordinates
                                        UserShardPrefrences.saveGpsCoordinates(
                                            mContext, requestResponse.gpsCoordinates
                                        )

                                    }

                                    when (requestResponse.allowedGPSFlag) {
                                        1 -> {
                                            binding.txtWorkMode.text = mContext!!.resources.getString(
                                                R.string.car_punch_in_txt
                                            )
                                            binding.lDistance.visibility = View.VISIBLE
                                            binding.txtLocName.visibility = View.VISIBLE
                                            binding.txtLatLong.visibility = View.VISIBLE
                                            binding.txtAnyWhere.visibility = View.GONE
//if(!b_hasLastTransaction&&!b_timer_stop)
                                            if (!b_hasLastTransaction) {
                                                callLastTransactionTemp()
                                            }
                                        }

                                        2 -> {
                                            binding.txtWorkMode.text =
                                                mContext!!.resources.getString(R.string.remote_work_txt)
                                            //  b_enable_transaction = true
                                            binding.lDistance.visibility = View.VISIBLE
                                            binding.txtLocName.visibility = View.VISIBLE
                                            binding.txtLatLong.visibility = View.VISIBLE
                                            binding.txtAnyWhere.visibility = View.GONE

                                        }

                                        3 -> {
                                            binding.txtWorkMode.text =
                                                mContext!!.resources.getString(R.string.romers_txt)
                                            b_enable_transaction = true
                                            Log.e(
                                                "b_enable_transaction:1303",
                                                b_enable_transaction.toString()
                                            )

                                            binding.lDistance.visibility = View.GONE
                                            binding.txtLocName.visibility = View.GONE
                                            binding.txtLatLong.visibility = View.GONE
                                            binding.txtAnyWhere.visibility = View.VISIBLE
                                            binding.txtAnyWhere.text =
                                                mContext!!.resources.getString(R.string.not_available_txt)


                                        }
                                    }


                                    if(UserShardPrefrences.getMulipleLoc(context)==false) {
                                        if (requestResponse.allowedGPSFlag != 3) {
                                            callLocationService(
                                                requestResponse.allowedDistance,
                                                requestResponse.gpsCoordinates,
                                                0
                                            )
                                        }
                                    }

                                    locationRelatedData()
                                    if (requestResponse.allowedGPSFlag == 1) {
                                        callLastTransactionAPI(false)
                                    } else {
                                        callLastTransactionAPI(true)
                                    }
                                    Log.d("LastTransaction:1409", "true")

                                  /*  if(requestResponse.allowedGPSFlag!=3) {
                                        setAutoSignInOut()
                                    }*/

                                    //  SnackBarUtil.showSnackbar(context, requestResponse.toString(), false)

                                }


                            }
                        }

                        is Resource.Error -> {
                            (activity as MainActivity).hideProgressBar()
                            response.message?.let { message ->
                                //  toast(message)
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context,
                                    message,
                                    R.drawable.caution,
                                    resources.getColor(R.color.red)
                                )

                            }
                        }

                        is Resource.Loading -> {
                            if(b) {
                                (activity as MainActivity).showProgressBar()
                            }
                        }
                    }
                }
            }
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun callLocationServiceCarParkTimer(
        allowedDistance: Int,
        gpsCoordinates: String,
        notificationTypeId: Int?,
        minutes: String
    ) {
        if (ContextCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            /*val intent = Intent(mContext!!.applicationContext, LocationServiceBackUp::class.java)
            intent.putExtra("distance", allowedDistance.toInt())
            intent.putExtra("gpsCoordinates", gpsCoordinates)
            intent.putExtra("tawajud_NiotificationsTypesId",notificationTypeId)
            intent.putExtra("timer_txt", minutes)
            intent.putExtra("Last_transactionStatus", b_hasLastTransaction)
            intent.putExtra("car_park_timer", true)
           //context?.startForegroundService(intent)
           ContextCompat.startForegroundService(requireContext().applicationContext, intent)
*/
            if (UserShardPrefrences.isUserLogin(mContext)){
                scheduleLocationService(
                    allowedDistance,
                    gpsCoordinates,
                    notificationTypeId,
                    minutes,
                    b_hasLastTransaction ?: false,
                    true
                )
            }

            /*     try {
                     if(!GlobalVariables.isAppInForeground) {
                         Log.d("LocationService", "Service start attempted")
                     }else{
                         ContextCompat.startForegroundService(requireContext().applicationContext, intent)
                     }
                 } catch (e: Exception) {
                     Log.e("LocationService", "Failed to start service: ${e.message}")
                 }*/
            // requireContext().startService(intent)


        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION
            )

        }
    }




    private fun callLocationService(allowedDistance: Int, gpsCoordinates: String,notificationTypeId :Int?) {
      /*  if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val intent = Intent(mContext!!, LocationService::class.java)
            intent.putExtra("distance", allowedDistance.toInt())
            intent.putExtra("gpsCoordinates", gpsCoordinates)
            intent.putExtra("tawajud_NiotificationsTypesId",notificationTypeId)
            intent.putExtra("timer_txt", binding.txtTime.text)
            intent.putExtra("Last_transactionStatus", b_hasLastTransaction)
            ContextCompat.startForegroundService(mContext!!, intent)
            intent.putExtra("car_park_timer", false)

        }
     else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION
            )

        }*/
    }


    private fun createNotificationChannel() {
        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Location Service"
            val descriptionText = "Please do transaction before it expires"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Request POST_NOTIFICATIONS permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE
                )
            }
        }
    }


    private fun addObserver_lasttransaction(b_last_trans: Boolean) {
        if (view != null){
            viewModel_transaction.transactionResponse.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {

                            (activity as MainActivity).hideProgressBar()

                            Log.e("  response.data",  response.data.toString())
                            response.data?.let { requestResponse ->
                                val requestResponse = requestResponse

                                if (requestResponse != null) {
                                    // showNotification(mContext!!, channelId, "Alert", "Please do transaction before it expires")
                                    if (requestResponse.data != null) {

                                        b_hasLastTransaction = true
                                        b_hasLastTransaction_temp = false
                                        binding.txtTime.text = requestResponse.data[0].moveTime
                                        str_move_time = requestResponse.data[0].moveTime
                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                        val parse = sdf.parse(requestResponse.data[0].moveDate)
                                        if (parse != null) {
                                            val instance = Calendar.getInstance()
                                            instance.time = parse
                                            binding.txtDate.text =
                                                DateFormat.format("dd MMM yyyy", parse).toString()
                                        }
                                        reasonId = requestResponse.data[0].reasonId
                                        reasonStr_current = requestResponse.data[0].type

                                        if (requestResponse.data[0].expectOutValue != null) {
                                            binding.txtExpectedout.text =
                                                requestResponse.data[0].expectOutValue
                                        } else {
                                            //binding.txtExpectedout.text = "---"
                                        }


                                        //  if (requestResponse.data[0].reasonEn == "IN") {

                                        if (requestResponse.data[0].type == "I") {


                                            selectInClickedPermanent()
                                            Log.e("testHome","1296")
                                        } else {

                                            selectOutClickedPermanent()
                                            Log.e("testHome","1303")

                                        }


                                        if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                           // binding.txtInOut.text = requestResponse.data[0].reasonEn
                                            binding.txtInOut.text = requestResponse.data[0].firstIN
                                            if (requestResponse.data[0].lastOUT!=null){
                                                binding.txtLastOut.text = requestResponse.data[0].lastOUT
                                            }else{
                                                binding.txtLastOut.text = ""
                                            }

                                            if (requestResponse.data[0].reasonEn.contains("IN")){
                                                binding.txtLastlocation.text= PrefUtils.addInWord(mContext!!,binding.txtLastlocation.text.toString(),requestResponse.data[0].reasonEn)
                                            }else{
                                                binding.txtLastlocation.text= PrefUtils.addOutWord(mContext!!,binding.txtLastlocation.text.toString(),requestResponse.data[0].reasonEn)

                                            }
                                        } else {
                                           // binding.txtInOut.text = requestResponse.data[0].reasonAr
                                            binding.txtInOut.text = requestResponse.data[0].firstIN
                                            if (requestResponse.data[0].lastOUT!=null){
                                                binding.txtLastOut.text = requestResponse.data[0].lastOUT
                                            }else{
                                                binding.txtLastOut.text = ""
                                            }

                                            if (requestResponse.data[0].reasonEn.contains("IN")){
                                                binding.txtLastlocation.text= PrefUtils.addInForArabic(mContext!!,binding.txtLastlocation.text.toString())
                                            }else{
                                                binding.txtLastlocation.text= PrefUtils.addOutForArabic(mContext!!,binding.txtLastlocation.text.toString())

                                            }

                                        }
                                        b_from_temp_trans= false
                                        stopCountdownTimer()
                                        binding.txtTimer.visibility = View.GONE


                                        if (hasActiveNotifications()) {
                                            cancelAllNotifications()
                                        }
                                    } else {


                                        if(!b_from_temp_trans) {
                                            binding.txtInOut.text = ""
                                            reasonStr_current = null
                                            reasonId = 0
                                            //binding.txtExpectedout.text = "---"
                                            binding.txtDate.text =
                                                mContext!!.resources.getString(R.string.not_available_txt)
                                            binding.txtTime.text =
                                                mContext!!.resources.getString(R.string.no_time_txt)
                                            binding.txtLastlocation.text= resources.getString(R.string.last_trasaction_txt)
                                            binding.txtInOut.text = ""
                                            binding.txtLastOut.text=""
                                        }


                                        b_hasLastTransaction = false
                                        reasonId = 0
                                        reasonStr_current = ""
                                       // b_hasLastTransaction_temp = false
                                       // callAutoSignInOut()
                                        // callLastTransactionTemp()
                                        /*
                                                                            SnackBarUtil.showSnackbar(
                                                                                context,
                                                                                requestResponse.message.toString(),
                                                                                false
                                                                            )
                                        */


                                        //date,time,in selection,you are in txt
                                        /*    binding.txtDate.text = "Not Available"
                                            binding.txtTime.text = "--:--"
                                            binding.txtInOut.text= ""*/



                                        /* if (((binding.txtWorkMode.text.equals( mContext!!.resources.getString(R.string.car_punch_in_txt))||binding.txtWorkMode.text.equals(
                                                 mContext!!.resources.getString(R.string.remote_work_txt))||binding.txtWorkMode.text.equals(
                                                 mContext!!.resources.getString(R.string.romers_txt))) && !b_hasLastTransaction && !b_hasLastTransaction_temp)) {*/

                                        if(!b_from_temp_trans){
                                            if ((binding.txtWorkMode.text.equals(resources.getString(
                                                    R.string.car_punch_in_txt)) && !b_hasLastTransaction && !b_hasLastTransaction_temp)) {

                                                binding.txtIn.setBackgroundResource(R.drawable.green_rectangle_in)
                                                //  binding.txtIn.setBackgroundResource(R.drawable.red_rectangle_out_inactive)
                                                binding.txtOut.setBackgroundResource(R.drawable.red_rectangle_out_inactive)
                                                binding.txtOut.setTextColor(resources.getColor(R.color.gray))
                                                binding.txtIn.setTextColor(resources.getColor(R.color.gray))

                                                Log.e("testHome","1344")


                                            } else {
                                                binding.txtIn.setBackgroundResource(R.drawable.green_rectangle_in)
                                                binding.txtOut.setBackgroundResource(R.drawable.red_rectangle_out)
                                                binding.txtOut.setTextColor(resources.getColor(R.color.grey))
                                                binding.txtIn.setTextColor(resources.getColor(R.color.grey))
                                                Log.e("testHome","1352")

                                            }

                                        }

                                    }


                                }

                            }
                        }

                        is Resource.Error -> {
                            (activity as MainActivity).hideProgressBar()
                            response.message?.let { message ->
                                //  toast(message)


                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                    context,
                                    message,
                                    R.drawable.caution,
                                    resources.getColor(R.color.red)
                                )
                            }
                        }

                        is Resource.Loading -> {
                            Log.d("LastTransaction:", "$b_last_trans")

                            if (b_last_trans) {
                                (activity as MainActivity).showProgressBar()
                            }

                        }
                    }
                }
            }
        }





    }

    private fun appversionChecks() {
        // Get package manager
        val packageManager = requireContext().packageManager

        // Get package name of the application
        val packageName = requireContext().packageName

        try {
            // Get package info
            val packageInfo = packageManager.getPackageInfo(packageName, 0)

            // Access version name and version code
            val versionName = packageInfo.versionName
            val versionCode = packageInfo.versionCode

            // Log or use the version information as needed
            Log.d("AppVersion", "Version Name: $versionName, Version Code: $versionCode")

            // Or you can display it in a TextView, for example
            // textView.text = "Version Name: $versionName\nVersion Code: $versionCode"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun selectOutClickedPermanent() {
        binding.txtIn.setBackgroundResource(R.drawable.green_rectangle_in)
        binding.txtOut.setBackgroundResource(R.drawable.red_rectangle_out_active)
        binding.txtOut.setTextColor(resources.getColor(R.color.white))
        binding.txtIn.setTextColor(resources.getColor(R.color.grey))
    }

    private fun selectInClickedPermanent() {
        binding.txtIn.setBackgroundResource(R.drawable.green_rectangle_in_active)
        binding.txtOut.setBackgroundResource(R.drawable.red_rectangle_out)
        binding.txtIn.setTextColor(resources.getColor(R.color.white))
        binding.txtOut.setTextColor(resources.getColor(R.color.grey))
    }


    private fun addObserver_transaction_Temp() {
        viewModel_transaction_temp.transactionResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse

                            if (requestResponse != null) {

                                if (requestResponse.message!!.contains(
                                        mContext!!.resources.getString(
                                            R.string.response_no_record_found_txt
                                        )
                                    )
                                ) {

                                } else {

                                    if (requestResponse.data != null) {

                                        binding.txtTime.text = requestResponse.data[0].moveTime
                                        str_move_time = requestResponse.data[0].dMoveTime
                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                        val parse = sdf.parse(requestResponse.data[0].moveDate)
                                        if (parse != null) {
                                            val instance = Calendar.getInstance()
                                            instance.time = parse
                                            binding.txtDate.text =
                                                LocaleHelper.convertArabicMonthToEnglish(
                                                    LocaleHelper.arabicToEnglish(
                                                        DateFormat.format(
                                                            "dd MMM yyyy", parse
                                                        ) as String
                                                    )
                                                )
                                        }

                                        if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                            //binding.txtInOut.text = requestResponse.data[0].reasonEn
                                            reasonId = requestResponse.data[0].reasonId
                                            binding.txtLastlocation.text= resources.getString(R.string.last_trasaction_txt)
                                            binding.txtInOut.text = ""
                                            binding.txtLastOut.text=""

                                            if (reasonId == 0) {
                                                selectInClicked()
                                                //  binding.switchInOut.isChecked = true;
                                            } else {
                                                selectOutClicked()
                                                //binding.switchInOut.isChecked = false;
                                            }

                                        } else {
                                           // binding.txtInOut.text = requestResponse.data[0].reasonAr
                                            reasonId = requestResponse.data[0].reasonId
                                            binding.txtLastlocation.text= resources.getString(R.string.last_trasaction_txt)
                                            binding.txtInOut.text = ""
                                            binding.txtLastOut.text=""
                                            if (reasonId == 0) {
                                                selectInClicked()
                                                //  binding.switchInOut.isChecked = true;
                                            } else {
                                                selectOutClicked()
                                                //binding.switchInOut.isChecked = false;
                                            }
                                        }

                                        if (requestResponse.data[0].expectOutValue != null) {
                                            //  binding.txtExpectedout.text =requestResponse.data[0].expectOutValue
                                            DateTime_Op.simpleTimeOnlyConversion24hoursFormat(
                                                requestResponse.data[0].expectOutValue
                                            )
                                        } else {
                                           // binding.txtExpectedout.text = "---"
                                        }
                                        compare_transaction_current_time(mobilePunchConsiderDuration)

                                    } else {/* SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                             context,
                                             requestResponse.message,
                                             R.drawable.app_icon,
                                             colorPrimary
                                         )*/

                                    }
                                }

                            }


                        }
                    }

                    is Resource.Error -> {
                        (activity as MainActivity).hideProgressBar()
                        response.message?.let { message ->
                            //  toast(message)


                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
                                message,
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )
                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }

    private fun setToolbar_Data(activity: MainActivity?) {

        if (UserShardPrefrences.getUserInfo(requireContext().applicationContext) != null) {
            when (UserShardPrefrences.getLanguage(mContext)!!.toInt()) {
                0 -> {
                    activity?.binding!!.layout.txtUsername.text =
                        UserShardPrefrences.getUserInfo(mContext).employeeName
                }

                1 -> {
                    activity?.binding!!.layout.txtUsername.text =
                        UserShardPrefrences.getUserInfo(mContext).employeeArabicName
                }
            }
            activity?.binding!!.layout.llInfo.visibility=View.VISIBLE
            activity?.binding!!.layout.txtUserdesignation.text =
                UserShardPrefrences.getUserInfo(mContext).role


            //  Glide.with(mContext!!).load("https://goo.gl/gEgYUd").into(activity?.binding!!.layout.imgUser);

        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hide_BackButton_title()
        (activity as MainActivity).show_alert()
        (activity as MainActivity).show_info()
        (activity as MainActivity).show_userprofile("")
        (activity as MainActivity).show_app_title("")
        (activity as MainActivity).hideProgressBar()
        (activity as MainActivity).showToolbar()
        (activity as MainActivity).showBottomNavigationView()

        locationSettingsReceiver = LocationSettingsReceiver()
        val intentFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        mContext!!.registerReceiver(locationSettingsReceiver, intentFilter)

        if (GlobalVariables.from_background) {
            handler.post(checkInternetRunnable)
        }



    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sunRiseSet(lat: Double, lng: Double) {

        val location = com.luckycatlabs.sunrisesunset.dto.Location(lat, lng)
        // Pass the time zone display here in the second parameter.
        val calculator = SunriseSunsetCalculator(location, Calendar.getInstance().timeZone)
        val officialSunrise: String = calculator.getOfficialSunriseForDate(Calendar.getInstance())
        val officialSunset: String = calculator.getOfficialSunsetForDate(Calendar.getInstance())


        try {
            val sdf = SimpleDateFormat("HH:mm")
            val dateRise = sdf.parse(officialSunrise)
            val dateSet = sdf.parse(officialSunset)
            //new format
            val sdf2 = SimpleDateFormat("hh:mm aa")
            //            tv_sunrise_time.setText(sdf2.format(dateRise));
//            tv_sunset_time.setText(sdf2.format(dateSet));
            binding.txtSunriseTime.text = officialSunrise
            binding.txtSunsetTime.text = officialSunset
            binding.txtDay.text = LocalDate.now().dayOfWeek.name


        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        mContext!!.unregisterReceiver(locationSettingsReceiver)
      //  stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            // Log.d(TAG, "stopLocationUpdates: $fusedLocationClient")
        }
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
        //mContext?.unregisterReceiver(checkInReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            locationUpdateHandler.removeCallbacksAndMessages(null)
        } catch (e: Exception) {
            locationUpdateHandler = Handler(Looper.getMainLooper())
            // Log.d(TAG, "stopLocationUpdates: $fusedLocationClient")
        }
    }

    private fun setTimeandDate() {
        // Get the current date and time
        val currentTime = timeFormat.format(Calendar.getInstance().time)
        val currentDate = dateFormat.format(Calendar.getInstance().time)

        // Set the values to the TextViews
        binding.txtTime.text = currentTime
        binding.txtDate.text = currentDate
    }

    private fun setClickListeners(activity: MainActivity?) {
        binding.imgCalender.setOnClickListener(this)
        binding.imgAttendance.setOnClickListener(this)
        binding.imgDashboard.setOnClickListener(this)
        binding.imgReports.setOnClickListener(this)
        binding.imgSelfservice.setOnClickListener(this)
        binding.imgSummary.setOnClickListener(this)
        binding.imgEmpRequest.setOnClickListener(this)
        binding.imgAdmin.setOnClickListener(this)

        binding.lManger.setOnClickListener(this)

        binding.swipeRefreshHome.setOnRefreshListener(this)

        binding.txtIn.setOnClickListener(this)
        binding.txtOut.setOnClickListener(this)

        binding.lRequestManual.setOnClickListener(this)
        binding.lRequestPermission.setOnClickListener(this)



        activity?.binding!!.layout.lUser.setOnClickListener(this)
        activity?.binding!!.layout.imgInfo.setOnClickListener(this)
        activity?.binding!!.layout.imgAlert.setOnClickListener(this)
        activity?.binding!!.layout.imgBack.setOnClickListener(this)

    }

    private fun navigateToNotifications() {

        replaceFragment(AnnouncementsFragment(), R.id.flFragment, true)
    }

    private fun navigateToUserInfo() {
        replaceFragment(UserInfoFragment(), R.id.flFragment, true)

    }

    private fun navigateToUserProfile() {
        replaceFragment(ProfileFragment(), R.id.flFragment, true)
    }
    private fun canUseLocation(): Boolean {
        return checkLocationPermission() && isLocationEnabled()
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.l_user -> {

                navigateToUserProfile()
            }

            R.id.img_info -> {

                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE

                navigateToUserInfo()
            }

            R.id.img_alert -> {

                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                navigateToNotifications()

            }

            R.id.img_back -> {
                (activity as MainActivity).onBackPressed()
            }


            R.id.img_attendance -> {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                replaceFragment(AttendanceFragment(), R.id.flFragment, true)
            }

            R.id.img_selfservice -> {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                replaceFragment(SelfServiceFragment(), R.id.flFragment, true)

            }

            R.id.img_admin -> {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                replaceFragment(AdminServicesFragment(), R.id.flFragment, true)

            }

            R.id.img_empRequest -> {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                replaceFragment(EmployeeReqestHRManagerFragment(), R.id.flFragment, true)

            }

            R.id.img_summary -> {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                replaceFragment(SummaryFragment(), R.id.flFragment, true)
            }


            R.id.img_dashboard -> {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                replaceFragment(DashboardTesting(), R.id.flFragment, true)
            }

            R.id.img_calender -> {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                replaceFragment(CalendarFragmentSelf(), R.id.flFragment, true)

            }

            R.id.img_reports -> {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                replaceFragment(ReportsFragment(), R.id.flFragment, true)


            }


            R.id.l_manger -> {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                // replaceFragment(ManagerEmployeeFragment(), R.id.flFragment, true)


                if (UserShardPrefrences.getisAdmin(mContext)) {
                    replaceFragment(AdminEmployeeFragment(), R.id.flFragment, true)
                } else {
                    replaceFragment(ManagerEmployeeFragment(), R.id.flFragment, true)
                }


            }

            R.id.txt_in ->{

//
             //   if (checkLocationPermission())
               if((checkLocationPermission()&&isLocationEnabled()&&!isSpoofed(locationCurrent!!,mContext!!))
                   ||(!isSpoofed(locationCurrent!!,mContext!!)&&(allowedGPSFlag==1||allowedGPSFlag==2))
                   ||(isSpoofed(locationCurrent!!,mContext!!)&&(allowedGPSFlag==3)))
                {
                    if (hasActiveNotifications()) {
                        cancelAllNotifications()
                    }

                    if(reasonStr_current!=null){
                        if(reasonStr_current.equals("I")){
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                mContext,
                                resources.getString(R.string.transaction_already_exists),
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )

                        }
                        else{
                            inClicked()

                        }
                    }
                    else{
                        inClicked()
                    }

                }
                else if(!checkLocationPermission()&&isLocationEnabled()&&!isSpoofed(locationCurrent!!,mContext!!)){
                    requestLocationPermission()

                }
                else{
                   if (checkLocationPermission()&&!isLocationEnabled()) {
                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,
                            resources.getString(R.string.enable_device_location),
                            R.drawable.caution,
                            resources.getColor(R.color.red),
                            SnackBarUtil.OnClickListenerNew {
                                openLocationSettings(mContext!!)

                            })
                    } else if(isSpoofed(locationCurrent!!,mContext!!)&&(allowedGPSFlag==1||allowedGPSFlag==2)){
                       SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.warning_spoof_loc),R.drawable.caution,resources.getColor(R.color.red),
                           SnackBarUtil.OnClickListenerNew {
                               //Do nothing on Ok btn click
                           })
                   }else
                       if(checkLocationPermission()&&!isLocationEnabled()&& isSpoofed(locationCurrent!!,mContext!!)){
                           SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,
                               resources.getString(R.string.enable_device_location),
                               R.drawable.caution,
                               resources.getColor(R.color.red),
                               SnackBarUtil.OnClickListenerNew {
                                   openLocationSettings(mContext!!)

                               })
                       }

                   // SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.enable_device_location),R.drawable.caution,resources.getColor(R.color.red))

               }

            }



            R.id.txt_out -> {




                if((checkLocationPermission()&&isLocationEnabled()&&!isSpoofed(locationCurrent!!,mContext!!))
                    ||(isSpoofed(locationCurrent!!,mContext!!)&&(allowedGPSFlag==3))
                )
                {
                    if (hasActiveNotifications()) {
                        cancelAllNotifications()
                    }



                    if(reasonStr_current!=null){
                        if(reasonStr_current.equals("O")){
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
                                resources.getString(R.string.transaction_already_exists),
                                R.drawable.caution,
                                resources.getColor(R.color.red)
                            )

                        }
                        else{
                            outClicked()

                        }
                    }
                    else{
                        toast(resources.getString(R.string.please_wait))

                    }



                }
                else if(!checkLocationPermission()&&isLocationEnabled()&&!isSpoofed(locationCurrent!!,mContext!!)){
                    requestLocationPermission()
                }
                else{
                    if(checkLocationPermission()&&!isLocationEnabled()){
                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.enable_device_location),R.drawable.caution,resources.getColor(R.color.red),
                            SnackBarUtil.OnClickListenerNew {
                                openLocationSettings(mContext!!)

                            })
                    }else
                    if (!isSpoofed(locationCurrent!!,mContext!!)&&checkLocationPermission()&&!isLocationEnabled()){
                        SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.enable_device_location),R.drawable.caution,resources.getColor(R.color.red),
                            SnackBarUtil.OnClickListenerNew {
                                openLocationSettings(mContext!!)

                            })
                    }else
                    {
                        if(isSpoofed(locationCurrent!!,mContext!!)&&(allowedGPSFlag==2 ||allowedGPSFlag==1)){
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,resources.getString(R.string.warning_spoof_loc),R.drawable.caution,resources.getColor(R.color.red),
                                SnackBarUtil.OnClickListenerNew {
                                    //Do nothing on Ok btn click
                                })
                        }else {
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,
                                resources.getString(R.string.warning_spoof_loc),
                                R.drawable.caution,
                                resources.getColor(R.color.red),
                                SnackBarUtil.OnClickListenerNew {
                                    //Do nothing on Ok btn click
                                })
                        }
                    }

                }



            }

            R.id.l_request_manual -> {

                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                GlobalVariables.updateRequest = true
                val requestsFragment = RequestsFragment()
                val args_violations = Bundle()
                args_violations.putParcelable("violations", arrListCurrentDelay[0])
                requestsFragment.arguments = args_violations
                replaceFragment(requestsFragment, R.id.flFragment, true)


                // replaceFragment(RequestsFragment(), R.id.flFragment, true)

            }

            R.id.l_request_permission -> {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                GlobalVariables.updateRequest = true
                val requestsFragment = RequestsFragment()
                val args_violations = Bundle()
                args_violations.putParcelable("violations", arrListCurrentDelay[0])
                requestsFragment.arguments = args_violations
                replaceFragment(requestsFragment, R.id.flFragment, true)

                //  replaceFragment(RequestsFragment(), R.id.flFragment, true)

            }
        }
    }


    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun inClicked() {
        if (b_delay) {
            if (b_enable_transaction && allowedGPSFlag != 0 && !b_hasLastTransaction ||b_enable_transaction && allowedGPSFlag != 0 &&!isSpoofed(locationCurrent!!,mContext!!)
                ||(isSpoofed(locationCurrent!!,mContext!!)&&allowedGPSFlag==3)) {
                Log.e("b_enable_transaction:1887", b_enable_transaction.toString())

                callCheckIn_OutAPI(0)
            } else if (b_enable_transaction && allowedGPSFlag == 0) {
                toast(resources.getString(R.string.please_wait))
            } else {
                if (!b_enable_transaction&&!b_show_out_office_dialog && distance
                    >= UserShardPrefrences.getAllowedDistance(context)!!&& isAdded)
                {

                    //originalString.replace("Distance: ", "")
                        showDialog(
                            mContext!!,
                            resources.getString(R.string.out_of_office),
                            resources.getString(R.string.out_location_1)+" "+binding.txtDistance.text.toString().replace(mContext!!.resources.getString(R.string.distance_txt), "")+" "+resources.getString(R.string.out_location_2),

                           // LocaleHelper.arabicToEnglish(String.format(resources.getString(R.string.not_allowed_new), distance)),
                            R.drawable.caution,
                            resources.getColor(R.color.red)
                        )


                }
            }

        } else {
            toast(resources.getString(R.string.please_wait))
        }

    }

    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun outClicked() {

        if (b_delay) {


            if (b_enable_transaction && allowedGPSFlag != 0) {
                Log.e("b_enable_transaction:1923", b_enable_transaction.toString())

                if (b_hasLastTransaction && b_allowMobileOutpunch) {
                    callCheckIn_OutAPI(1)
                }   else if (b_hasLastTransaction && !b_allowMobileOutpunch ){
                    showDialog(
                        mContext!!,
                        resources.getString(R.string.out_of_office),
                        resources.getString(R.string.not_allowed_out),
                        R.drawable.caution,
                        resources.getColor(R.color.red)
                    )
                }
                else if (b_hasLastTransaction && b_allowMobileOutpunch &&!isWithinRadius) {


                    showDialog(
                        mContext!!,
                        resources.getString(R.string.out_of_office),
                        //  resources.getString(R.string.not_allowed_out),
                        resources.getString(R.string.out_location_1)+" "+binding.txtDistance.text.toString().replace(mContext!!.resources.getString(R.string.distance_txt), "")+" "+resources.getString(R.string.out_location_2),
                        R.drawable.caution,
                        resources.getColor(R.color.red)
                    )

                }

            } else if (b_enable_transaction && allowedGPSFlag == 0) {
                toast(resources.getString(R.string.please_wait))
            } else {
                if (!b_show_out_office_dialog) showDialog(
                    mContext!!,
                    resources.getString(R.string.out_of_office),
                   // resources.getString(R.string.not_allowed_new),
                    resources.getString(R.string.out_location_1)+" "+binding.txtDistance.text.toString().replace(mContext!!.resources.getString(R.string.distance_txt), "")+" "+resources.getString(R.string.out_location_2),
                    R.drawable.caution,
                    resources.getColor(R.color.red)
                )

            }

        } else {
            toast(resources.getString(R.string.please_wait))
        }


    }

    private fun selectInClicked() {
        binding.txtIn.setBackgroundResource(R.drawable.green_rectangle_in_active)
        binding.txtOut.setBackgroundResource(R.drawable.red_rectangle_out_inactive)
        binding.txtIn.setTextColor(resources.getColor(R.color.white))
        binding.txtOut.setTextColor(resources.getColor(R.color.gray))

        Log.e("testHome", "1839")
    }

    private fun selectOutClicked() {
        binding.txtIn.setBackgroundResource(R.drawable.green_rectangle_in)
        binding.txtOut.setBackgroundResource(R.drawable.red_rectangle_out_active)


        binding.txtOut.setTextColor(resources.getColor(R.color.white))
        binding.txtIn.setTextColor(resources.getColor(R.color.grey))
        Log.e("testHome", "1849")

    }

    @SuppressLint("SuspiciousIndentation")
    fun stopCountdownTimer() {
        if (countDown_Ticker != null) countDown_Ticker?.cancel()
        Log.d("MyApp", "stopCountdownTimer called")
        //  binding.txtTimer.visibility = View.GONE

    }
    private fun isWorkScheduled(uniqueWorkName: String, callback: (Boolean) -> Unit) {
        val workManager = WorkManager.getInstance(mContext!!.applicationContext)
        workManager.getWorkInfosForUniqueWorkLiveData(uniqueWorkName)
            .observe(ProcessLifecycleOwner.get()) { workInfos ->
                if (workInfos == null || workInfos.isEmpty()) {
                    callback(false)
                } else {
                    // Check if any of the work statuses are active
                    val isScheduled = workInfos.any {
                        it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING
                                || it.state == WorkInfo.State.SUCCEEDED
                    }
                    callback(isScheduled)
                }
            }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showTimer(diffInSeconds: Long) {
        var boldStart: Int
        var reminderIntervalMinutes:Long
        var boldEnd: Int
        var lastCallTime: Long?
        var updatedDiffSeconds :Long? =0L
        val mobileConsiderationMinute = mobilePunchConsiderDuration // Example value, replace with the global value
        val mobileConsiderationMillis = TimeUnit.MILLISECONDS.toMillis((mobileConsiderationMinute * 60 * 1000).toLong()) // Convert to milliseconds
        if (diffInSeconds != mobileConsiderationMillis && diffInSeconds > mobileConsiderationMillis){
            updatedDiffSeconds=diffInSeconds-mobileConsiderationMillis
        }
        //  startLocationService()
        startHandler()

        //       reminderIntervalMinutes =
        if(UserShardPrefrences.getisMinute(mContext)){
            reminderIntervalMinutes = UserShardPrefrences.getminutelyReminder(mContext)!!.toLong()

        }
        else if (UserShardPrefrences.getisCustom(mContext)){
           reminderIntervalMinutes = UserShardPrefrences.getCustomReminder(mContext)!!.toLong()

        }
        else {
            reminderIntervalMinutes = 0L
        }


        //  reminderIntervalMinutes =1L
        if (UserShardPrefrences.getLastCallTime(context)!! == 0L){
            UserShardPrefrences.setLastCallTime(context,System.currentTimeMillis())
        }
        lastCallTime=UserShardPrefrences.getLastCallTime(context)

        Log.d("systemTime 3158:", "onTick: $lastCallTime")


        var isFirstTimecalled = false

        countDown_Ticker = object : CountDownTimer(diffInSeconds - updatedDiffSeconds!!, 1000) {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SuspiciousIndentation")
            override fun onTick(millisUntilFinished: Long) {

                val spannableString = SpannableString(
                    LocaleHelper.arabicToEnglish(
                        String.format(
                            Locale.getDefault(),
                            mContext!!.resources.getString(R.string.valid_for_txt) + "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                        )
                    )
                )

                countdown_mints = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                binding.txtTimer.text = spannableString
                // Check and call LocationService after 3 minutes
                // Calculate the current time and elapsed time since the last call
                val currentTime = System.currentTimeMillis()
                val elapsedMillisSinceLastCall = TimeUnit.MILLISECONDS.toMinutes(currentTime-lastCallTime!!)

                // Log.d(TAG, "exactMinutePast: "+elapsedMillisSinceLastCall2/60000)

                if(reminderIntervalMinutes!=0L && UserShardPrefrences.getInHome(mContext)!!) {
                    if (elapsedMillisSinceLastCall != null && (reminderIntervalMinutes < mobilePunchConsiderDuration)) {

                        if (UserShardPrefrences.getisCustom(mContext)) {
                        /*    Log.e("Reminder:Custom:3156", reminderIntervalMinutes.toString())
                            Log.e("Reminder:Custom:3157", elapsedMillisSinceLastCall.toString())
                            Log.e(
                                "Reminder:Custom:3157",
                                UserShardPrefrences.getNotificationData(mContext)[0].toString()
                            )*/
                            /*  val intent = Intent(mainActivityContext, LocationServiceBackUp::class.java)
                        context?.startForegroundService(intent)*/

                            val reminderIntervalMinutesForCustomNotification= mobileConsiderationMinute-reminderIntervalMinutes

                            Log.e("Reminder:Custom:3156", elapsedMillisSinceLastCall.toString())
                            Log.e("Reminder:Custom:3157", reminderIntervalMinutesForCustomNotification.toString())

                            if (b_hasLastTransaction_temp && !b_hasLastTransaction && elapsedMillisSinceLastCall == reminderIntervalMinutesForCustomNotification && UserShardPrefrences.getNotificationData(
                                    mContext
                                )[0].tawajud_NiotificationsTypesId == 1 &&
                                UserShardPrefrences.getNotificationData(mContext)[0].isActive && !b_custom_notification!!
                            ) {
//  if (b_hasLastTransaction_temp && !b_hasLastTransaction && elapsedMillisSinceLastCall == reminderIntervalMinutes && UserShardPrefrences.getNotificationData(context)[0].tawajud_NiotificationsTypesId == 1 &&
//                            UserShardPrefrences.getNotificationData(context)[0].isActive
//                        )
                                if (!b_hasLastTransaction && gpsCoordinates != null) {
                                    callLocationServiceCarParkTimer(
                                        UserShardPrefrences.getAllowedDistance(context)!!.toInt(),
                                        gpsCoordinates!!,
                                        0,
                                        //timeRemainingFormatted
                                        binding.txtTimer.text.toString().replace(
                                            mContext!!.resources.getString(R.string.valid_for_txt),
                                            ""
                                        )
                                    )
                                    b_custom_notification = true
                                    // UserShardPrefrences.setLastCallTime(mContext!!.applicationContext,currentTime)
                                    //PrefUtils.stop-scheduleLocationServiceWithoutService(mainActivityContext, OneTimeWorkRequestBuilder<LocationServiceWorkerWithoutdata>().build())
                                }
                                /*   b_custom_notification = false

                          lastCallTime= UserShardPrefrences.getLastCallTime(mContext!!.applicationContext)*/
                            }

                        }
//when the app destroy it also show notification
                        else {
                            /*     if (GlobalVariables.isAppInForeground){
                            val intent = Intent(requireContext().applicationContext, LocationServiceBackUp::class.java)
                            requireContext().startForegroundService(intent)
                        }*/

                            if (b_hasLastTransaction_temp && !b_hasLastTransaction && elapsedMillisSinceLastCall == reminderIntervalMinutes && UserShardPrefrences.getNotificationData(
                                    mContext
                                )[0].tawajud_NiotificationsTypesId == 1 &&
                                UserShardPrefrences.getNotificationData(mContext)[0].isActive
                            ) {
                                Log.e("Reminder:Minute:3185", reminderIntervalMinutes.toString())
                                Log.e("Reminder:Minute:3186", elapsedMillisSinceLastCall.toString())
                                Log.d(
                                    "NotificationTrigger",
                                    "onCallNotificationServices CurrentTime: ${
                                        convertMillisToDateTimeString(currentTime)
                                    }"
                                )
                                Log.d(
                                    "NotificationTrigger",
                                    "onCallNotificationServices lastCallTime: ${
                                        convertMillisToDateTimeString(lastCallTime!!)
                                    }"
                                )

                                if (!b_hasLastTransaction && gpsCoordinates != null) {

                                    //minutely
                                    callLocationServiceCarParkTimer(
                                        UserShardPrefrences.getAllowedDistance(context)!!.toInt(),
                                        gpsCoordinates!!,
                                        0,
                                        //timeRemainingFormatted
                                        binding.txtTimer.text.toString().replace(
                                            mContext!!.resources.getString(R.string.valid_for_txt),
                                            ""
                                        )
                                    )
                                    /* PrefUtils.stopscheduleLocationServiceWithoutdataService(mainActivityContext,
                                    OneTimeWorkRequestBuilder<LocationServiceWorkerWithoutdata>().build()
                                )*/

                                }


//start timer
                                //else  boolen var= false
                                // Update the last call time
                                lastCallTime = currentTime
                                UserShardPrefrences.setLastCallTime(mContext!!.applicationContext, currentTime)

                                // locationServiceCalled=true
                            }

                        }
                     /*   if (elapsedMillisSinceLastCall > reminderIntervalMinutes) {
                            UserShardPrefrences.setLastCallTime(mContext!!.applicationContext, System.currentTimeMillis())
                            lastCallTime = UserShardPrefrences.getLastCallTime(mContext!!.applicationContext)
                            Log.d("systemTime 3272:", "onTick: $lastCallTime")
                        }*/
                    }

                }
                /*var elapsedMillisSinceLastCall2 = currentTime - lastCallTime!!
                // Adjust currentTime to remove any second difference
                if (elapsedMillisSinceLastCall2 % 60000 == 0L) {
                    val secondsDifference = elapsedMillisSinceLastCall2 % 60000
                    elapsedMillisSinceLastCall2 -= secondsDifference
                }*/
                //   b_timer_stop = false


                b_hasLastTransaction_temp = true
                b_from_temp_trans = true

            }

            override fun onFinish() {
                b_custom_notification = false
                b_hasLastTransaction_temp = false
                b_from_temp_trans = false
                isWorkScheduled("LocationServiceHomeWork") { isScheduled ->
                    if (isScheduled) {
                        // Cancel the existing work
                        WorkManager.getInstance(mContext!!.applicationContext)
                            .cancelUniqueWork("LocationServiceHomeWork")
                    }

                }
                PrefUtils.cancelLocationJob(mContext!!.applicationContext,1)
                UserShardPrefrences.setinAttendance(mContext,false)
                UserShardPrefrences.setinHome(mContext,false)

                binding.txtTimer.setText(mContext!!.resources.getString(R.string.finish_txt))
                //UserShardPrefrences.setLastCallTime(mContext,0L)
                binding.txtTimer.visibility = View.GONE
                callLastTransactionAPI(true)
                Log.d("LastTransaction:2217", "true")


            }
        }.start()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleLocationService(
        allowedDistance: Int,
        gpsCoordinates: String,
        notificationTypeId: Int?,
        minutes: String,
        lastTransactionStatus: Boolean,
        carParkTimer: Boolean
    ) {
        if (UserShardPrefrences.getInHome(mContext)!!) {
            val uniqueWorkName = "LocationServiceHomeWork"

            val inputData = Data.Builder()
                .putInt("distance", allowedDistance)
                .putString("gpsCoordinates", gpsCoordinates)
                .putInt("tawajud_NiotificationsTypesId", notificationTypeId ?: 0)
                .putString("timer_txt", minutes)
                .putBoolean("Last_transactionStatus", lastTransactionStatus)
                .putBoolean("car_park_timer", carParkTimer)
                .build()

            val locationServiceRequest = OneTimeWorkRequestBuilder<LocationServiceWorker>()
                .setInitialDelay(0, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .build()
            //  Log.d(TAG, "WorkMangerID: " + checkWorkStatus(mContext!!, locationServiceRequest.id))
            WorkManager.getInstance(mContext!!.applicationContext).enqueueUniqueWork(
                uniqueWorkName,
                ExistingWorkPolicy.KEEP,
                locationServiceRequest
            )
        }
    }

    private fun convertMillisToDateTimeString(millis: Long): String {
        val date = Date(millis)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun compare_transaction_current_time(mobilePunchConsiderDuration: Int) {


        var newT: Date? = null
        var currentT: Date? = null

        val newTime: String
        val currentTime: String

        var myTime: String = str_move_time.toString()
        val time = myTime.split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        myTime = time[1]
        val tim = myTime.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        myTime = tim[0]
        val df = SimpleDateFormat("HH:mm:ss")
        val d = df.parse(myTime)
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.time = d
        cal.add(Calendar.MINUTE, mobilePunchConsiderDuration)
        newTime = LocaleHelper.arabicToEnglish(df.format(cal.time))
        val date = Date()
        currentTime = LocaleHelper.arabicToEnglish(df.format(date))
        newT = df.parse(newTime)
        currentT = df.parse(currentTime)



        if (currentT.before(newT)) {


            var moveTime: String? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timeLength: Int = str_move_time!!.length


                moveTime = DateTime_Op.simpleTimeConversionNew(str_move_time!!)/*
                                moveTime =
                                    if (timeLength == 6) LocalTime.parse(
                                        str_move_time,
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                                    )
                                        .format(DateTimeFormatter.ofPattern("HH:mm"))
                                    else LocalTime.parse(
                                        str_move_time,
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSS", Locale.US)
                                    )
                                        .format(DateTimeFormatter.ofPattern("HH:mm"))
                */
            }

            binding.txtTime.text = moveTime

            assert(newT != null)
            val diff = newT.time - currentT.time
            val formula = (1000 % 60).toLong()
            val diffSeconds = diff / formula
            val diffInSeconds = TimeUnit.MILLISECONDS.toMillis(diff)
            if (UserShardPrefrences.getAllowedGPSFlag(context) !== 3
            ) {
                stopCountdownTimer()

                binding.txtTimer.visibility = View.VISIBLE
                showTimer(diffInSeconds)
            } else {
                callLastTransactionAPI(true)
                Log.d("LastTransaction:2297", "true")

            }
        } else {
            callLastTransactionAPI(true)
            Log.d("LastTransaction:2302", "true")

        }
    }


    private fun checkGooglePlayServicesAvailable(context: Context): Boolean {
        val status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context)
        if (status == ConnectionResult.SUCCESS) {
            return true
        }
        Log.e(
            TAG,
            "Google Play Services not available: " + GooglePlayServicesUtil.getErrorString(status)
        )
        if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            val errorDialog = GooglePlayServicesUtil.getErrorDialog(
                status, mContext!! as Activity, 1
            )
            errorDialog?.show()
        }
        return false
    }


    private fun getDistanceinMeter(workloc: Location?, myLoc: Location): Float {
        return myLoc.distanceTo(workloc!!) /*/ 1000*/
        Log.e("b_enable_transaction", "2194")
    }


    companion object {/*  private const val GPS_TIME_INTERVAL = 1000 * 60 * 5 // get gps location every 1 min
          private const val GPS_DISTANCE = 1000 // set the distance value in meter
          private const val HANDLER_DELAY = 1000 * 60 * 5
          private const val START_HANDLER_DELAY = 0*/

        private const val REQUEST_NOTIFICATION_PERMISSION = 1
        private const val GPS_TIME_INTERVAL = 1000 * 60 * 5 // get gps location every 1 min
        private const val GPS_DISTANCE = 1000 // set the distance value in meter
        private const val HANDLER_DELAY = 1000 * 2 * 5
        private const val START_HANDLER_DELAY = 0
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001


        /*    private const val GPS_TIME_INTERVAL = 1000 * 1 * 1 // get gps location every 1 min
            private const val GPS_DISTANCE = 10 // set the distance value in meter
            private const val HANDLER_DELAY = 10 * 1 * 1
            private const val START_HANDLER_DELAY = 0*/
        private const val POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE = 1

        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
        const val PERMISSION_ALL = 1
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRefresh() {

       callLocationDetailsAPI(false)
        callEmployeeDetailsAPI(false)
        callCurrentDelayDetailsAPI(false)

        // callWorkLocationAPI()
        if (UserShardPrefrences.getMulipleLoc(context)==true) {
            Log.e("internet:3290", requireActivity().application.hasInternetConnection().toString())
            callWorkLocationAPI()
            // locationRelatedData()
        }
        binding.swipeRefreshHome.isRefreshing = false

    }

    private fun showDialog(
        context: Context,
        messageheading: String,

        message: String,
        drawable_icon: Int,
        color: Int,
    ) {
        // Create a new instance of the AlertDialog.Builder class
        /* val builder = AlertDialog.Builder(context)
         builder.setTitle(title)//Set the tittle
         builder.setMessage(mess)//Set the message of the dialog
         builder.setPositiveButton("OK") { dialog, _ ->
             // Implement the Code when OK Button is Clicked
             dialog.dismiss()

             b_show_out_office_dialog = false
         }
         *//* builder.setNegativeButton("Cancel") { dialog, _ ->
             // Implement the Code when Cancel Button is CLiked
             dialog.dismiss() //Close or dismiss the alert dialog
         }*//*
        b_show_out_office_dialog = true
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()*/




        try {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var alertDialogView: View? = null
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_, null)
                alertDialog.setView(alertDialogView)
                val textDialog = alertDialogView.findViewById<TextView>(R.id.diag_text)
                textDialog.text = message
                val diag_ok = alertDialogView.findViewById<TextView>(R.id.diag_ok)

                val icon = alertDialogView.findViewById<ImageView>(R.id.icon)

                //   icon.setImageResource(drawable_icon)

                val resourceName = context.resources.getResourceEntryName(drawable_icon)
                Log.e("resourceName", resourceName)
                if (resourceName.contains("caution")) {
                    icon.setImageResource(drawable_icon)
                }
                diag_ok.setBackgroundColor(color)
                val textHeadingDialog =
                    alertDialogView.findViewById<TextView>(R.id.diag_text_heading)
                textHeadingDialog.text = messageheading


                val show = alertDialog.show()
                val window = show.window
                if (window != null) {
                    window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    //TODO Dialog Size
                    show.window!!.setLayout(
                        SnackBarUtil.getWidth(context) / 100 * 90,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    show.window!!.setGravity(Gravity.CENTER)
                }
                diag_ok.setOnClickListener {
                    show.dismiss()
                    b_show_out_office_dialog = false

                }

                b_show_out_office_dialog = true
                show.setCancelable(false)
                show.setCanceledOnTouchOutside(false)

            }
//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }


    private fun showFaceDialog(s: String) {


        SnackBarUtil.showSnackbar_Drawable_backroundcolor(
            context, s, R.drawable.caution, resources.getColor(R.color.red)
        )

    }


    fun getLocationName(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        var locationName = ""

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                // Example: "123 Main St, City, Country"
                locationName =
                    "${address.getAddressLine(0)}, ${address.locality}, ${address.countryName}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return locationName
    }




    fun parseCoordinates(coordinates: String): Location? {
        val parts = coordinates.split(",")
        if (parts.size == 2) {
            try {
                val lat = parts[0].trim().toDouble()
                val lon = parts[1].trim().toDouble()

                val location = Location("")
                location.latitude = lat
                location.longitude = lon

                return location
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        } else {
            // Handle invalid format error
            Log.e("GPS Parsing Error", "Invalid coordinate format: $coordinates")
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCheckInRequested() {
        if (!b_hasLastTransaction_temp){
            callLocationDetailsAPI(false)
            if (UserShardPrefrences.getMulipleLoc(context)==true) {
                callWorkLocationAPI()
            }
            callCheckIn_OutAPI(0)
            b_hasLastTransaction_temp=true
        }
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
}