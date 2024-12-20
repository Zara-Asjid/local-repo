package com.sait.tawajudpremiumplusnewfeatured

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.PathInterpolator
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.sait.tawajudpremiumplusnewfeatured.core.BaseFragment
import com.sait.tawajudpremiumplusnewfeatured.databinding.FragmentSettingsBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.ChangePasswordFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.ui.complaints.ComplaintsFragment
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

class SettingsFragmentTesting : BaseFragment(),OnClickListener, CompoundButton.OnCheckedChangeListener {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var mContext: Context? = null

    private var mLanguageCode = "ar"
    private var b_FromSettings: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        mContext = inflater.context
        val activity = this.activity as MainActivity?

        if(UserShardPrefrences.getLanguage(mContext).equals("0")){
            binding.txtVersion.text = getAppVersionName()
        }
        else{

            binding.txtVersion.text = getAppVersionName()?.let {
                LocaleHelper.convertToArabicNumerals(
                    it
                )
            }

        }

        if(UserShardPrefrences.getLanguage(mContext).equals("1")){
            binding.txtArabic.setBackgroundResource(R.drawable.green_rounded_rectangle)
            binding.txtEnglish.setBackgroundResource(R.drawable.lightgreen_rounded_rectangle)
            binding.txtArabic.setTextColor(mContext!!.resources.getColor(R.color.white))
            binding.txtEnglish.setTextColor(mContext!!.resources.getColor(R.color.color_skyblue))

            binding.imgArrow.rotation = 180f
            (activity as MainActivity).binding.layout.imgBack.rotation = 180f
        }
        setClickListeners(activity)
        binding.fingerLoginSwitch.isChecked = UserShardPrefrences.getFingerSwitch(mContext)

        setThemeBackground()
        return binding.root
    }

    fun getAppVersionName(): String? {
        try {
            val packageManager = mContext!!.packageManager
            val packageName = mContext!!.packageName
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }
    private fun setThemeBackground() {
        val themeName = resources.getResourceEntryName(UserShardPrefrences.getCurrentTheme(mContext))
        Log.e("themeName",themeName)
        if(themeName.contains("skyblue")){
            binding.imgBrown.setBackgroundResource(R.drawable.brown_circle)
            binding.imgSkyblue.setBackgroundResource(R.drawable.skyblue_circle_boarder)
            binding.imgGreen.setBackgroundResource(R.drawable.green_circle)
            binding.imgTheme.setBackgroundResource(R.drawable.app_theme_b)
            binding.imgPolicy.setBackgroundResource(R.drawable.policy_b)

        }
        else  if(themeName.contains("brown")){
            binding.imgBrown.setBackgroundResource(R.drawable.brown_circle_boarder)
            binding.imgSkyblue.setBackgroundResource(R.drawable.skyblue_circle)
            binding.imgGreen.setBackgroundResource(R.drawable.green_circle)
            binding.imgTheme.setBackgroundResource(R.drawable.app_theme_br)
            binding.imgPolicy.setBackgroundResource(R.drawable.policy_br)

        }
        else{
            binding.imgBrown.setBackgroundResource(R.drawable.brown_circle)
            binding.imgSkyblue.setBackgroundResource(R.drawable.skyblue_circle)
            binding.imgGreen.setBackgroundResource(R.drawable.green_circle_boarder)
            binding.imgTheme.setBackgroundResource(R.drawable.app_theme)

            binding.imgPolicy.setBackgroundResource(R.drawable.policy)

        }
    }


    private fun setClickListeners(activity: MainActivity?) {
        binding.txtArabic.setOnClickListener(this)
        binding.txtEnglish.setOnClickListener(this)
        binding.llChangePassword.setOnClickListener(this)
        activity?.binding!!.layout.imgBack.setOnClickListener(this)
        binding.fingerLoginSwitch.setOnCheckedChangeListener(this)
        binding.llComplaintSuggestion.setOnClickListener(this)
        binding.llRateapp.setOnClickListener(this)

        binding.llPrivacypolicyapp.setOnClickListener(this)


        binding.imgGreen!!.setOnClickListener(this)
        binding.imgSkyblue!!.setOnClickListener(this)


        binding.imgBrown!!.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()


        (activity as MainActivity).show_BackButton()
        (activity as MainActivity).hide_alert()
        (activity as MainActivity).hide_info()
        (activity as MainActivity).hide_userprofile()
        (activity as MainActivity).show_app_title(requireActivity().resources.getString(R.string.title_settings))
        (activity as MainActivity).hideProgressBar()
        (activity as MainActivity).showToolbar()
        (activity as MainActivity).showBottomNavigationView()

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.ll_rateapp ->{
                navigateToPlayStoreForRating(mContext!!)
            }
            R.id.ll_privacypolicyapp ->{
                replaceFragment(PrivacyPolicyFragment(), R.id.flFragment, true)
            }


            R.id.img_brown -> {
                applyThemeChangeAnimation(v)
                UserShardPrefrences.setCurrentTheme(context,R.style.AppTheme_brown)
//                applyTheme()
               restartActivity()
//                restartActivityWithAnimation()

            }

            R.id.img_green -> {
                applyThemeChangeAnimation(v)
                UserShardPrefrences.setCurrentTheme(context,R.style.AppTheme_green)
                //applyTheme()
                restartActivity()
//                restartActivityWithAnimation()

            }
            R.id.img_skyblue -> {
                applyThemeChangeAnimation(v)
                UserShardPrefrences.setCurrentTheme(context,R.style.AppTheme_skyblue)
                //applyTheme()
                restartActivity()
//                restartActivityWithAnimation()

            }


            R.id.ll_complaint_suggestion ->   replaceFragment(ComplaintsFragment(), R.id.flFragment, true)
            R.id.ll_change_password -> navigateToChangePassword()
            R.id.img_back -> (activity as MainActivity).onBackPressed()
            R.id.txt_english -> {

                if(UserShardPrefrences.getLanguage(mContext).equals("1")) {


                    mLanguageCode = "en"
                    activity?.let { LocaleHelper.setLocale(it, mLanguageCode) };
                    UserShardPrefrences.setLanguage(context, "0")
                    setEnglishBgClick()
                    restartActivity()
                }

            }

            R.id.txt_arabic -> {

                if(UserShardPrefrences.getLanguage(mContext).equals("0")) {
                    mLanguageCode = "ar"
                    activity?.let { LocaleHelper.setLocale(it, mLanguageCode) };
                    UserShardPrefrences.setLanguage(context, "1")
                    setArabicBgClick()
                    restartActivity()

                }

            }
        }
    }
//   private fun applyThemeChangeAnimation(v: View?) {
//
//       v?.animate()
//           ?.alpha(0.5f)  // Fade out slightly
//           ?.scaleX(0.92f)  // Shrink effect (increased for more noticeable effect)
//           ?.scaleY(0.92f)
//           ?.setDuration(800)  // Extended duration for smoothness (was 500ms)
//           ?.setInterpolator(DecelerateInterpolator())  // Smooth out effect
//           ?.withEndAction {
//               v?.animate()
//                   ?.alpha(1f)  // Fade in
//                   ?.scaleX(1f)  // Restore size
//                   ?.scaleY(1f)
//                   ?.setDuration(900)  // Extended duration for gradual return (was 600ms)
//                   ?.setInterpolator(LinearOutSlowInInterpolator())  // More natural easing
//                   ?.start()
//           }
//           ?.start()
//   }

           private fun applyThemeChangeAnimation(view: View) {
               view.postDelayed({
                   // Fade out effect (absorbing feel)
                   val fadeOut = ObjectAnimator.ofFloat(view.rootView, "alpha", 2f, 2.5f).apply {
                       duration = 300
                       interpolator = AccelerateInterpolator()
                   }

                   // Subtle scale-down effect
                   val scaleX = ObjectAnimator.ofFloat(view.rootView, "scaleX", 2f, 2.98f).apply {
                       duration = 300
                       interpolator = AccelerateInterpolator()
                   }

                   val scaleY = ObjectAnimator.ofFloat(view.rootView, "scaleY", 2f, 2.98f).apply {
                       duration = 300
                       interpolator = AccelerateInterpolator()
                   }

                   // Fade back in (emerging effect after theme switch)
                   val fadeIn = ObjectAnimator.ofFloat(view.rootView, "alpha", 2.5f, 2f).apply {
                       duration = 300
                       interpolator = DecelerateInterpolator()
                       startDelay = 300  // Starts after the fade-out completes
                   }

                   AnimatorSet().apply {
                       playTogether(fadeOut, scaleX, scaleY)
                       play(fadeIn).after(fadeOut)
                       start()
                   }
               }, 300)
           }



    fun navigateToPlayStoreForRating(context: Context) {
        val packageName = context.packageName
        try {
            // You can also use "market://details?id=" for Google Play app only
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: android.content.ActivityNotFoundException) {
            // If Play Store app is not available, open the link in a browser
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }



    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length >= 3
    }
    private fun navigateToChangePassword() {
        replaceFragment(ChangePasswordFragment(), R.id.flFragment, true)

    }

    private fun setArabicBgClick() {



        binding.txtArabic.setBackgroundResource(R.drawable.green_rounded_rectangle)
        binding.txtArabic.setTextColor(resources.getColor(R.color.white))

        binding.txtEnglish.setBackgroundResource(R.drawable.lightgreen_rounded_rectangle)
        binding.txtEnglish.setTextColor(colorPrimary)
    }

    private fun setEnglishBgClick() {
        binding.txtEnglish.setBackgroundResource(R.drawable.green_rounded_rectangle)
        binding.txtEnglish.setTextColor(resources.getColor(R.color.white))

        binding.txtArabic.setBackgroundResource(R.drawable.lightgreen_rounded_rectangle)
        binding.txtArabic.setTextColor(colorPrimary)
    }

    private fun restartActivity() {

        // Create an intent to restart MainActivity with the new theme applied
        val loginIntent = Intent(mContext, MainActivity::class.java)
        loginIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION  // Prevent activity transition animation
        loginIntent.putExtra("b_FromSettings", true)  // Flag to indicate the navigation is from the settings

        // Start the MainActivity
        mContext!!.startActivity(loginIntent)

         }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {


        when(p0!!.id){

            R.id.finger_login_switch -> {
                if(p0.isChecked) {

                    UserShardPrefrences.setFingerSwitch(mContext, true)
                    //   fingerprintClicked()

                }
                else{
                    UserShardPrefrences.setFingerSwitch(mContext, false)
                }
            }
        }
    }
}