package com.sait.tawajudpremiumplusnewfeatured.ui.register

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.core.BaseActivity
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.databinding.ActivityRegistrationBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.register.viewmodels.EmailValidity.EmailValidityViewModel
import com.sait.tawajudpremiumplusnewfeatured.ui.register.viewmodels.RegisterViewModel
import com.sait.tawajudpremiumplusnewfeatured.util.ColorUtils
import com.sait.tawajudpremiumplusnewfeatured.util.Cryptography_Android
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.Keyboard_Op
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.applyTheme
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.getEncriptedKey
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.getVector
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class RegisterActivity : BaseActivity(),View.OnClickListener {

    private var _binding: ActivityRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel
    private lateinit var viewModel_emailValidity: EmailValidityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        applyTheme()

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        colorPrimary =  Color.parseColor(
            ColorUtils.toHexColor(
                ColorUtils.resolveColorAttribute(
                    this,
                    R.attr.themecolor
                )
            )
        )

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        viewModel_emailValidity = ViewModelProvider(this).get(EmailValidityViewModel::class.java)

        _binding = ActivityRegistrationBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(_binding?.root)
        if (UserShardPrefrences.isFirstRun(this@RegisterActivity)) {



            SnackBarUtil.showSnackbar_Drawable_backroundcolor(this,
                resources.getString(R.string.location_access_description),
                R.drawable.app_icon,
                colorPrimary,
                SnackBarUtil.OnClickListenerNew {

                    UserShardPrefrences.updateFirstRun(this@RegisterActivity)

                })

        }

        setClickListeners()



if(UserShardPrefrences.getLanguage(RegisterActivity@this)!!.equals("0")){
    binding.txtEng.setBackgroundResource(R.drawable.whitecircle_greenborder)
    binding.txtArabic.setBackgroundResource(R.drawable.green_rounded_rectangle)
}


        else{
    binding.txtEng.setBackgroundResource(R.drawable.green_rounded_rectangle)
    binding.txtArabic.setBackgroundResource(R.drawable.whitecircle_greenborder)
        }


    }






    override fun onPause() {
        super.onPause()


    }


    private fun setClickListeners() {
        binding.txtRegister.setOnClickListener(this)
binding.txtEng.setOnClickListener(this)
        binding.txtArabic.setOnClickListener(this)


    }




    private fun navigateToLoginActivity() {

        //  UserShardPrefrences.updateFirstRun(this@RegisterActivity)

        UserShardPrefrences.setisRestart(this@RegisterActivity,false)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun addObserver() {
        viewModel.registerResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.progressOverlay.visibility = View.GONE

                        response.data?.let { registerResponse ->
                            val registerData = registerResponse
                            if (registerData != null) {

                                if (registerData != null && registerData.Success_Code.equals(1)) {
                                    Log.e("Register:", registerData.toString())

                                    Log.e("DecryptResponse:", Cryptography_Android.decryptTo
                                        (registerResponse.URL, getEncriptedKey()!!, getVector())

                                    )

                                    UserShardPrefrences.setBaseUrl(
                                        this,
                                        Cryptography_Android.decryptTo
                                            ( registerData.URL, getEncriptedKey()!!, getVector(),)
                                    )
                                    UserShardPrefrences.setPackage(this,
                                        Cryptography_Android.decryptTo
                                            ( registerData.PackageName, getEncriptedKey()!!, getVector()))
                                    UserShardPrefrences.setReleaseNo(this,
                                        Cryptography_Android.decryptTo
                                            (registerData.VersionName, getEncriptedKey()!!, getVector()))
                                    UserShardPrefrences.setLicenseStartDate(
                                        this,
                                        Cryptography_Android.decryptTo( registerData.StartDate, getEncriptedKey()!!, getVector())
                                    )
                                    UserShardPrefrences.setLicenseEndDate(this,
                                        Cryptography_Android.decryptTo(
                                            registerData.SupportEndDate, getEncriptedKey()!!, getVector()))
                                    UserShardPrefrences.setEmail(this,
                                        Cryptography_Android.decryptTo
                                            (registerData.ClientEmail, getEncriptedKey()!!, getVector()))
                                    UserShardPrefrences.setNoUsers(this,
                                        Cryptography_Android.decryptTo
                                            (  registerData.NoOfUsers, getEncriptedKey()!!, getVector()))
                                    UserShardPrefrences.setDomain(this, binding.edtEmail.text.toString().substringAfter("@"))
                                    UserShardPrefrences.setMobile(this,
                                        Cryptography_Android.decryptTo
                                            (  registerData.MobileNo, getEncriptedKey()!!, getVector()))
                                    UserShardPrefrences.setName(this,
                                        Cryptography_Android.decryptTo
                                            (registerData.ClientName, getEncriptedKey()!!, getVector()))
                                    UserShardPrefrences.setShortName(this,
                                        Cryptography_Android.decryptTo
                                            (registerData.ClientShortName, getEncriptedKey()!!, getVector()))

                                        callEmailValidityApi()


                                } else {

                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(this, resources.getString(R.string.not_registered), R.drawable.caution,resources.getColor(R.color.red))
                                }
                            }
                        }
                    }

                    is Resource.Error -> {
                        binding.progressOverlay.visibility = View.GONE
                        response.message?.let { message ->
                              //toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(this,message,R.drawable.caution,resources.getColor(R.color.red))

                           // SnackBarUtil.showSnackbar(this, message, false)
                          /*  UserShardPrefrences.setBaseUrl(
                                this,
                               " http://38.242.232.82/TawajudAPIs/api/"
                            )*/
                        }

                      //  navigateToLoginActivity()

                    }

                    is Resource.Loading -> {
                        binding.progressOverlay.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    private fun getRegisterDetails() {
       viewModel.getRegisterData(binding.edtEmail.text.toString(),"Tawajud",1016)
    }


    private fun restartActivity() {
        val intent = intent
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        finish()
        startActivity(intent)
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.txt_register -> {


                binding.edtEmail.error = null
                val email: String = binding.edtEmail.getText().toString()
                var focusView: View? = null
                var cancel = false

                if (TextUtils.isEmpty(email)) {
                    binding.edtEmail.setError(getString(R.string.error_field_required))
                    focusView = binding.edtEmail
                    cancel = true
                }else
                    if (!isValidEmail(email)) {
                        binding.edtEmail.setError(getString(R.string.error_invalid_email))
                        focusView = binding.edtEmail
                        cancel = true
                    }

                if (cancel) {
                    focusView!!.requestFocus()
                }

                else{
                    Keyboard_Op.hide(this)
                    callRegisterApi()

                }



//                navigateToLoginActivity()
            }
            R.id.txt_eng->{

                if(UserShardPrefrences.getLanguage(this).equals("1")){
                    binding.txtEng.setBackgroundResource(R.drawable.whitecircle_greenborder)
                    binding.txtArabic.setBackgroundResource(R.drawable.green_rounded_rectangle)

                    UserShardPrefrences.setLanguage(this@RegisterActivity, "0")
                    LocaleHelper.setLocale(this@RegisterActivity, "en")
                    restartActivity()
                }


            }
            R.id.txt_arabic->{


                if(UserShardPrefrences.getLanguage(this).equals("0")){
                    binding.txtEng.setBackgroundResource(R.drawable.green_rounded_rectangle)

                    binding.txtArabic.setBackgroundResource(R.drawable.whitecircle_greenborder)

                    UserShardPrefrences.setLanguage(this@RegisterActivity, "1")
                    LocaleHelper.setLocale(this@RegisterActivity, "ar")
                    restartActivity()
                }

            }
        }
    }

    private fun callEmailValidityApi() {
        addObserver_emailValidity()
        getEmailValidityData()

    }

    private fun addObserver_emailValidity() {
        viewModel_emailValidity.registerResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.progressOverlay.visibility = View.GONE

                        response.data?.let { registerResponse ->
                            val registerData = registerResponse
                            if (registerData != null) {

                                if (registerData != null && registerData.isSuccess) {
                                    if (registerData.data.msg.equals(resources.getString(R.string.email_exist_txt)) ){
                                        navigateToLoginActivity()
                                    }else
                                        if(registerData.data.msg.equals(resources.getString(R.string.email_not_exist_txt))){
                                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(this,registerResponse.data.msg, R.drawable.caution,resources.getColor(R.color.red))

                                        }

                                } else {

                                    SnackBarUtil.showSnackbar_Drawable_backroundcolor(this,registerResponse.data.msg, R.drawable.caution,resources.getColor(R.color.red))
                                }
                            }
                        }
                    }

                    is Resource.Error -> {
                        binding.progressOverlay.visibility = View.GONE
                        response.message?.let { message ->
                            //toast(message)
                            SnackBarUtil.showSnackbar_Drawable_backroundcolor(this,message,R.drawable.caution,resources.getColor(R.color.red))

                            // SnackBarUtil.showSnackbar(this, message, false)
                            /*  UserShardPrefrences.setBaseUrl(
                                  this,
                                 " http://38.242.232.82/TawajudAPIs/api/"
                              )*/
                        }

                        //  navigateToLoginActivity()

                    }

                    is Resource.Loading -> {
                        binding.progressOverlay.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    private fun callRegisterApi() {
        addObserver()
        getRegisterDetails()
    }

    private fun getEmailValidityData() {
        viewModel_emailValidity.getRegisterData(binding.edtEmail.text.toString(),this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = android.graphics.Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    // view.clearFocus()
                    hideKeyboard(this)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}