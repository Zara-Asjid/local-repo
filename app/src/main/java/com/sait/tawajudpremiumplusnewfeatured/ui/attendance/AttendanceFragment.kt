package com.sait.tawajudpremiumplusnewfeatured.ui.attendance



import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.SpannableString
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.mvvm_application.util.extension.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.sait.tawajudpremiumplusnewfeatured.LocationService
import com.sait.tawajudpremiumplusnewfeatured.LocationServiceBackUp
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.UserInfoFragment
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentAttendanceBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.AnnouncementsFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.adapter.AnnouncementAdapter
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementData
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.viewmodels.AnnouncementViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.adapters.GridView_AdvancePunch_Adapter
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.adapters.GridView_AdvancePunch_Adapter.AdvancePunchItemClickListener
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.viewmodels.reasons.ReasonsViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.models.ReasonsData
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.models.ReasonsResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.SaveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.WorkLocationData
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.WorkLocationRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.WorkLocations.WorkLocationViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.location.LocationViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.saveEntrytransaction.SaveEntryTransactionViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.transaction.TransactionViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.transaction_temp.TransactionTempViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.b_delay
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.Keys
import com.sait.tawajudpremiumplusnewfeatured.util.LocationServiceWorker
import com.sait.tawajudpremiumplusnewfeatured.util.LocationSettingsReceiver
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.extension.GlobalHandler
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.cancelLocationJob
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.isSpoofed
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class AttendanceFragment : BaseFragment(), OnClickListener,
    OnMapReadyCallback, AdvancePunchItemClickListener {
    private var _binding: FragmentAttendanceBinding? = null
    private lateinit var locationSettingsReceiver: LocationSettingsReceiver

    private val binding get() = _binding!!
    private var mContext: Context? = null
    private var mGoogleMap: GoogleMap? = null
    private lateinit var viewModel_Reasons: ReasonsViewModel
    private lateinit var viewModel_transaction: TransactionViewModel
    private lateinit var viewModel_workLocations: WorkLocationViewModel

    private lateinit var viewModel_Locationdetails: LocationViewModel
    private lateinit var dFormat : DecimalFormat

    private var unReadCount : Int ?= null

    var locationManager: LocationManager? = null
    private var customMarker: Marker? = null
    private var markerLatLng: LatLng? = null
    var markerOptions: MarkerOptions? = null
    private var permissionDeniedToastShown :Boolean=false

    private var b_hasMobilePunch:Boolean = false
    private var b_mustPunchPhysical:Boolean = false
    private var b_allowMobileOutpunch:Boolean = false
    private var b_validateSecondIN:Boolean = false
    private var b_allowOnlyFirstINForCarPark:Boolean = false
    private var workLocationId:Int =0
    private var str_move_time: String? = null
    private var b_start: Boolean = false

    private var reasonId :Int =0
    private var allowedDistance:Int = 0
    private var mobilePunchConsiderDuration:Int = 0

    private var allowedGPSFlag:Int = 0

    var latitude // latitude
            = 0.0
    var longitude // longitude
            = 0.0
    var isFirstZoom = false
    private var b_hasLastTransaction: Boolean = false
    private var b_hasLastTransaction_temp: Boolean = false

    var arrListWorkLocation = java.util.ArrayList<String>()

    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 20000
    var countDown_Ticker: CountDownTimer? = null
    var countdown_mints: Long = 0
    var reasonsResponse: ReasonsResponse? = null
    /* private var reasonsItemList: List<ReasonsData> =
         ArrayList<ReasonsData>()*/

    private lateinit var arrListReasons: ArrayList<ReasonsData>
    private lateinit var viewModel_SaveEntry: SaveEntryTransactionViewModel
    private lateinit var viewModel_transaction_temp: TransactionTempViewModel
    val workLoc = Location("")
    val currentLocation = Location("")
    var str_work_loc: String? = null
    var isWithinRadius: Boolean? = null

    private var b_enable_transaction: Boolean = false
    var supportMapFragment: SupportMapFragment? = null


    val handlernew = Handler(Looper.getMainLooper())
    val delaynew: Long = 4 * 1000 // 10 seconds in milliseconds

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocationMarker: Marker? = null
    private lateinit var locationUpdateHandler: Handler
    private var b_show_out_office_dialog:Boolean = false
    var finalDateMonth: String? = null
    var initialDateMonth: String? = null
    private lateinit var viewModel_announcements: AnnouncementViewModel
    private lateinit var arrListAnnouncements: java.util.ArrayList<AnnouncementData>
    private var announcementAdapter: AnnouncementAdapter? = null
    private var b_from_temp_trans: Boolean = false
    var gpsCoordinates: String? = null
    var distance: Float= 0.0F
    private var reasonStr_current: String? = null
    private var b_custom_notification: Boolean=false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)

        supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        viewModel_Reasons = ViewModelProvider(this)[ReasonsViewModel::class.java]
        viewModel_transaction = ViewModelProvider(this)[TransactionViewModel::class.java]
        viewModel_Locationdetails = ViewModelProvider(this)[LocationViewModel::class.java]
        viewModel_SaveEntry = ViewModelProvider(this)[SaveEntryTransactionViewModel::class.java]
        viewModel_transaction_temp = ViewModelProvider(this)[TransactionTempViewModel::class.java]
        viewModel_announcements = ViewModelProvider(this)[AnnouncementViewModel::class.java]
        viewModel_workLocations = ViewModelProvider(this)[WorkLocationViewModel::class.java]

        val activity = this.activity as MainActivity?
        mContext = inflater.context
        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }
        hideIcons(activity)
        arrListReasons = arrayListOf()

        binding.workModeTxt.isSelected =true
        binding.locCordinatesTxt.isSelected =true
        binding.lngitdeLatitdeTxt.isSelected= true

        callReasonsAPI()

        callLocationDetailsAPI(true)
        //  callLastTransactionAPI()
        setClickListeners(activity)
        arrListAnnouncements= arrayListOf()
        arrListWorkLocation = arrayListOf()

        setDate()
   /*     if (UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!) != null &&

            UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!)!!.isNotEmpty()
        ) {


            if (UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!) != "0") {
                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.VISIBLE

                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.text =
                    UserShardPrefrences.getCountAnnouncementsUnRead(mContext!!)
            }
        } else {
            callAnnouncementsAPI()
        }*/



        if(!GlobalVariables.b_delay_attendance) {
            GlobalHandler.postDelayedTask(myTask, 5 * 1000)
        }
        else{
            callWorkLocationAPI()
        }
        return binding.root
    }

    private fun hideIcons(activity: MainActivity?) {
        activity?.binding!!.layout.llInfo.visibility=View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val myTask = Runnable {
        Log.e("After","This will be executed after 5 seconds")
        GlobalVariables.b_delay_attendance = true

        if (UserShardPrefrences.getMulipleLoc(context)==true) {
            callWorkLocationAPI()
        }
    }

    private fun callAnnouncementsAPI() {

        addObserver_announcements()
        getAnnouncementsDetails()
    }
    private fun getAnnouncementsDetails() {
        val managerEmpRequest = AnnouncementRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            0, finalDateMonth!!,
            initialDateMonth!!
        )
        Log.e("commonRequest", managerEmpRequest.toString())
        viewModel_announcements.getAnnouncementData(mContext!!, managerEmpRequest)

    }
    @SuppressLint("SuspiciousIndentation")
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
                                    arrListAnnouncements.clear()
                                    arrListAnnouncements.addAll(managerEmpResponse.data)
                                    if (arrListAnnouncements.size > 0) {
                                        (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                                            View.VISIBLE

                                        (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.text =
                                            arrListAnnouncements.size.toString()

                                        UserShardPrefrences.saveCountAnnouncementsUnRead(
                                            requireActivity(),
                                            arrListAnnouncements.size.toString()
                                        )
                                    } else {
                                        (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                                            View.GONE

                                    }

                                    //setAdapterAnnouncements(arrListAnnouncements)

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


    private fun setDate() {

        finalDateMonth = DateTime_Op.getFinalDateMonth("yyyy-MM-dd")
        initialDateMonth = DateTime_Op.getInitialDateMonth("yyyy-MM-dd")

    }
    private var locationCurrent: Location? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun locationRelatedData() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // Initialize locationRequest
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            // .setInterval(1000) // Update interval in milliseconds 1sec
            .setInterval(1*1000*60*2)  // 2 mins

        // Initialize locationCallback
        locationCallback = object : LocationCallback() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onLocationResult(locationResult: LocationResult) {
                locationCurrent = locationResult.lastLocation
                if (isAdded) {

                    updateMapLocation(locationCurrent!!)                }

            }
        }

        locationUpdateHandler = Handler(Looper.getMainLooper())

        // Check and request location permissions
        if (checkLocationPermission()) {
            initMap()
        } else {
            requestLocationPermission()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Start location updates every 2 seconds
        locationUpdateHandler.postDelayed(locationUpdateRunnable, 2000)
        //  callWorkLocationAPI()


    }

    private val locationUpdateRunnable = object : Runnable {
        override fun run() {
            startLocationUpdates()
            locationUpdateHandler.postDelayed(this, 2000)
        }
    }

    private fun checkLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

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
    }



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

                if (UserShardPrefrences.getMulipleLoc(context)==true) {
                    callWorkLocationAPI()
                }
            }
            coarseLocationGranted -> {
                // The user granted the coarse location permission
                locationRelatedData()

                //  displayLocationToast(locationCurrent!!)
                Log.e("currentLocation:258", locationCurrent.toString())

                if (UserShardPrefrences.getMulipleLoc(context)==true) {
                    callWorkLocationAPI()
                }
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
                    if(notificationsGranted && !fineLocationGranted){
                        openAppSettings()
                    }else
                        if(!notificationsGranted && !fineLocationGranted){
                            requestLocationPermission()
                        }else
                        {
                            requestLocationPermission()
                        }
                    //  requestLocationPermissionsNew()
                    //toast("Location permission denied. Please enable it from the app settings.")

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
    /*
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Reload the map after obtaining permission
                    initMap()
                } else {
                    // Handle permission denied
                }
            }
        }
    */

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Check if location services are enabled
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Prompt the user to enable GPS
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }

        // Enable My Location button and move camera to current location
        if (ActivityCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    updateMapLocation(location)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateMapLocation(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        if (UserShardPrefrences.getLanguage(mContext).equals("1")){
            dFormat=  DecimalFormat("#,##0.000000")
            latitude = java.lang.Double.valueOf(LocaleHelper.arabicToEnglish(location.latitude.toString()))
            longitude = java.lang.Double.valueOf(LocaleHelper.arabicToEnglish(location.longitude.toString()))
        }else{
            dFormat = DecimalFormat("#.######")
            latitude = java.lang.Double.valueOf(dFormat.format(location.latitude))
            longitude = java.lang.Double.valueOf(dFormat.format(location.longitude))

        }


        currentLocation!!.latitude = latitude
        currentLocation!!.longitude = longitude



        if(allowedGPSFlag == 3){

            binding.txtDistance.visibility = View.GONE
            binding.lngitdeLatitdeTxt.visibility = View.VISIBLE
            binding.locCordinatesTxt.visibility = View.VISIBLE

            if(latitude!=null &&longitude!=null) {

                binding.lngitdeLatitdeTxt.text = "$latitude,$longitude"
                binding.locCordinatesTxt.text = getLocationName(mContext!!, latitude, longitude)
            }
            else{
                binding.lngitdeLatitdeTxt.text = mContext!!.resources.getString(R.string.not_available_txt)
                binding.locCordinatesTxt.text = mContext!!.resources.getString(R.string.not_available_txt)
            }
        }
        else{
            binding.lngitdeLatitdeTxt.visibility = View.VISIBLE
            binding.locCordinatesTxt.visibility = View.VISIBLE
            binding.txtDistance.visibility = View.VISIBLE
        }
        /*  if(UserShardPrefrences.getGpsCoordinates(mContext)!=null && UserShardPrefrences.getGpsCoordinates(mContext)!!
                  .isNotEmpty()&& allowedGPSFlag!=3){*/
        if (gpsCoordinates != null && gpsCoordinates!!.isNotEmpty() && allowedGPSFlag != 3
        ){
            str_work_loc = UserShardPrefrences.getGpsCoordinates(mContext)
            val parts: List<String> = str_work_loc!!.split(",")
            workLoc.latitude = parts[0].toDouble()
            workLoc.longitude = parts[1].toDouble()
            distance = getDistanceinMeter(workLoc, currentLocation!!)- UserShardPrefrences.getAllowedDistance(mContext)!!
            //  var distance: Float = 3000f

            if(UserShardPrefrences.getLanguage(mContext).equals("1")){


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


            //    binding.txtDistance.text = mContext!!.resources.getString(R.string.distance_txt)+LocaleHelper.arabicToEnglish(String.format("%.2f", distance)).replace(",", ".",false)+requireContext().resources.getString(R.string.meters_txt)
            }else
                if (mContext!=null) {
                    binding.txtDistance.visibility = View.VISIBLE
                    if (distance > 0) {
                        binding.txtDistance.text =
                            mContext!!.resources.getString(R.string.distance_txt) + String.format(
                                "%.2f", distance
                            ) + requireContext().resources.getString(
                                R.string.meters_txt
                            )

                        Log.e("distance_1002", String.format("%.2f", distance))


                    } else {
                        binding.txtDistance.text =
                            mContext!!.resources.getString(R.string.distance_txt) + String.format(
                                "%.2f", 0.0F
                            ) + requireContext().resources.getString(
                                R.string.meters_txt
                            )
                        Log.e("distance_1012", String.format("%.2f", distance))


                    }
                }

          /*  {
                if (mContext!=null){
                    binding.txtDistance.visibility = View.VISIBLE
                    binding.txtDistance.text =mContext!!.resources.getString(R.string.distance_txt)+String.format("%.2f", distance)+mContext!!.resources.getString(R.string.meters_txt)
                }

            }*/
            if(allowedGPSFlag == 1 || allowedGPSFlag==2){
                //if (distance < 1000) {
                if (distance < UserShardPrefrences.getAllowedDistance(mContext)!!.toDouble()) {
                    /*   Toast.makeText(
                       mContext!!,"With in Range " +
                       "Got Coordinates: " + latitude + ", " + longitude,
                       Toast.LENGTH_SHORT
                   ).show()
       */

                    b_enable_transaction = true


                } else {
                    //  SnackBarUtil.showSnackbar(mContext,"Out of Range")
                    b_enable_transaction = false


                    //   showDialog(mContext!!,"Out of office","You can't able to perform any transactions")

                    /*
                if(!b_show_out_office_dialog && mContext!=null)
                    showDialog(requireContext(),mContext!!.resources.getString(R.string.out_of_office_txt),mContext!!.resources.getString(R.string.not_allowed),R.drawable.caution,resources.getColor(R.color.red))
    */

                }

            }



        }
        else{
            Log.e("b_enable_transaction","601")
            binding.txtDistance.visibility = View.GONE
        }


        markerLatLng = LatLng(latitude, longitude)

        // Remove previous marker
        currentLocationMarker?.remove()

        // Add new marker for current location
        currentLocationMarker = mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Current Location")
        )

        // Move camera to the current location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))


        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.isCompassEnabled = true
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        // Display a toast message with the updated location
//        Log.e("Updated Location", "Updated Location: ${location.latitude}, ${location.longitude}")
        /*
                Toast.makeText(
                    mContext,
                    "Updated Location: ${location.latitude}, ${location.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
        */
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
                locationName = "${address.getAddressLine(0)}, ${address.locality}, ${address.countryName}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return locationName
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                mContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        mContext!!.unregisterReceiver(locationSettingsReceiver)
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            // Log.d(TAG, "stopLocationUpdates: $fusedLocationClient")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        //  locationUpdateHandler.removeCallbacks(locationUpdateRunnable)
        try {
            locationUpdateHandler.removeCallbacksAndMessages(null)
        } catch (e: Exception) {
            locationUpdateHandler = Handler(Looper.getMainLooper())
            // Log.d(TAG, "stopLocationUpdates: $fusedLocationClient")
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun callLocationDetailsAPI(b: Boolean) {
        addObserverForLocationDetails(b)
        postLocationDetails()
    }

    private fun postLocationDetails() {

        val context = context?.applicationContext
        if (context != null) {
            val locationRequest = CommonRequest(
                UserShardPrefrences.getUserInfo(context).fKEmployeeId,
                UserShardPrefrences.getLanguage(mContext)!!.toInt()
            )
            viewModel_Locationdetails.getLocationData(mContext!!, locationRequest)
        }}

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addObserverForLocationDetails(b: Boolean) {
        //  if(view!=null) {

        if(b) {
            viewModel_Locationdetails.locationResponse.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when (response) {
                        is Resource.Success -> {
                            (activity as MainActivity).hideProgressBar()

                            response.data?.let { locationResponse ->
                                val locationResponse = locationResponse

                                if (locationResponse != null) {
                                    if(UserShardPrefrences.getMulipleLoc(context)==false) {

                                        if (locationResponse.workLocationId.toString() != "0") {
                                            UserShardPrefrences.saveWorkLocationId(
                                                mContext, locationResponse.workLocationId.toString()
                                            )

                                        }

                                        if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                            if (locationResponse.workLocationName != null) {
                                                UserShardPrefrences.saveWorkLocationName(
                                                    mContext, locationResponse.workLocationName
                                                ).toString()
                                            }
                                        } else {
                                            if (locationResponse.workLocationArabicName != null) {
                                                UserShardPrefrences.saveWorkLocationName(
                                                    mContext, locationResponse.workLocationArabicName
                                                ).toString()
                                            }
                                        }

                                        if (locationResponse.gpsCoordinates != null) {
                                            UserShardPrefrences.saveGpsCoordinates(
                                                mContext, locationResponse.gpsCoordinates
                                            )

                                            gpsCoordinates = locationResponse.gpsCoordinates!!
                                                .split(",") // Split the string by comma
                                                .map { it.trim().toDouble() }.toString()

                                        }

                                    }

                                    if (locationResponse.allowedGPSFlag != null) {
                                        UserShardPrefrences.setAllowedGPSFlag(
                                            mContext,
                                            locationResponse.allowedGPSFlag
                                        )

                                        allowedGPSFlag = locationResponse.allowedGPSFlag
                                    }

                                    UserShardPrefrences.setMustPhysicalPunch(
                                        mContext,
                                        locationResponse.mustPunchPhysical
                                    )


                                    b_hasMobilePunch = locationResponse.hasMobilePunch


                                    b_mustPunchPhysical = locationResponse.mustPunchPhysical
                                    b_allowMobileOutpunch = locationResponse.allowMobileOutpunch
                                    b_validateSecondIN = locationResponse.validateSecondIN
                                    b_allowOnlyFirstINForCarPark =
                                        locationResponse.allowOnlyFirstINForCarPark
                                    mobilePunchConsiderDuration =
                                        locationResponse.mobilePunchConsiderDuration
                                    //  allowedDistance = locationResponse.allowedDistance
                                    allowedGPSFlag = locationResponse.allowedGPSFlag


                                    // binding.txtWorkMode.text= requestResponse.gpsCoordinates
                                    /* if (locationResponse.workLocationName != null && locationResponse.workLocationName.isNotEmpty()) {
                                         binding.locCordinatesTxt.text =
                                             locationResponse.workLocationName
                                     }
                                     binding.lngitdeLatitdeTxt.text = locationResponse.gpsCoordinates
 */

                                    if(UserShardPrefrences.getMulipleLoc(context)==false) {
                                        workLocationId = locationResponse.workLocationId

                                        if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                            if (locationResponse.workLocationName != null && locationResponse.workLocationName.isNotEmpty()) {
                                                binding.locCordinatesTxt.text =
                                                    locationResponse.workLocationName
                                                UserShardPrefrences.saveWorkLocationName(
                                                    mContext, locationResponse.workLocationName
                                                ).toString()

                                            }
                                        } else {
                                            if (locationResponse.workLocationArabicName != null && locationResponse.workLocationArabicName.isNotEmpty()) {
                                                binding.locCordinatesTxt.text =
                                                    locationResponse.workLocationArabicName
                                                UserShardPrefrences.saveWorkLocationName(
                                                    mContext, locationResponse.workLocationArabicName
                                                ).toString()

                                            }


                                        }
                                        binding.lngitdeLatitdeTxt.text = locationResponse.gpsCoordinates
                                        UserShardPrefrences.saveGpsCoordinates(
                                            mContext, locationResponse.gpsCoordinates
                                        )

                                    }

                                    when (locationResponse.allowedGPSFlag) {

                                        1 -> {
                                            binding.workModeTxt.text =
                                                mContext!!.resources.getString(R.string.car_punch_in_txt)

                                            if (!b_hasLastTransaction) {
                                                callLastTransactionTemp()
                                            }
                                        }

                                        2 -> {
                                            binding.workModeTxt.text =
                                                mContext!!.resources.getString(R.string.remote_work_txt)
                                            //  b_enable_transaction = true
                                        }

                                        3 -> {
                                            binding.workModeTxt.text =
                                                mContext!!.resources.getString(R.string.romers_txt)
                                            b_enable_transaction = true
                                        }
                                    }
                                    if(UserShardPrefrences.getMulipleLoc(context)==false) {
                                        if (locationResponse.allowedGPSFlag != 3) {
                                            callLocationService(
                                                locationResponse.allowedDistance,
                                                locationResponse.gpsCoordinates,
                                                0
                                            )
                                        }
                                    }

                                    locationRelatedData()
                                    if (locationResponse.allowedGPSFlag == 1) {
                                        callLastTransactionAPI(false)
                                    } else {
                                        callLastTransactionAPI(true)
                                    }


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

                                // SnackBarUtil.showSnackbar(context, message, false)

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
        else {
            if (view != null)
                viewModel_Locationdetails.locationResponse.observe(viewLifecycleOwner) { event ->
                    event.getContentIfNotHandled()?.let { response ->
                        when (response) {
                            is Resource.Success -> {
                                (activity as MainActivity).hideProgressBar()

                                response.data?.let { locationResponse ->
                                    val locationResponse = locationResponse

                                    if (locationResponse != null) {
                                        if (locationResponse.workLocationId.toString() != "0") {
                                            UserShardPrefrences.saveWorkLocationId(
                                                mContext,
                                                locationResponse.workLocationId.toString()
                                            )

                                        }
                                        if (locationResponse.workLocationName != null && locationResponse.workLocationName.isNotEmpty()) {
                                            when (UserShardPrefrences.getLanguage(mContext)!!
                                                .toInt()) {
                                                0 -> {
                                                    UserShardPrefrences.saveWorkLocationName(
                                                        mContext,
                                                        locationResponse.workLocationName
                                                    ).toString()
                                                }

                                                1 -> {
                                                    UserShardPrefrences.saveWorkLocationName(
                                                        mContext,
                                                        locationResponse.workLocationArabicName
                                                    ).toString()
                                                }
                                            }

                                        }

                                        /*
                                                                                if (locationResponse.gpsCoordinates != null) {
                                                                                    UserShardPrefrences.saveGpsCoordinates(
                                                                                        mContext,
                                                                                        locationResponse.gpsCoordinates
                                                                                    )
                                                                                }
                                        */
                                        if (locationResponse.allowedGPSFlag != null) {
                                            UserShardPrefrences.setAllowedGPSFlag(
                                                mContext,
                                                locationResponse.allowedGPSFlag
                                            )

                                            allowedGPSFlag = locationResponse.allowedGPSFlag
                                        }

                                        UserShardPrefrences.setMustPhysicalPunch(
                                            mContext,
                                            locationResponse.mustPunchPhysical
                                        )


                                        b_hasMobilePunch = locationResponse.hasMobilePunch


                                        b_mustPunchPhysical = locationResponse.mustPunchPhysical
                                        b_allowMobileOutpunch = locationResponse.allowMobileOutpunch
                                        b_validateSecondIN = locationResponse.validateSecondIN
                                        b_allowOnlyFirstINForCarPark =
                                            locationResponse.allowOnlyFirstINForCarPark
                                        mobilePunchConsiderDuration =
                                            locationResponse.mobilePunchConsiderDuration
                                        //   allowedDistance = locationResponse.allowedDistance
                                        allowedGPSFlag = locationResponse.allowedGPSFlag
                                        workLocationId = locationResponse.workLocationId

                                        // binding.txtWorkMode.text= requestResponse.gpsCoordinates
                                        /* if (locationResponse.workLocationName != null && locationResponse.workLocationName.isNotEmpty()) {
                                             binding.locCordinatesTxt.text =
                                                 locationResponse.workLocationName
                                         }
                                         binding.lngitdeLatitdeTxt.text =
                                             locationResponse.gpsCoordinates*/

                                        when (locationResponse.allowedGPSFlag) {

                                            1 -> {
                                                binding.workModeTxt.text =
                                                    mContext!!.resources.getString(R.string.car_punch_in_txt)

                                                if (!b_hasLastTransaction) {
                                                    callLastTransactionTemp()
                                                }
                                            }

                                            2 -> {
                                                binding.workModeTxt.text =
                                                    mContext!!.resources.getString(R.string.remote_work_txt)
                                                //  b_enable_transaction = true
                                            }

                                            3 -> {
                                                binding.workModeTxt.text =
                                                    mContext!!.resources.getString(R.string.romers_txt)
                                                b_enable_transaction = true
                                            }
                                        }

                                        locationRelatedData()
                                        if (locationResponse.allowedGPSFlag == 1) {
                                            callLastTransactionAPI(false)
                                        } else {
                                            callLastTransactionAPI(true)
                                        }


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

                                    // SnackBarUtil.showSnackbar(context, message, false)

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
    //}


    private fun callReasonsAPI() {
        addObserver_Reasons()
        getReasonsDetails()
    }
    private fun callLastTransactionAPI(b_last_trans: Boolean) {

        // viewModel_transaction = ViewModelProvider(this)[TransactionViewModel::class.java]

        addObserver_transaction(b_last_trans)
        postTransactionDetails()



    }

    private fun postTransactionDetails() {
        val transactionRequest = CommonRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            //172,
            UserShardPrefrences.getLanguage(mContext)!!.toInt())
        viewModel_transaction.getTransactionData(mContext!!,transactionRequest)
    }

    private fun addObserver_transaction(b_last_trans: Boolean) {


        if (view != null){

            viewModel_transaction.transactionResponse.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { response ->
                    when(response) {
                        is Resource.Success -> {
                            (activity as MainActivity).hideProgressBar()

                            response.data?.let { transactionResponse ->

                                if (transactionResponse != null) {

                                    if (transactionResponse.data != null) {
                                        b_hasLastTransaction = true
                                        b_hasLastTransaction_temp = false
                                        binding.dateTimeTxt.text = LocaleHelper.arabicToEnglish(convertDateFormat(
                                            transactionResponse.data[0].moveDate,
                                            "yyyy-MM-dd'T'HH:mm:ss",
                                            "dd/MM/yyyy"
                                        )) + " " + transactionResponse.data[0].moveTime
                                        str_move_time = transactionResponse.data[0].moveTime
                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                        reasonId = transactionResponse.data[0].reasonId
                                        reasonStr_current = transactionResponse.data[0].type

                                        if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                            //  binding.txtInOut.text = transactionResponse.data[0].reasonEn
                                            if (transactionResponse.data[0].reasonEn.equals("IN")) {
                                                selectInClicked()
                                                binding.validateTypeTxt.text= transactionResponse.data[0].reasonEn
                                            } else {
                                                selectOutClicked()
                                                binding.validateTypeTxt.text= transactionResponse.data[0].reasonEn
                                            }
                                        } else {
                                            // binding.txtInOut.text = transactionResponse.data[0].reasonAr

                                            if (transactionResponse.data[0].reasonAr.equals("دخول")) {
                                                // reasonId = 0
                                                //  binding.switchInOut.isChecked = true;
                                                selectInClicked()

                                            } else {
                                                // reasonId = 1
                                                //  binding.switchInOut.isChecked = false;
                                                selectOutClicked()

                                            }
                                            // reasonId  = requestResponse.data[0].reasonId

                                        }
                                        b_from_temp_trans= false
                                        stopCountdownTimer()
                                        binding.txtTimer.visibility = View.GONE
                                        if (hasActiveNotifications()) {
                                            cancelLocationJob(mContext!!.applicationContext,1)
                                            cancelAllNotifications()
                                        }

                                    } else {

                                        if(!b_from_temp_trans) {
                                            binding.dateTimeTxt.text =
                                                mContext!!.resources.getString(R.string.not_available_txt)
                                            binding.validateTypeTxt.text = ""
                                            reasonStr_current = null
                                            reasonId = 0
                                        }
                                        b_hasLastTransaction = false
                                        // b_hasLastTransaction_temp = false
                                        reasonId = 0
                                        reasonStr_current = ""
                                        /*  SnackBarUtil.showSnackbar(
                                              context,
                                              transactionResponse.message.toString(),
                                              false

                                          )
      */

                                        if ((binding.workModeTxt.text.equals("Car Park Punch In")||binding.workModeTxt.text.equals("خاصية مواقف السيارات") && !b_hasLastTransaction&& !b_hasLastTransaction_temp)) {
                                            selectInClickedForTempTransaction()
                                        } else {
                                            selectNoInOutClicked()
                                        }
                                    }


                                }



                            }
                        }

                        is Resource.Error -> {
                            (activity as MainActivity).hideProgressBar()
                            response.message?.let { message ->
                                //  toast(message)

                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.caution,resources.getColor(R.color.red))

                                //   SnackBarUtil.showSnackbar(context, message, false)
                            }
                        }

                        is Resource.Loading -> {

                            if(b_last_trans) {
                                (activity as MainActivity).showProgressBar()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun selectOutClicked() {
        binding.imgIn.alpha = 1.0f
        binding.imgAdavancepunch.alpha = 1.0f
        binding.imgOut.alpha = 0.5f
        binding.imgIn.setImageResource(R.drawable.sign_in_icon)
        binding.imgOut.setImageResource(R.drawable.sign_out_icon)
        binding.imgAdavancepunch.setImageResource(R.drawable.advance_punch_new)
        binding.lAdvancePunch.isClickable = true
        binding.signOutTxt.setTextColor(resources.getColor(R.color.grey_attendance))
        binding.signInTxt.setTypeface(null, Typeface.BOLD)
        // binding.imgOut.setImageResource(R.drawable.sign_out_icon_gray)

        /*      binding.signInTxt.setTextColor(Color.parseColor("#B4B4B4"))
              binding.advancePunchTxt.setTextColor(Color.parseColor("#B4B4B4"))
              binding.imgIn.setImageResource(R.drawable.sign_in_gray)
              binding.imgAdavancepunch.setImageResource(R.drawable.advance_punch_gray)*/
    }

    private fun selectInClicked() {

        // binding.imgIn.setImageResource(R.drawable.sign_in_gray)

        binding.imgIn.alpha = 0.5f
        binding.imgOut.alpha = 1.0f
        binding.imgAdavancepunch.alpha = 1.0f
        binding.lOut.isClickable=true
        binding.lAdvancePunch.isClickable = true

        binding.imgIn.setImageResource(R.drawable.sign_in_icon)
        binding.imgOut.setImageResource(R.drawable.sign_out_icon)
        binding.imgAdavancepunch.setImageResource(R.drawable.advance_punch_new)
        binding.signInTxt.setTextColor(resources.getColor(R.color.grey_attendance))
        binding.signOutTxt.setTextColor(resources.getColor(R.color.red))
        binding.signOutTxt.setTypeface(null, Typeface.BOLD)

    }


    private fun selectNoInOutClicked() {
        binding.imgIn.alpha = 1.0f
        binding.imgAdavancepunch.alpha = 1.0f

        binding.imgOut.alpha = 0.5f
        binding.imgIn.setImageResource(R.drawable.sign_in_icon)
        binding.imgOut.setImageResource(R.drawable.sign_out_icon)
        binding.imgAdavancepunch.setImageResource(R.drawable.advance_punch_new)

        //    binding.imgOut.setImageResource(R.drawable.advance_punch_gray)


    }

    fun convertDateFormat(inputDate: String, inputFormat: String, outputFormat: String): String {
        val inputFormatter = SimpleDateFormat(inputFormat)
        val date = inputFormatter.parse(inputDate)
        val outputFormatter = SimpleDateFormat(outputFormat)
        return outputFormatter.format(date)
    }

    private fun setClickListeners(activity: MainActivity?) {
        binding.lAdvancePunch.setOnClickListener(this)
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        activity?.binding!!.layout.imgInfo.setOnClickListener(this)
        activity?.binding!!.layout.imgAlert.setOnClickListener(this)
        binding.lIn.setOnClickListener(this)
        binding.lOut.setOnClickListener(this)

    }

    private fun getReasonsDetails() {
        viewModel_Reasons.getReasonsData(mContext!!)
    }

    private fun addObserver_Reasons() {
        viewModel_Reasons.reasonsResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {

                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { ReasonsResponse ->
                            val reasonsResponse = ReasonsResponse
                            if (reasonsResponse != null) {

                                arrListReasons.clear()

                                if (reasonsResponse.data != null && reasonsResponse.data.isNotEmpty()) {

                                    /*
                                                                        SnackBarUtil.showSnackbar(
                                                                            mContext,
                                                                            reasonsResponse.data.toString()
                                                                        )
                                    */

                                    for (i in 0 until reasonsResponse.data.size) {
                                        if (reasonsResponse.data[i].isDisplayType) {
                                            arrListReasons.add(reasonsResponse.data[i])
                                        }
                                    }


                                    if (arrListReasons.size > 0) {
                                        binding.lAdvancePunch.visibility = View.VISIBLE


                                    } else {
                                        binding.lAdvancePunch.visibility = View.GONE
                                    }


                                }
                                //   arrListReasons.addAll(reasonsResponse.data)
                                //   binding.rvViolation.adapter?.notifyDataSetChanged()


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


    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  addObserver_transaction()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).show_alert()
        (activity as MainActivity).show_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(mContext!!.resources.getString(R.string.attendance_txt))

        locationSettingsReceiver = LocationSettingsReceiver()
        val intentFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        mContext!!.registerReceiver(locationSettingsReceiver, intentFilter)

        /*
                if(GlobalVariables.from_background){
                  */
        /*  Log.e("background_app","Home fragment OnResume called")
                    CoroutineScope(Dispatchers.Main).launch {
                        callLocationDetailsAPI()
                        GlobalVariables.from_background=false
                    }*//*

            Log.e("background_app", "Home fragment OnResume called")

            GlobalHandler.postDelayedTask(myTaskBackground, 3 * 1000)
        }
*/
        GlobalVariables.from_background=true

        if (GlobalVariables.from_background) {
            handler.post(checkInternetRunnable)
        }

    }

    private val checkInternetRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            val application = requireActivity().application as TawajudApplication

            if (application.hasInternetConnection()) {
                Log.e("background_app", "Home fragment OnResume called")
                if (UserShardPrefrences.getMulipleLoc(context)==true) {
                    callWorkLocationAPI()
                }
                callLocationDetailsAPI(false)

                GlobalVariables.b_delay_attendance = false
                if (!GlobalVariables.b_delay_attendance) {
                    GlobalHandler.postDelayedTask(myTask, 5 * 1000)
                }

            } else {
                // Repeat the check after 1 second if there is no internet connection
                handler.postDelayed(this, 1000)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val myTaskBackground = Runnable {
        Log.e("After","This will be executed after 2 seconds")
        callLocationDetailsAPI(false)
        //  GlobalVariables.from_background=false

    }

    private fun navigateToNotifications() {
        replaceFragment(AnnouncementsFragment(), R.id.flFragment, true)
    }
    private fun navigateToUserInfo() {
        replaceFragment(UserInfoFragment(), R.id.flFragment, true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {

        when (v?.id) {


            R.id.img_back ->{
                (activity as MainActivity).onBackPressed()
                binding.mapView.visibility=View.GONE
            }
       /*     R.id.img_info -> {

                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE

                navigateToUserInfo()
            }

            R.id.img_alert -> {

                (activity as MainActivity)?.binding!!.layout.txtAnnouncementCount.visibility =
                    View.GONE
                navigateToNotifications()
            }*/
            R.id.l_advance_punch -> {


                if(b_enable_transaction&& allowedGPSFlag != 0
                    &&!isSpoofed(locationCurrent!!,mContext!!)
                    ||(isSpoofed(locationCurrent!!,mContext!!)&&(allowedGPSFlag==3))) {
                    if (b_hasLastTransaction && b_allowMobileOutpunch) {
                        showAdvancePunchDialog()
                    }
                    else{

                        binding.imgAdavancepunch.alpha = 1.0f
                        binding.imgAdavancepunch.isClickable=false
                       // toast(resources.getString(R.string.adv_punch_access))
                    }

                }
                else if (b_enable_transaction && allowedGPSFlag == 0){
                    toast(resources.getString(R.string.please_wait))
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
                        }else {
                            if (isSpoofed(
                                    locationCurrent!!,
                                    mContext!!
                                ) && (allowedGPSFlag == 2 || allowedGPSFlag == 1)
                            ) {
                                SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext,
                                    resources.getString(R.string.warning_spoof_loc),
                                    R.drawable.caution,
                                    resources.getColor(R.color.red),
                                    SnackBarUtil.OnClickListenerNew {
                                        //Do nothing on Ok btn click
                                    })
                            }
                        }
                    if(!b_show_out_office_dialog && mContext!=null &&!isSpoofed(locationCurrent!!,
                            mContext!!)){
                        //  showDialog(mContext!!,mContext!!.resources.getString(R.string.out_of_office_txt),mContext!!.resources.getString(R.string.not_allowed_new),R.drawable.caution,resources.getColor(R.color.red))
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





            }

            R.id.l_out ->{





                if(checkLocationPermission()&&isLocationEnabled()&&!isSpoofed(locationCurrent!!,mContext!!)
                        ||(isSpoofed(locationCurrent!!,mContext!!)&&(allowedGPSFlag==3)))
                {
                    if (hasActiveNotifications()) {
                        cancelAllNotifications()
                    }
                    //  outClicked()

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
            R.id.l_in -> {

                /*
                                if (checkLocationPermission())
                                {
                                    if (hasActiveNotifications()) {
                                        cancelAllNotifications()
                                    }
                                    inClicked()
                                }
                                else{
                                    requestLocationPermission()

                                }
                */


                if((checkLocationPermission()&&isLocationEnabled()&&!isSpoofed(locationCurrent!!,mContext!!))
                    ||(!isSpoofed(locationCurrent!!,mContext!!) &&(allowedGPSFlag==1||allowedGPSFlag==2))
                    ||(isSpoofed(locationCurrent!!,mContext!!) &&(allowedGPSFlag==3)))
                {
                    if (hasActiveNotifications()) {
                        cancelAllNotifications()
                    }
                    if(reasonStr_current!=null){
                        if(reasonStr_current.equals("I")){
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(
                                context,
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
                }

            }

        }

    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
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
                else if (b_hasLastTransaction && b_allowMobileOutpunch &&!isWithinRadius!!) {


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

    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun inClicked() {
        if (b_delay) {
            if (b_enable_transaction && allowedGPSFlag != 0 && !b_hasLastTransaction ||b_enable_transaction && allowedGPSFlag != 0
                &&!isSpoofed(locationCurrent!!,mContext!!)
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
    private fun hasActiveNotifications(): Boolean {
        // Check if the fragment is attached to its activity
        if (isAdded) {
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNotifications = notificationManager.activeNotifications
                activeNotifications.isNotEmpty()
            } else {
                // For older devices, we assume notifications might exist
                // Alternatively, you can maintain your own flag/state to track posted notifications.
                true
            }
        }
        return false
    }

    private fun cancelAllNotifications() {
        if (isAdded) {
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
                // icon.setImageResource(drawable_icon)

                val resourceName = context.resources.getResourceEntryName(drawable_icon)
                Log.e("resourceName", resourceName)
                if (resourceName.contains("caution")) {
                    icon.setImageResource(drawable_icon)
                }
                diag_ok.setBackgroundColor(color)
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
                diag_ok.setOnClickListener { show.dismiss()
                    b_show_out_office_dialog = false

                }

                b_show_out_office_dialog = true
                show.setCancelable(false)
            }
//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }


    }
    private var alertDialog: AlertDialog? = null

    private fun showAdvancePunchDialog() {

        val inflate = LayoutInflater.from(context).inflate(R.layout.advance_punch_fragment, null)

        val builder = AlertDialog.Builder(
            mContext!!
        )


        alertDialog = builder.setIcon(android.R.drawable.ic_dialog_alert)
            .setView(inflate)
            .setOnDismissListener {
            }
            .show()





        val window = alertDialog!!.window
        window?.decorView?.setBackgroundResource(android.R.color.transparent)

        builder.setCancelable(true)
        val gv_advancepunch: GridView =
            inflate.findViewById<View>(R.id.grid_advancepunch) as GridView


        // Create an object of CustomAdapter and set Adapter to GirdView
        val customAdapter = GridView_AdvancePunch_Adapter(mContext, arrListReasons,this)
        gv_advancepunch.adapter = customAdapter


    }

    private fun dismissAlertDialog() {
        alertDialog?.dismiss()
    }
    /* fun onPositiveButtonClick(dialog: DialogInterface) {
         // Handle positive button click if needed
         dialog.dismiss() // Dismiss the dialog
     }*/

    private fun addMarkeronMap(latLng: LatLng, title: String): MarkerOptions? {
        return MarkerOptions()
            .position(latLng)
            .icon(BitmapDescriptorFactory.defaultMarker())
            .title(title)
    }




    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    /*
        override fun onLocationChanged(location: Location) {
            //  Log.d("mylog", "Got Location: " + location.latitude + ", " + location.longitude)


            val dFormat = DecimalFormat("#.######")
            latitude = java.lang.Double.valueOf(dFormat.format(location.latitude))
            longitude = java.lang.Double.valueOf(dFormat.format(location.longitude))



            currentLocation!!.latitude = latitude
            currentLocation!!.longitude = longitude

            str_work_loc = UserShardPrefrences.getGpsCoordinates(mContext)
            val parts: List<String> = str_work_loc!!.split(",")
            workLoc.latitude = parts[0].toDouble()
            workLoc.longitude = parts[1].toDouble()
            var distance: Float = getDistanceinMeter(workLoc, currentLocation!!)

            binding.txtDistance.setText("Distance: "+String.format("%.2f", distance)+" meters")


            if(distance< UserShardPrefrences.getAllowedDistance(mContext).toDouble()){

                */
    /*   Toast.makeText(
                       mContext!!,"With in Range " +
                       "Got Coordinates: " + latitude + ", " + longitude,
                       Toast.LENGTH_SHORT
                   ).show()
       *//*


            b_enable_transaction = true


        }

        else{
            //  SnackBarUtil.showSnackbar(mContext,"Out of Range")
            b_enable_transaction = false


            //   showDialog(mContext!!,"Out of office","You can't able to perform any transactions")


            */
    /*   Toast.makeText(
                       mContext!!,"Out of Range " +
                               "Got Coordinates: " + latitude + ", " + longitude,
                       Toast.LENGTH_SHORT
                   ).show()*//*

        }


        */
    /*  Toast.makeText(
                  mContext!!,
                  "Got Coordinates: $latitude, $longitude",
                  Toast.LENGTH_SHORT
              ).show()
      *//*


        markerLatLng = LatLng(latitude, longitude)

        //  binding.lngitdeLatitdeTxt.text = "$latitude,$longitude"
        locationManager!!.removeUpdates(this)
        setUpMap(latitude, longitude)
    }
*/
    private fun getDistanceinMeter(workloc: Location?, myLoc: Location): Float {
        return myLoc.distanceTo(workloc!!) /*/ 1000*/
    }



    private fun setUpMap(latitude: Double, longitude: Double) {
        /* val supportMapFragment =
             childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment*/
        if (fragmentManager != null) {
            /*
                         supportMapFragment =
                            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            */
            supportMapFragment?.getMapAsync { googleMap ->
                mGoogleMap = googleMap

                val marker: View =
                    (mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                        R.layout.custom_marker_layout,
                        null
                    )
                val numTxt = marker.findViewById<View>(R.id.tv_map_list_disc_price) as TextView
                numTxt.text = "27"

                markerOptions =
                    addMarkeronMap(
                        LatLng(latitude, longitude),
                        "You"
                    )

                mGoogleMap!!.addMarker(markerOptions!!)
                if (!isFirstZoom) {

                    mGoogleMap!!.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(latitude, longitude),
                            9f
                        )
                    )
                    isFirstZoom = true
                }




            }
        }


        /*  val marker: View =
              (mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                  R.layout.custom_marker_layout,
                  null
              )
          val numTxt = marker.findViewById<View>(R.id.tv_map_list_disc_price) as TextView
          numTxt.text = "27"
          customMarker = mGoogleMap!!.addMarker(
              MarkerOptions()
                  .position(markerLatLng!!)
                  .title("Title")
                  .snippet("Description")
                  .icon(
                      createDrawableFromView(
                          mContext!!,
                          marker
                      )?.let {
                          BitmapDescriptorFactory.fromBitmap(
                              it
                          )
                      }
                  )
          )*/
        val mapView = supportMapFragment!!.view
        /*
                if (mapView!!.viewTreeObserver.isAlive) {
                    mapView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                        @SuppressLint("NewApi") // We check which build version we are using.
                        override fun onGlobalLayout() {
                            val bounds = markerLatLng?.let { LatLngBounds.Builder().include(it).build() }
                            mapView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            //       mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                        }
                    })
                }
        */
    }

    fun createDrawableFromView(context: Context, view: View): Bitmap? {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.buildDrawingCache()
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    companion object {
        /*  private const val GPS_TIME_INTERVAL = 1000 * 60 * 5 // get gps location every 1 min
          private const val GPS_DISTANCE = 1000 // set the distance value in meter
          private const val HANDLER_DELAY = 1000 * 60 * 5
          private const val START_HANDLER_DELAY = 0*/

        const val LOCATION_PERMISSION_REQUEST_CODE = 100
        private const val REQUEST_NOTIFICATION_PERMISSION = 1

        private const val GPS_TIME_INTERVAL = 1000 * 60 * 5 // get gps location every 1 min
        private const val GPS_DISTANCE = 1000 // set the distance value in meter
        private const val HANDLER_DELAY = 1000 * 2 * 5
        private const val START_HANDLER_DELAY = 0
        private const val POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE = 1


        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        const val PERMISSION_ALL = 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAdvancePunchItemClick(position: Int) {
        dismissAlertDialog()


        callCheckIn_OutAPI(position)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callCheckIn_OutAPI(reasonId: Int) {
        addObserver_check_in_out()
        callSaveManualEntry(reasonId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun callSaveManualEntry(reasonId: Int) {

        Log.e("latitude:longitude","$latitude,$longitude")
        val requestDate: String = DateTime_Op.getCurrentDateTime(Keys.DateFormate1)
        val reqTime: String = DateTime_Op.getCurrentDateTime(Keys.TimeFormate24)



        val saveManualEntryRequest = SaveManualEntryRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            Instant.now().toString(),
            reasonId,

            Instant.now().toString(),
            "",
            //"24.49742,54.367506",
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addObserver_check_in_out() {

        viewModel_SaveEntry.saveEntrytransactionResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse

                            if (requestResponse != null) {

                                if (requestResponse.message.equals(mContext!!.resources.getString(R.string.response_error_txt))) {
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,requestResponse.data.msg,R.drawable.caution,resources.getColor(R.color.red))
                                    /*
                                                                        SnackBarUtil.showSnackbar(
                                                                            context,
                                                                            requestResponse.data.msg
                                                                        )*/

                                } else {
                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,requestResponse.data.msg,R.drawable.app_icon,colorPrimary)

                                    /*    SnackBarUtil.showSnackbar(
                                            context,
                                            requestResponse.data.msg
                                        )
    */

                                    // callLastTransactionAPI()

                                    if (b_mustPunchPhysical && !b_hasLastTransaction) {
                                        PrefUtils.cancelLocationJob(mContext!!.applicationContext,1)
                                        UserShardPrefrences.setLastCallTime(mContext,0L)
                                        WorkManager.getInstance(mContext!!.applicationContext)
                                            .cancelAllWork()
                                        UserShardPrefrences.setinHome(mContext,false)
                                        UserShardPrefrences.setinAttendance(mContext,true)
                                        b_custom_notification = false
                                        callLastTransactionTemp()
                                        //showTimer(mobilePunchConsiderDuration)
                                    } else {
                                        callLastTransactionAPI(true)
                                    }


                                }

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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun callLastTransactionTemp() {
        addObserver_transaction_Temp()
        postTransactionDetails_Temp()

    }

    private fun postTransactionDetails_Temp() {
        val transactionRequest = CommonRequest(
            UserShardPrefrences.getUserInfo(mContext).fKEmployeeId,
            //172,
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )
        viewModel_transaction_temp.getTransactionData(mContext!!, transactionRequest)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addObserver_transaction_Temp() {
        viewModel_transaction_temp.transactionResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {


                        (activity as MainActivity).hideProgressBar()

                        response.data?.let { requestResponse ->
                            val requestResponse = requestResponse

                            if (requestResponse != null) {

                                if (requestResponse.data != null) {



                                    binding.dateTimeTxt.text = LocaleHelper.arabicToEnglish(convertDateFormat(
                                        requestResponse.data[0].moveDate,
                                        "yyyy-MM-dd'T'HH:mm:ss",
                                        "dd/MM/yyyy"
                                    )) + " " + requestResponse.data[0].moveTime

                                    str_move_time = requestResponse.data[0].dMoveTime

                                    if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
                                        //    binding.txtInOut.text = requestResponse.data[0].reasonEn



                                        reasonId  = requestResponse.data[0].reasonId

                                        if(reasonId == 0){
                                            selectInClickedForTempTransaction()
                                            binding.validateTypeTxt.text=requestResponse.data[0].reasonEn
                                            //  binding.switchInOut.isChecked = true;
                                        }

                                        else{
                                            selectInClickedForTempTransaction()
                                            binding.validateTypeTxt.text=requestResponse.data[0].reasonEn

                                            //binding.switchInOut.isChecked = false;
                                        }






                                    }else{
                                        reasonId  = requestResponse.data[0].reasonId

                                        if(reasonId == 0){
                                            selectInClickedForTempTransaction()
                                            binding.validateTypeTxt.text=requestResponse.data[0].reasonAr
                                            //  binding.switchInOut.isChecked = true;
                                        }

                                        else{
                                            selectInClickedForTempTransaction()
                                            binding.validateTypeTxt.text=requestResponse.data[0].reasonAr
                                            //binding.switchInOut.isChecked = false;
                                        }
                                    }

                                    compare_transaction_current_time(mobilePunchConsiderDuration)

                                } else {
                                    /*
                                                                        SnackBarUtil.showSnackbar(
                                                                            context,
                                                                            requestResponse.message.toString(),
                                                                            false
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
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(context,message,R.drawable.caution,resources.getColor(R.color.red))


                            // SnackBarUtil.showSnackbar(context, message, false)
                        }
                    }

                    is Resource.Loading -> {
                        (activity as MainActivity).showProgressBar()

                    }
                }
            }
        }
    }

    private fun selectInClickedForTempTransaction() {
        binding.lIn.alpha = 0.6f
        binding.lAdvancePunch.isClickable = false
        binding.lOut.isClickable=false
        binding.signOutTxt.setTextColor(resources.getColor(R.color.grey_attendance))
        binding.advancePunchTxt.setTextColor(resources.getColor(R.color.grey_attendance))
        binding.imgOut.setImageResource(R.drawable.sign_out_icon_gray)
        binding.imgAdavancepunch.setImageResource(R.drawable.advance_punch_gray)


        binding.imgIn.setImageResource(R.drawable.sign_in_icon)
        binding.signInTxt.setTextColor(resources.getColor(R.color.grey_attendance))

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    private fun compare_transaction_current_time(mobilePunchConsiderDuration: Int) {


        var newT: Date? = null
        var currentT: Date? = null

        val newTime: String
        val currentTime: String

        var myTime: String = str_move_time.toString()
        val time = myTime.split("T".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        myTime = time[1]
        val tim = myTime.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        myTime = tim[0]
        val df = SimpleDateFormat("HH:mm:ss")
        val d = df.parse(myTime)
        val cal =Calendar.getInstance(Locale.ENGLISH)
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
                moveTime =
                    if (timeLength == 6) LocalTime.parse(
                        str_move_time,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                    )
                        .format(DateTimeFormatter.ofPattern("HH:mm"))
                    else LocalTime.parse(
                        str_move_time,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                    )
                        .format(DateTimeFormatter.ofPattern("HH:mm"))
            }

            // binding.dateTimeTxt.text = date.toString()+" "+moveTime
            binding.dateTimeTxt.text = getCurrentDate()+" "+ moveTime


            /*
                        binding.dateTimeTxt.text = convertDateFormat(
                            transactionResponse.data[0].moveDate,
                            "yyyy-MM-dd'T'HH:mm:ss",
                            "dd/MM/yyyy"
                        ) + " " + transactionResponse.data[0].moveTime
            */


            assert(newT != null)
            val diff = newT.time - currentT.time
            val formula = (1000 % 60).toLong()
            val diffSeconds = diff / formula
            val diffInSeconds = TimeUnit.MILLISECONDS.toMillis(diff)
            if (UserShardPrefrences.getAllowedGPSFlag(context) !== 3) {
                stopCountdownTimer()

                binding.txtTimer.visibility = View.VISIBLE

                showTimer(diffInSeconds)
            }
            else {
                callLastTransactionAPI(true)
            }
        } else {
            callLastTransactionAPI(true)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val currentDate = LocalDate.now().format(formatter)
        return currentDate
    }

    fun stopCountdownTimer() {
        if(countDown_Ticker!=null)
            countDown_Ticker?.cancel()

        binding.txtTimer.visibility = View.GONE
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

                            //                          callLastTransactionAPI(false)

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
        }else{
            updatedDiffSeconds=0L
        }
        //  startLocationService()
        startHandler()


        if(UserShardPrefrences.getisMinute(mContext)){
            reminderIntervalMinutes =     UserShardPrefrences.getminutelyReminder(mContext)!!.toLong()

        }
        else if (UserShardPrefrences.getisCustom(mContext)){
            reminderIntervalMinutes = UserShardPrefrences.getCustomReminder(mContext)!!.toLong()

        }
        else {
            reminderIntervalMinutes = 0L
        }


        Log.e("Reminder:getminutelyReminder",
            UserShardPrefrences.getminutelyReminder(mContext)!!.toLong().toString()
        )
        Log.e("Reminder:getCustomReminder",
            UserShardPrefrences.getCustomReminder(mContext)!!.toLong().toString()
        )

        //  reminderIntervalMinutes =1L
        if (UserShardPrefrences.getLastCallTime(context)!! == 0L){
            UserShardPrefrences.setLastCallTime(context,System.currentTimeMillis())
        }
        lastCallTime=UserShardPrefrences.getLastCallTime(context)


        countDown_Ticker = object : CountDownTimer(diffInSeconds, 1000) {
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
                                UserShardPrefrences.getNotificationData(mContext)[0].isActive && !b_custom_notification
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
                                UserShardPrefrences.setLastCallTime(
                                    mContext!!.applicationContext,
                                    currentTime
                                )

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
                isWorkScheduled("LocationServiceAttendanceWork") { isScheduled ->
                    if (isScheduled) {
                        // Cancel the existing work
                        WorkManager.getInstance(mContext!!.applicationContext)
                            .cancelUniqueWork("LocationServiceAttendanceWork")
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
    private fun convertMillisToDateTimeString(millis: Long): String {
        val date = Date(millis)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(date)
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
            /*  val intent = Intent(mContext!!.applicationContext, LocationServiceBackUp::class.java)
              intent.putExtra("distance", allowedDistance.toInt())
              intent.putExtra("gpsCoordinates", gpsCoordinates)
              intent.putExtra("tawajud_NiotificationsTypesId",notificationTypeId)
              intent.putExtra("timer_txt", minutes)
              intent.putExtra("Last_transactionStatus", b_hasLastTransaction)

              intent.putExtra("car_park_timer", true)*/
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
            //  requireContext().startService(mContext!!, intent)

            // requireContext().startService(intent)


        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION
            )

        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleLocationService(
        allowedDistance: Int,
        gpsCoordinates: String,
        notificationTypeId: Int?,
        minutes: String,
        lastTransactionStatus: Boolean,
        carParkTimer: Boolean
    ){
        if (UserShardPrefrences.getInAttendance(mContext)!!){
            val uniqueWorkName = "LocationServiceAttendanceWork"

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
    private val myTaskTransaction = Runnable {
        Log.e("After", "This will be executed after 3 seconds")
        callLastTransactionAPI(false)

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

        if(view!=null) {

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


                                        var isWithinRadius = false


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

                    binding.lngitdeLatitdeTxt.text = workLocation.gpsCoordinates

                    UserShardPrefrences.saveGpsCoordinates(
                        mContext, workLocation.gpsCoordinates
                    )



                    binding.locCordinatesTxt.visibility = View.VISIBLE
                    if (UserShardPrefrences.getLanguage(mContext)
                            .equals("1")
                    ) {
                        binding.locCordinatesTxt.text =
                            workLocation.workLocationArabicName
                    } else {
                        binding.locCordinatesTxt.text =
                            workLocation.workLocationName
                    }



                    binding.txtDistance.text =
                        mContext!!.resources.getString(R.string.distance_txt) + String.format(
                            "%.2f", distance
                        ) + requireContext().resources.getString(
                            R.string.meters_txt
                        )


                }
            }
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
}
