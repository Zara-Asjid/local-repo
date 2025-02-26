package com.sait.tawajudpremiumplusnewfeatured.ui.login


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.request.target.Target
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvm_application.util.extension.hide
import com.example.mvvm_application.util.extension.show
import com.pixplicity.easyprefs.library.Prefs
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.login.viewmodels.LoginViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.ColorUtils
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.Keyboard_Op
import com.sait.tawajudpremiumplusnewfeatured.util.PrefKeys
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.view.View.GONE
import android.view.View.OnFocusChangeListener
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.mvvm_application.util.extension.hideKeyboard
import com.sait.tawajudpremiumplusnewfeatured.databinding.ActivityLoginBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.home.HomeFragmentNew
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.WorkLocationRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.NotificationData
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.NotificationRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.login.viewmodels.NotificationViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.b_delay_attendance
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.ensureValidUrlScheme
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.isSpoofed
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.isUrlReachable
import kotlinx.coroutines.launch
import java.util.ArrayList

class LoginFragment : BaseFragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener,OnFocusChangeListener,TextWatcher,View.OnTouchListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var notificationViewModel: NotificationViewModel

    private var _binding: ActivityLoginBinding? = null
    private lateinit var arrListNotifications: ArrayList<NotificationData>

    private  var  isAllFabsVisible:Boolean = false

    private  var  isPasswordVisible:Boolean = false

    var isTouchId:Boolean= false
    public var isUpdateCheckInProgress = false

private var b_FromReStart:Boolean = false
    private var mContext: Context? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Only inflate the view once and use it throughout
        binding = ActivityLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        notificationViewModel= ViewModelProvider(this).get(NotificationViewModel::class.java)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext() // Now it's safe to require context
        setClickListeners()
        // Show the progress bar while image is loading

        GlobalVariables.colorPrimary =  Color.parseColor(
            ColorUtils.toHexColor(
                ColorUtils.resolveColorAttribute(
                    mContext,
                    R.attr.themecolor
                )
            )
        )

        setThemeBackground()

        isBiometricSensorAvailable(mContext!!)

        if(UserShardPrefrences.getLanguage(mContext).equals("0")){
            binding.fabLanguage.setImageResource(R.drawable.ar_icon)
        }
        else{
            binding.fabLanguage.setImageResource(R.drawable.en_icon)
        }

        if (UserShardPrefrences.isSaveCradentials(mContext)) {
            binding.cbRemember.setChecked(true)
            binding.etUsername.setText(UserShardPrefrences.getUserName(mContext))
            binding.etPassword.setText(UserShardPrefrences.getPassword(mContext))
        } else {
            binding.cbRemember.setChecked(false)
        }


        binding.layout.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            val heightDiff: Int =
                binding.layout.getRootView().getHeight() - binding.layout.getHeight()
            if (heightDiff > 100) {
                if (UserShardPrefrences.getFingerSwitch(mContext)) {
                    binding.txtLogin.text = mContext!!.resources.getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.VISIBLE
                    isTouchId = true

                } else {
                    binding.txtLogin.text = mContext!!.resources.getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.GONE
                    isTouchId = false
                }
            } else {
                if (UserShardPrefrences.getFingerSwitch(mContext)) {
                    binding.txtLogin.text = mContext!!.resources.getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.VISIBLE
                    isTouchId = true

                } else {
                    binding.txtLogin.text =mContext!!. resources.getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.GONE
                    isTouchId = false
                }
            }
        })
        setClickListeners()
        val baseUrl = UserShardPrefrences.getBaseUrl(mContext!!)
        val fullUrl = ensureValidUrlScheme(DateTime_Op.removeApiSegment(baseUrl) + "/Logo/tawajud.png")
        if (fullUrl != null) {
            isUrlReachable(fullUrl) { isValid ->
                if (getView()!=null && isAdded){
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted  {
                        if (isValid) {
                            Glide.with(this@LoginFragment)
                                .load(fullUrl)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable>,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        binding.imgLogo.setImageResource(R.drawable.logo_b)
                                        return true
                                    }
                                    override fun onResourceReady(
                                        resource: Drawable,
                                        model: Any,
                                        target: Target<Drawable>?,
                                        dataSource: DataSource,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        return false
                                    }
                                })
                                .into(binding.imgLogo)
                        } else {
                            binding.imgLogo.setImageResource(R.drawable.logo_b)
                        }
                    }
                }
            }
        } else {
            binding.imgLogo.setImageResource(R.drawable.logo_b)
        }

        Log.e("logo", DateTime_Op.removeApiSegment(UserShardPrefrences.getBaseUrl(mContext!!))+"/Logo/tawajud.png")

        GlobalVariables.b_delay = false
        b_delay_attendance= false

    }









    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = activity?.applicationContext
    }



    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideProgressBar()
        (activity as MainActivity).hideToolbar()
        (activity as MainActivity).hideBottomNavigationView()

    }


    private fun setThemeBackground() {
        val themeName = resources.getResourceEntryName(UserShardPrefrences.getCurrentTheme(mContext))
        Log.e("themeName",themeName)
        if(themeName.contains("green")){
            binding.imgBrown.setBackgroundResource(R.drawable.brown_circle)
            binding.imgSkyblue.setBackgroundResource(R.drawable.skyblue_circle)
            binding.imgGreen.setBackgroundResource(R.drawable.green_circle_boarder)
            binding.imgPswd.setBackgroundResource(R.drawable.password)
        }
        else  if(themeName.contains("brown")){
            binding.imgBrown.setBackgroundResource(R.drawable.brown_circle_boarder)
            binding.imgSkyblue.setBackgroundResource(R.drawable.skyblue_circle)
            binding.imgGreen.setBackgroundResource(R.drawable.green_circle)
            binding.imgPswd.setBackgroundResource(R.drawable.password_br)

        }
        else{
            binding.imgBrown.setBackgroundResource(R.drawable.brown_circle)
            binding.imgSkyblue.setBackgroundResource(R.drawable.skyblue_circle_boarder)
            binding.imgGreen.setBackgroundResource(R.drawable.green_circle)
            binding.imgPswd.setBackgroundResource(R.drawable.password_b)

        }

    }





    override fun onPause() {
        super.onPause()

        if (UserShardPrefrences.isSaveCradentials(mContext)) {
            UserShardPrefrences.saveCredentials(
                mContext!!, binding.etUsername.getText().toString(),
                binding.etPassword.getText().toString()
            )
        }
    }


    private fun setClickListeners() {
        binding.txtLogin.setOnClickListener(this)
        binding.cbRemember.setOnCheckedChangeListener(this)
        binding.fabpluse.setOnClickListener(this)
        binding.fabLanguage.setOnClickListener(this)
        binding.fabRestart.setOnClickListener(this)
        binding.imgFingerPrint.setOnClickListener(this)
        binding.imgHidepassword.setOnClickListener(this)
        binding.imgTouchid.setOnClickListener(this)
        binding.etUsername.onFocusChangeListener = this
        binding.etPassword.onFocusChangeListener=this
        binding.etPassword.addTextChangedListener(this)
        binding.subLayout.setOnTouchListener(this)
        binding.imgGreen!!.setOnClickListener(this)
        binding.imgSkyblue!!.setOnClickListener(this)


        binding.imgBrown!!.setOnClickListener(this)
    }


    private fun addObserver(userName: String, password: String) {
        viewModel.loginResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.progressOverlay.visibility = View.GONE
                        response.data?.let { loginResponse ->
                            val loginData = loginResponse
                            if (loginData != null) {

                                if (loginData != null && loginData.message == mContext!!.resources.getString(R.string.success_txt)) {
                                    Log.e("Login:", loginData.toString())
                                    UserShardPrefrences.setUsername_new(mContext,userName)
                                    UserShardPrefrences.setPassword_new(mContext,password)


                                    Log.e("password_login", password)
                                    UserShardPrefrences.saveUserInfo(mContext, loginData.data)
                                    UserShardPrefrences.setuserLogin(mContext, true)
                                    UserShardPrefrences.setmultipleLoc(mContext,loginData.data.allowMultipleLocations)
                                    UserShardPrefrences.setminutelyReminder(mContext,loginData.data.minutelyReminder)
                                    UserShardPrefrences.setCustomReminder(mContext,loginData.data.customReminder)

                                    if(loginData.data.role.contains("Admin")){
                                        UserShardPrefrences.setisAdmin(mContext,true)
                                    }
                                    else{
                                        UserShardPrefrences.setisAdmin(mContext,false)
                                    }


                                    if(loginData.data.minutelyReminder != 0){
                                        UserShardPrefrences.setisMinute(mContext,true)
                                        UserShardPrefrences.setisCustom(mContext,false)

                                    }
                                    else{
                                        UserShardPrefrences.setisMinute(mContext,false)
                                        UserShardPrefrences.setisCustom(mContext,true)

                                    }


                                    if(loginData.data.isHR){
                                        UserShardPrefrences.setisHR(mContext,true)
                                    }
                                    else{
                                        UserShardPrefrences.setisHR(mContext,false)
                                    }


                                    if(loginData.data.isManager){
                                        UserShardPrefrences.setisManager(mContext,true)
                                    }
                                    else{
                                        UserShardPrefrences.setisManager(mContext,false)
                                    }

                                    //UserShardPrefrences.setisManager(this,loginData.data.isManager)
                                   // Prefs.putString(PrefKeys.Auth_key, loginData.data.token)
                                    UserShardPrefrences.setLoginToken(mContext,loginData.data.token)

                                    callNotificationTypeApi()



                                }
                                else if (loginData.statusCode == 200 && loginData.message == mContext!!.resources.getString(R.string.response_error_txt)) {
                                    /*
                                                                        SnackBarUtil.showSnackbar(
                                                                            this,
                                                                            loginData.data.error
                                                                        )
                                    */

                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext!!,loginData.data.error,R.drawable.caution,resources.getColor(R.color.red))

                                } else {
                                    // SnackBarUtil.showSnackbar(this, loginData.message, false)

                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext!!,loginData.message,R.drawable.caution,resources.getColor(R.color.red))

                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.progressOverlay.visibility = View.GONE
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext!!,message,R.drawable.caution,resources.getColor(R.color.red))

                            //SnackBarUtil.showSnackbar(this, message, false)

                        }
                    }


                    is Resource.Loading -> {
                        binding.progressOverlay.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun postLoginDetails(userName: String, password: String) {
        Log.e("device_id",  UserShardPrefrences.getUniqueId(mContext).toString())
        viewModel.getLoginData(
            mContext!!,
            userName,
            password,
            0,
            UserShardPrefrences.getLanguage(mContext)!!.toInt(),
            UserShardPrefrences.getUniqueId(mContext).toString(),
            Build.VERSION.RELEASE,
            "Android",
            UserShardPrefrences.getNoUsers(mContext!!)!!.toInt()
        )


    }


    private fun navigateToMainActivity() {

       /* startActivity(Intent(this, MainActivity::class.java))
        finish()*/


        replaceFragment(HomeFragmentNew(), R.id.flFragment, true)


    }


    private fun loginClicked() {
        // Reset errors.
        binding.etUsername.setError(null)
        binding.etPassword.setError(null)
        val email: String = binding.etUsername.getText().toString()
        val password: String = binding.etPassword.getText().toString()
        var cancel = false
        var focusView: View? = null
        if (TextUtils.isEmpty(email)) {
            binding.etUsername.setError(getString(R.string.error_field_required))
            focusView = binding.etUsername
            cancel = true
        }

        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError(getString(R.string.error_field_required))
            binding.imgHidepassword.hide()
            focusView = binding.etPassword
            cancel = true
        }

        if (cancel) {
            // Keep focus on the first field with an error
            if (TextUtils.isEmpty(email)) {
                binding.etUsername.requestFocus()
            } else {
                binding.etPassword.requestFocus()
            }
            // focusView!!.requestFocus()
        } else {
            try {
                /* binding.imgTouchid!!.visibility = View.GONE
                 isTouchId = false
                 binding.txtLogin.text = "SIGN IN"*/

                Keyboard_Op.hide(mContext)


                        if (UserShardPrefrences.isSaveCradentials(mContext)) {
                            UserShardPrefrences.saveCredentials(
                                mContext, binding.etUsername.getText().toString(),
                                binding.etPassword.getText().toString()
                            )
                        }
                        callLoginAPI(binding.etUsername.text.toString(),binding.etPassword.text.toString())

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
    }

    private fun callNotificationTypeApi() {
        addObserverForNotification()
        postNotifciationDetails()
    }

    private fun postNotifciationDetails() {
        val workLocationRequest = NotificationRequest(
            UserShardPrefrences.getLanguage(mContext)!!.toInt()
        )
        notificationViewModel.getNotificationTypeData(
            mContext!!,
            workLocationRequest
        )
    }

    private fun addObserverForNotification() {
        notificationViewModel.notificationResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.progressOverlay.visibility = View.GONE

                        response.data?.let { notificationTypesResponse ->
                            val notificationResponse = notificationTypesResponse
                            if (notificationResponse != null) {
                                navigateToMainActivity()
                                arrListNotifications= arrayListOf()
                                if (notificationResponse != null && notificationResponse.message == mContext!!.resources.getString(R.string.success_txt)) {
                                    Log.e("Notification:", notificationResponse.toString())
                                    for (i in 0 until notificationResponse.data.size) {
                                        arrListNotifications.addAll(
                                            listOf(
                                                notificationResponse.data[i]
                                            )
                                        )
                                    }
                                   UserShardPrefrences.setNotificationData(mContext,arrListNotifications)

                                }
                                else if (notificationResponse.statusCode == 200 && notificationResponse.message == mContext!!.resources.getString(R.string.response_error_txt)) {
                                    /*
                                                                        SnackBarUtil.showSnackbar(
                                                                            this,
                                                                            loginData.data.error
                                                                        )
                                    */

                                  //  SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext!!,notificationResponse.data.error,R.drawable.caution,resources.getColor(R.color.red))

                                } else {
                                    // SnackBarUtil.showSnackbar(this, loginData.message, false)

                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext!!,notificationResponse.message,R.drawable.caution,resources.getColor(R.color.red))

                                }
                            }
                        }
                    }

                    is Resource.Error -> {
                        binding.progressOverlay.visibility = View.GONE
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(mContext!!,message,R.drawable.caution,resources.getColor(R.color.red))

                            //SnackBarUtil.showSnackbar(this, message, false)

                        }
                    }

                    is Resource.Loading -> {
                        binding.progressOverlay.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun callLoginAPI(userName: String, password: String) {
        addObserver(userName,password)
        postLoginDetails(userName,password)
    }


    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length >= 3
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onClick(p0: View?) {
        when (p0?.id) {


            R.id.img_brown -> {
                UserShardPrefrences.setCurrentTheme(mContext,R.style.AppTheme_brown)

                restartClicked(b_FromReStart)
            }

            R.id.img_green -> {
                UserShardPrefrences.setCurrentTheme(mContext,R.style.AppTheme_green)

               // restartActivity()
                restartClicked(b_FromReStart)
            }
            R.id.img_skyblue -> {
                UserShardPrefrences.setCurrentTheme(mContext,R.style.AppTheme_skyblue)

                restartClicked(b_FromReStart)
            }


            R.id.img_hidepassword ->{

                togglePasswordVisibility(binding.etPassword, binding.imgHidepassword)

            }

            R.id.img_touchid->{
                binding.etUsername.clearFocus()
                binding.etPassword.clearFocus()
                if(isTouchId){
                    fingerprintClicked()
                }


            }
            R.id.txt_login -> {




                loginClicked()
            }


            R.id.fabpluse -> {

                fabclicked()
            }

            R.id.fab_restart ->{



                UserShardPrefrences.setisRestart(mContext,true)

                b_FromReStart = true
                UserShardPrefrences.setuserLogin(mContext, false)
                UserShardPrefrences.clearUserInfo(mContext)
                restartClicked(b_FromReStart)
            }

            R.id.img_fingerPrint->
            {
                fingerprintClicked()
            }


            R.id.fab_language ->{
                langClicked()
            }
        }
    }



    private fun togglePasswordVisibility(editText: EditText, toggleIcon: ImageView) {
        //   val isPasswordVisible = editText.transformationMethod == PasswordTransformationMethod.getInstance()

        if (isPasswordVisible) {
            // Password is visible, hide it
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            toggleIcon.setImageResource(R.drawable.hide)
            isPasswordVisible = false
        } else {
            // Password is hidden, show it
            editText.transformationMethod = null
            toggleIcon.setImageResource(R.drawable.view)
            isPasswordVisible = true
        }

        // Move the cursor to the end of the text
        editText.setSelection(editText.text.length)
    }

    private fun isBiometricSensorAvailable(context: Context): Boolean {
        val packageManager = context.packageManager

        // Check for the biometric feature using PackageManager
        return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun fingerprintClicked() {



        if (isBiometricSensorAvailable(mContext!!)) {
            // Biometric sensor is available on this device
            //    Toast.makeText(this, "Biometric sensor is available", Toast.LENGTH_SHORT).show()
            showFingerPrintDialoge()

        } else {
            // Biometric sensor is not available on this device
            Toast.makeText(mContext, resources.getString(R.string.biometric_not_available), Toast.LENGTH_SHORT).show()
        }


    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showFingerPrintDialoge() {



        val biometricManager = mContext!!.getSystemService(AppCompatActivity.BIOMETRIC_SERVICE) as BiometricManager
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(resources.getString(R.string.fingerprint_authentication))
                    .setSubtitle(resources.getString(R.string.finger_on_sensor))
                    .setNegativeButtonText(resources.getString(R.string.cancel_txt))
                    .build()

                val executor = ContextCompat.getMainExecutor(mContext!!)

                val biometricPrompt = BiometricPrompt(this@LoginFragment, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            // Handle authentication success
                            callLoginAPI(
                                UserShardPrefrences.getUsername_new(mContext).toString(),
                                UserShardPrefrences.getPassword_new(mContext).toString()
                            )



                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            // Handle authentication failure

                        }
                    })

                biometricPrompt.authenticate(promptInfo)
            }
        }

        /*  val promptInfo = BiometricPrompt.PromptInfo.Builder()
              .setTitle("Fingerprint Authentication")
              .setSubtitle("Place your finger on the sensor to authenticate")
              .setNegativeButtonText("Cancel")
              .build()

          biometricPrompt.authenticate(promptInfo)*/
    }



    private fun langClicked() {
        if (UserShardPrefrences.getLanguage(mContext).equals("0")) {
            //  binding.fabLanguage.setImageResource(R.drawable.ar_icon)
            UserShardPrefrences.setLanguage(mContext, "1")
            LocaleHelper.setLocale(mContext!!, "ar")

        } else {
            // binding.fabLanguage.setImageResource(R.drawable.en_icon)
            UserShardPrefrences.setLanguage(mContext, "0")
            LocaleHelper.setLocale(mContext!!, "en")
        }

        restartFirstActivity()
    }

    private fun restartClicked(b_FromReStart: Boolean) {
        val i = mContext!!.packageManager
            .getLaunchIntentForPackage(mContext!!.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        if(b_FromReStart){
            i.putExtra("b_FromReStart",true)
        }

        startActivity(i)
    }
    private fun restartFirstActivity() {
        val i = mContext!!.applicationContext.packageManager
            .getLaunchIntentForPackage(mContext!!.applicationContext.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)



    }
    private fun fabclicked() {

        if (!isAllFabsVisible) {


            binding.fabLanguage.visibility = View.VISIBLE
            binding.fabRestart.visibility = View.VISIBLE
            isAllFabsVisible = true;
        } else {

            binding.fabLanguage.visibility = View.GONE
            binding.fabRestart.visibility = View.GONE

            isAllFabsVisible = false;
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

        when(p0!!.id){

            R.id.cb_remember -> {
                if(p0.isChecked)
                    UserShardPrefrences.setSaveCradentials(mContext, true)

                else{
                    UserShardPrefrences.setSaveCradentials(mContext, false)
                }
            }
        }

    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v!!.id) {
            R.id.et_username -> {

                if (UserShardPrefrences.getFingerSwitch(mContext)) {
                    if (hasFocus) {
                        binding.txtLogin.text = mContext!!.resources.getString(R.string.action_sign_in)
                        binding.imgTouchid!!.visibility = View.GONE
                        isTouchId = false
                    } else {
                        binding.txtLogin.text = mContext!!.resources.getString(R.string.touch_id_txt)
                        binding.imgTouchid!!.visibility = View.VISIBLE
                        isTouchId = true
                    }

                } else {


                    binding.txtLogin.text = mContext!!.resources.getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.GONE
                    isTouchId = false


                }
            }

            R.id.et_password -> {
                if (UserShardPrefrences.getFingerSwitch(mContext)) {
                    if (hasFocus) {
                        binding.txtLogin.text = mContext!!.resources.getString(R.string.action_sign_in)
                        binding.imgTouchid!!.visibility = View.GONE
                        isTouchId = false
                    } else {
                        binding.txtLogin.text = mContext!!.resources.getString(R.string.touch_id_txt)
                        binding.imgTouchid!!.visibility = View.VISIBLE
                        isTouchId = true
                    }

                } else {


                    binding.txtLogin.text = mContext!!.resources.getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.GONE
                    isTouchId = false


                }
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.imgHidepassword.show()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(v?.id){
            R.id.sub_layout->{

                if (event != null) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val currentFocus = requireActivity().currentFocus
                        if (currentFocus is EditText) {
                            val rect = Rect()
                            currentFocus.getGlobalVisibleRect(rect)
                            // If the touch event is outside the EditText, hide the keyboard
                            if (!rect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                                hideKeyboard()
                                    v.performClick()
                                return true // Event is handled
                            }
                        }
                    }
                }
            }
        }

        return false
    }


}
