package com.sait.tawajudpremiumplusnewfeatured.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvm_application.util.extension.hide
import com.example.mvvm_application.util.extension.show
import com.pixplicity.easyprefs.library.Prefs
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.core.BaseActivity
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.ActivityLoginBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.login.viewmodels.LoginViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.ColorUtils.resolveColorAttribute
import com.sait.tawajudpremiumplusnewfeatured.util.ColorUtils.toHexColor
import com.sait.tawajudpremiumplusnewfeatured.util.DateTime_Op
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.Keyboard_Op
import com.sait.tawajudpremiumplusnewfeatured.util.PrefKeys
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.applyTheme
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences


class LoginActivity : BaseActivity(),View.OnClickListener,OnCheckedChangeListener,OnFocusChangeListener,TextWatcher {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    private  var  isAllFabsVisible:Boolean = false

    private  var  isPasswordVisible:Boolean = false

    var isTouchId:Boolean= false

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        applyTheme()
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        _binding = ActivityLoginBinding.inflate(layoutInflater)

        supportActionBar?.hide()
        setContentView(_binding?.root)

        colorPrimary =  Color.parseColor(toHexColor(resolveColorAttribute(this, R.attr.themecolor)))

        setThemeBackground()

        isBiometricSensorAvailable(this@LoginActivity)

        if(UserShardPrefrences.getLanguage(this@LoginActivity).equals("0")){
            binding.fabLanguage.setImageResource(R.drawable.ar_icon)
        }
        else{
            binding.fabLanguage.setImageResource(R.drawable.en_icon)
        }
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this,R.color.green);
        }

        if (UserShardPrefrences.isSaveCradentials(this)) {
            binding.cbRemember.setChecked(true)
            binding.etUsername.setText(UserShardPrefrences.getUserName(this))
            binding.etPassword.setText(UserShardPrefrences.getPassword(this))
        } else {
            binding.cbRemember.setChecked(false)
        }


        binding.layout.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener {
            val heightDiff: Int =  binding.layout.getRootView().getHeight() -  binding.layout.getHeight()
            if (heightDiff > 100) {
                if(UserShardPrefrences.getFingerSwitch(this)){
                    binding.txtLogin.text = getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.VISIBLE
                    isTouchId = true

                }
                else{
                    binding.txtLogin.text = getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.GONE
                    isTouchId = false
                }
            } else {
                if(UserShardPrefrences.getFingerSwitch(this)){
                    binding.txtLogin.text = getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.VISIBLE
                    isTouchId = true

                }
                else{
                    binding.txtLogin.text = getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.GONE
                    isTouchId = false
                }
            }
        })
        setClickListeners()

        Glide.with(this!!).load(DateTime_Op.removeApiSegment(UserShardPrefrences.getBaseUrl(this@LoginActivity))+"/Logo/tawajud.png").into(binding.imgLogo)

        Log.e("logo",DateTime_Op.removeApiSegment(UserShardPrefrences.getBaseUrl(this@LoginActivity))+"/Logo/tawajud.png")

        GlobalVariables.b_delay = false

    }
    override fun onResume() {
        super.onResume()
     //   hideSystemUI()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
           // hideSystemUI()
        }
    }
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
    private fun setThemeBackground() {
        val themeName = resources.getResourceEntryName(UserShardPrefrences.getCurrentTheme(this))
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

        if (UserShardPrefrences.isSaveCradentials(this)) {
            UserShardPrefrences.saveCredentials(
                this, binding.etUsername.getText().toString(),
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

                                if (loginData != null && loginData.message == this@LoginActivity.resources.getString(R.string.success_txt)) {
                                    Log.e("Login:", loginData.toString())
                                    UserShardPrefrences.setuserLogin(this, true)
                                    UserShardPrefrences.setUsername_new(this,userName)
                                    UserShardPrefrences.setPassword_new(this,password)
                                    UserShardPrefrences.saveUserInfo(this, loginData.data)
                                    UserShardPrefrences.setuserLogin(this, true)
                                    if(loginData.data.role.contains("Admin")){
                                        UserShardPrefrences.setisAdmin(this,true)
                                    }
                                    else{
                                        UserShardPrefrences.setisAdmin(this,false)
                                    }

                                    /* if(loginData.data.role.contains("Admin")||loginData.data.role.contains("HR")){
                                         UserShardPrefrences.setisManager(this,true)
                                     }
                                     else{
                                         UserShardPrefrences.setisManager(this,false)
                                     }
 */

                                    if(loginData.data.isHR){
                                        UserShardPrefrences.setisHR(this,true)
                                    }
                                    else{
                                        UserShardPrefrences.setisHR(this,false)
                                    }


                                    if(loginData.data.isManager){
                                        UserShardPrefrences.setisManager(this,true)
                                    }
                                    else{
                                        UserShardPrefrences.setisManager(this,false)
                                    }

                                    //UserShardPrefrences.setisManager(this,loginData.data.isManager)
                                    Prefs.putString(PrefKeys.Auth_key, loginData.data.token)

                                    navigateToMainActivity()


                                }
                                else if (loginData.statusCode == 200 && loginData.message == this@LoginActivity.resources.getString(R.string.response_error_txt)) {
                                    /*
                                                                        SnackBarUtil.showSnackbar(
                                                                            this,
                                                                            loginData.data.error
                                                                        )
                                    */

                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(this,loginData.data.error,R.drawable.caution,resources.getColor(R.color.red))

                                } else {
                                    // SnackBarUtil.showSnackbar(this, loginData.message, false)

                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(this,loginData.message,R.drawable.caution,resources.getColor(R.color.red))

                                }
                            }
                        }
                    }

                    is Resource.Error -> {
                        binding.progressOverlay.visibility = View.GONE
                        response.message?.let { message ->
                            //  toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(this,message,R.drawable.caution,resources.getColor(R.color.red))

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
        Log.e("device_id",  UserShardPrefrences.getUniqueId(this).toString())
        if (password == UserShardPrefrences.getnewPassword(this)) {
            viewModel.getLoginData(
                this, userName,
                password, 0, UserShardPrefrences.getLanguage(this@LoginActivity)!!.toInt(),
                UserShardPrefrences.getUniqueId(this).toString(), Build.VERSION.RELEASE, "Android",101
            )
        } else
        {

        }
        viewModel.getLoginData(
            this, userName,
            password, 0, UserShardPrefrences.getLanguage(this@LoginActivity)!!.toInt(),
            UserShardPrefrences.getUniqueId(this).toString(), Build.VERSION.RELEASE, "Android",101
        )


    }


    private fun navigateToMainActivity() {

        startActivity(Intent(this, MainActivity::class.java))
        finish()
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
        } else if (email.length < 4) {
            binding.etUsername.setError(getString(R.string.error_invalid_username))
            focusView = binding.etUsername
            cancel = true
        }

        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError(getString(R.string.error_field_required))
            binding.imgHidepassword.hide()
            focusView = binding.etPassword
            cancel = true
        } else
            if (!isPasswordValid(password)) {
                binding.etPassword.setError(getString(R.string.error_invalid_password))
                focusView = binding.etPassword
                cancel = true
            }else

                if (cancel) {
                    focusView!!.requestFocus()
                } else {
                    try {
                        /* binding.imgTouchid!!.visibility = View.GONE
                         isTouchId = false
                         binding.txtLogin.text = "SIGN IN"*/

                        Keyboard_Op.hide(this)


                        if (UserShardPrefrences.isSaveCradentials(this)) {
                            UserShardPrefrences.saveCredentials(
                                this, binding.etUsername.getText().toString(),
                                binding.etPassword.getText().toString()
                            )
                        }
                        callLoginAPI(binding.etUsername.text.toString(),binding.etPassword.text.toString())



                    } catch (e: Exception) {
                        e.printStackTrace()
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
    private fun restartActivity() {
        val intent = intent
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        finish()
        startActivity(intent)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View?) {
        when (p0?.id) {


            R.id.img_brown -> {
                UserShardPrefrences.setCurrentTheme(this,R.style.AppTheme_brown)
                //  applyTheme()

                restartActivity()
            }

            R.id.img_green -> {
                UserShardPrefrences.setCurrentTheme(this,R.style.AppTheme_green)
                //applyTheme()

                restartActivity()
            }
            R.id.img_skyblue -> {
                UserShardPrefrences.setCurrentTheme(this,R.style.AppTheme_skyblue)
                //applyTheme()

                restartActivity()
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


                /*
                                if(isTouchId){
                                    fingerprintClicked()
                                }else{
                                    loginClicked()
                                }
                */

                loginClicked()
            }


            R.id.fabpluse -> {

                fabclicked()
            }

            R.id.fab_restart ->{

                UserShardPrefrences.setuserLogin(this, false)
                UserShardPrefrences.clearUserInfo(this)
                restartClicked()
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
    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun fingerprintClicked() {



        if (isBiometricSensorAvailable(this)) {
            // Biometric sensor is available on this device
            //    Toast.makeText(this, "Biometric sensor is available", Toast.LENGTH_SHORT).show()
            showFingerPrintDialoge()

        } else {
            // Biometric sensor is not available on this device
            Toast.makeText(this, resources.getString(R.string.biometric_not_available), Toast.LENGTH_SHORT).show()
        }


        /* if (UserShardPrefrences.isBiometricRegistered(this@LoginActivity)) {
             showFingerPrintDialoge()
         } else {
             val alertDialog = AlertDialog.Builder(this)
             val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
             var alertDialogView: View? = null

             inflater?.let {
                 alertDialogView = it.inflate(R.layout.biometric, null)
                 alertDialog.setView(alertDialogView)

                 val diag_ok = alertDialogView?.findViewById<Button>(R.id.diag_ok)
                 val diag_cancel = alertDialogView?.findViewById<Button>(R.id.diag_cancel)

                 val dialog = alertDialog.show()

                 dialog.window?.let { window ->
                     window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                     // TODO Dialog Size
                     window.setLayout(((getWidth(this) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT)
                     window.setGravity(Gravity.CENTER)
                 }

                 diag_ok?.setOnClickListener {
                     diag_cancel?.performClick()

                     val biometricManager = getSystemService(BIOMETRIC_SERVICE) as BiometricManager
                     when (biometricManager.canAuthenticate()) {
                         BiometricManager.BIOMETRIC_SUCCESS -> {
                             val promptInfo = BiometricPrompt.PromptInfo.Builder()
                                 .setTitle("Fingerprint Authentication")
                                 .setSubtitle("Place your finger on the sensor to authenticate")
                                 .setNegativeButtonText("Cancel")
                                 .build()

                             val executor = ContextCompat.getMainExecutor(this)

                             val biometricPrompt = BiometricPrompt(this@LoginActivity, executor,
                                 object : BiometricPrompt.AuthenticationCallback() {
                                     override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                         super.onAuthenticationSucceeded(result)
                                         // Handle authentication success

                                         callLoginAPI(UserShardPrefrences.getUserName(this@LoginActivity).toString(),
                                             UserShardPrefrences.getPassword(this@LoginActivity).toString()
                                         )
                                         UserShardPrefrences.setBiometricRegistered(this@LoginActivity,true)


                                     }

                                     override fun onAuthenticationFailed() {
                                         super.onAuthenticationFailed()
                                         // Handle authentication failure
                                         UserShardPrefrences.setBiometricRegistered(this@LoginActivity,false)

                                     }
                                 })

                             biometricPrompt.authenticate(promptInfo)
                         }
                         BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                             Toast.makeText(this, "No biometric hardware available", Toast.LENGTH_LONG).show()
                         }
                         BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                             Toast.makeText(this, "Biometric hardware is unavailable", Toast.LENGTH_LONG).show()
                         }
                         BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                             Toast.makeText(this, "No biometric enrolled, please register in Settings", Toast.LENGTH_LONG).show()
                         }
                     }
                 }

                 diag_cancel?.setOnClickListener {
                     dialog.dismiss()
                 }
             }
         }
 */
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showFingerPrintDialoge() {



        val biometricManager = getSystemService(BIOMETRIC_SERVICE) as BiometricManager
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(resources.getString(R.string.fingerprint_authentication))
                    .setSubtitle(resources.getString(R.string.finger_on_sensor))
                    .setNegativeButtonText(resources.getString(R.string.cancel_txt))
                    .build()

                val executor = ContextCompat.getMainExecutor(this)

                val biometricPrompt = BiometricPrompt(this@LoginActivity, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            // Handle authentication success
                            callLoginAPI(
                                UserShardPrefrences.getUsername_new(this@LoginActivity).toString(),
                                UserShardPrefrences.getPassword_new(this@LoginActivity).toString()
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
        if (UserShardPrefrences.getLanguage(this@LoginActivity).equals("0")) {
            //  binding.fabLanguage.setImageResource(R.drawable.ar_icon)
            UserShardPrefrences.setLanguage(this@LoginActivity, "1")
            LocaleHelper.setLocale(this@LoginActivity, "ar")

        } else {
            // binding.fabLanguage.setImageResource(R.drawable.en_icon)
            UserShardPrefrences.setLanguage(this@LoginActivity, "0")
            LocaleHelper.setLocale(this@LoginActivity, "en")
        }

        restartFirstActivity()
    }

    private fun restartClicked() {
        val i = this!!.packageManager
            .getLaunchIntentForPackage(this!!.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }
    private fun restartFirstActivity() {
        val i = applicationContext.packageManager
            .getLaunchIntentForPackage(applicationContext.packageName)
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
                    UserShardPrefrences.setSaveCradentials(this, true)

                else{
                    UserShardPrefrences.setSaveCradentials(this, false)
                }
            }
        }

    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v!!.id) {
            R.id.et_username -> {

                if (UserShardPrefrences.getFingerSwitch(this)) {
                    if (hasFocus) {
                        binding.txtLogin.text = this@LoginActivity.resources.getString(R.string.action_sign_in)
                        binding.imgTouchid!!.visibility = View.GONE
                        isTouchId = false
                    } else {
                        binding.txtLogin.text = this@LoginActivity.resources.getString(R.string.touch_id_txt)
                        binding.imgTouchid!!.visibility = View.VISIBLE
                        isTouchId = true
                    }

                } else {


                    binding.txtLogin.text = this@LoginActivity.resources.getString(R.string.action_sign_in)
                    binding.imgTouchid!!.visibility = View.GONE
                    isTouchId = false


                }
            }

            R.id.et_password -> {
                if (UserShardPrefrences.getFingerSwitch(this)) {
                    if (hasFocus) {
                        binding.txtLogin.text = this@LoginActivity.resources.getString(R.string.action_sign_in)
                        binding.imgTouchid!!.visibility = View.GONE
                        isTouchId = false
                    } else {
                        binding.txtLogin.text = this@LoginActivity.resources.getString(R.string.touch_id_txt)
                        binding.imgTouchid!!.visibility = View.VISIBLE
                        isTouchId = true
                    }

                } else {


                    binding.txtLogin.text = this@LoginActivity.resources.getString(R.string.action_sign_in)
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

}

