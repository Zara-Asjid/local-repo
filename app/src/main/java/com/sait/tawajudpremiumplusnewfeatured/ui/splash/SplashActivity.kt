package com.sait.tawajudpremiumplusnewfeatured.ui.splash

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import com.sait.tawajudpremiumplusnewfeatured.MainActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.databinding.ActivitySplashBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.landingScreens.LandingActivity
import com.sait.tawajudpremiumplusnewfeatured.ui.register.RegisterActivity
import com.sait.tawajudpremiumplusnewfeatured.util.ColorUtils
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.sait.tawajudpremiumplusnewfeatured.util.applyTheme
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.getStableDeviceId
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.isDeviceRooted
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.security.MessageDigest
import kotlin.system.exitProcess

class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!
    var slideup: Animation? = null;
    var slide_down:Animation? = null
    var slidein_left: Animation? = null;
    var slidein_right:Animation? = null;
    var blink:Animation?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        // Initialize shared preferences
        UserShardPrefrences.initialize(this)
        applyTheme()
        WorkManager.getInstance(this)
            .cancelAllWork()
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(_binding?.root)
        colorPrimary =  Color.parseColor(
            ColorUtils.toHexColor(
                ColorUtils.resolveColorAttribute(
                    this,
                    R.attr.themecolor
                )
            )
        )
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }

        slideup = AnimationUtils.loadAnimation(this, R.anim.centertotop)
        slide_down = AnimationUtils.loadAnimation(this, R.anim.centertobottom)

        blink = AnimationUtils.loadAnimation(this,R.anim.blink_infinite)
        slidein_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_new)
        slidein_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_new)


        binding.logo.animation = slideup

        binding.imgPermisssion.animation = slideup

        binding.imgRolebased.animation = slidein_right

        binding.imgReports.animation = slidein_left



        binding.imgCalendar.animation = slidein_left

        binding.imgAttendance.animation = slidein_right
        binding.lBottom.animation = slideup

        binding.txtGo.animation = blink
        if (isDeviceRooted()) {
            Log.e("RootCheck", "Device is rooted!")
            SnackBarUtil.showSnackbar_Drawable_backroundcolor(this,
                "Device is rooted! The app will now exit.",
                R.drawable.app_icon,
                resources.getColor(
                    R.color.red
                ),
                SnackBarUtil.OnClickListenerNew {
                    this.finishAffinity()          // Closes the app
                    exitProcess(0)          // Force exits the JVM (optional)

                })
            // Skip proceeding to the main part of the app since the device is rooted
            return
        } else {
            Log.i("RootCheck", "Device is not rooted.")
        }
        Log.d("UniqueId(sharedPref)", "" + UserShardPrefrences.getUniqueId(this))
        //      if (!UserShardPrefrences.getUniqueIdOnce(this)) {
        //      if (!UserShardPrefrences.getUniqueIdOnce(this)) {
        val s: String = generateDeviceIdentifier(this)!!
        if (s == "") {
            Log.i("UniqueId", "Empty")
        } else {
            val s1 = s.substring(0, 8)
            val s2 = s.substring(8, 12)
            val s3 = s.substring(12, 16)
            val s4 = s.substring(16, 20)
            val s5 = s.substring(20, 24)
            val s6 = s.substring(24, 32)
            val unique_ID = "$s1-$s2-$s3-$s4-$s5$s6"
            UserShardPrefrences.setUniqueId(this, unique_ID)
           //UserShardPrefrences.setUniqueId(this, "374DA933-CD79-434D-B559-5DA2CE62EF8E")

            UserShardPrefrences.setUniqueIdOnce(this, true)

           Log.i("UniqueId",UserShardPrefrences.getOrCreateKeystoreUUID(this))
           // Log.i("UniqueId",getStableDeviceId(this))
            //        }
        }
        val packageName = "com.sait.tawajudpremiumplusnewfeatured" // Replace with your package name
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val applicationInfo = packageInfo.applicationInfo
            val uid = applicationInfo!!.uid
            Log.d("UID", "The UID for package $packageName is $uid")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val handler = Handler()

        handler.postDelayed({


       /*     if (UserShardPrefrences.isFirstRun(this@LandingScreens_activity)) {
//            startActivity(new Intent(LandingScreens.this, TabActivity.class));
//            finish();
                UserShardPrefrences.updateFirstRun(this@LandingScreens_activity)
            } else {
                startActivity(Intent(this@LandingScreens_activity, SplashActivity::class.java))
                finish()
            }*/



            if (UserShardPrefrences.isFirstRun(this@SplashActivity)) {
                //UserShardPrefrences.updateFirstRun(this@SplashActivity)
                startActivity(Intent(this@SplashActivity, LandingActivity::class.java))
            } else {


                if(intent.extras!=null) {
                    val b_FromReStart = intent.extras!!.getBoolean("b_FromReStart")
                   // val b_FromSettings = intent.extras!!.getBoolean("b_FromSettings")
                    if(b_FromReStart){
                        startActivity(Intent(this@SplashActivity, RegisterActivity::class.java))

                    }


                   else if( UserShardPrefrences.getisRestart(this)){
                        startActivity(Intent(this@SplashActivity, RegisterActivity::class.java))

                    }
                    else{
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))

                    }
                }
                else{


                    if( UserShardPrefrences.getisRestart(this)){
                        startActivity(Intent(this@SplashActivity, RegisterActivity::class.java))

                    }
                    else{
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    }

                }

            }
            finish()
        }, 4000)
    }
}



fun generateDeviceIdentifier(context: Context): String? {
    val pseudoId =
        "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    val longId = pseudoId + androidId
    return try {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(longId.toByteArray(), 0, longId.length)

        // get md5 bytes
        val md5Bytes = messageDigest.digest()

        // creating a hex string
        var identifier: String? = ""
        for (md5Byte in md5Bytes) {
            val b = 0xFF and md5Byte.toInt()

            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF) {
                identifier += "0"
            }

            // add number to string
            identifier += Integer.toHexString(b)
        }

        // hex string to uppercase
        //            identifier = identifier.toUpperCase();
        identifier
    } catch (e: Exception) {
        //            return UUID.randomUUID().toString();
        ""
    }
}
















