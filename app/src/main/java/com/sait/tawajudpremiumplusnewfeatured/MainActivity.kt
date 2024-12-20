package com.sait.tawajudpremiumplusnewfeatured


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.work.WorkManager
import com.example.mvvm_application.util.extension.hide
import com.example.mvvm_application.util.extension.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.play.core.appupdate.AppUpdateManager
import com.sait.tawajudpremiumplusnewfeatured.core.BaseActivity
import com.sait.tawajudpremiumplusnewfeatured.databinding.ActivityMainBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.contact.ContactFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.login.LoginFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.PdfGenaratorFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.reports.ReportsFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.RequestsFragment
import com.sait.tawajudpremiumplusnewfeatured.ui.splash.SplashActivity
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.ViolationsPendingFragment
import com.sait.tawajudpremiumplusnewfeatured.util.ColorUtils
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.colorPrimary
import com.sait.tawajudpremiumplusnewfeatured.util.applyTheme
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.sait.tawajudpremiumplusnewfeatured.ui.home.HomeFragmentNew
import com.sait.tawajudpremiumplusnewfeatured.util.SnackBarUtil
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.ktx.AppUpdateResult
import com.sait.tawajudpremiumplusnewfeatured.util.GlobalVariables.b_custom_notification
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils

class MainActivity: BaseActivity(),OnClickListener   {

    lateinit var bottomNavigationView : BottomNavigationView
    private var _binding: ActivityMainBinding? = null
    public val binding get() = _binding!!
    lateinit var toolbarLayout : View
    val loginFragment= LoginFragment()
    val homeFragment= HomeFragmentNew()
    val contactsFragment= ContactFragment()
    val helpFragment= HelpFragment()
    val settingsFragmentTesting= SettingsFragmentTesting()
    val settingsFragment= SettingsFragment()
    private var doubleBackToExitPressedOnce = false
    private var backPressedTime: Long = 0


    private lateinit var appUpdateManager: AppUpdateManager
    private val MY_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        applyTheme()
        colorPrimary =  Color.parseColor(
            ColorUtils.toHexColor(
                ColorUtils.resolveColorAttribute(
                    this,

                    R.attr.themecolor
                )
            )
        )

        _binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(_binding?.root)

        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkForAppUpdate()

        /*  if (Build.VERSION.SDK_INT >= 21) {
              window.statusBarColor = ContextCompat.getColor(this,R.color.white);
          }*/
        //  colorPrimary = SnackBarUtil.resolveColorAttribute(this, R.attr.themecolor)
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        toolbarLayout = findViewById(R.id.layout)

        setClickListeners()



        //  setCurrentFragment(loginFragment)

        if(intent.extras != null){
            val b_FromSettings = intent.extras!!.getBoolean("b_FromSettings")
            if(b_FromSettings){
           setCurrentFragment(settingsFragmentTesting)
                  binding.bottomNavigationView.selectedItemId = R.id.navigation_settings;

//                setCurrentFragment(homeFragment)
//                binding.bottomNavigationView.selectedItemId = R.id.navigation_home;
            }

            else  if (intent != null && intent.getStringExtra("navigate_to") == "home_fragment") {

                if (UserShardPrefrences.isUserLogin(this)) {
                    setCurrentFragment(homeFragment)
                }
                else{
                    setCurrentFragment(loginFragment)
                }
            }
        }


        else{
            setCurrentFragment(loginFragment)

        }



        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home->setCurrentFragment(homeFragment)
                R.id.navigation_help->setCurrentFragment(helpFragment)
                R.id.navigation_contact->setCurrentFragment(contactsFragment)
                R.id.navigation_settings->{
                    when( hideProgressBar()){
                        binding.progressOverlay.hide() ->{ setCurrentFragment(settingsFragment) }
                    }
                }

            }
            true
        }

    }



    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            //   hideSystemUI()
        }
    }
    private fun checkForAppUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability()
                == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE))
            {
                // Request the update
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    IMMEDIATE,
                    this,
                    MY_REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MY_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    // Update accepted, app will be updated in the background
                }
                Activity.RESULT_CANCELED -> {
                    // Update canceled by the user

                    SnackBarUtil.showSnackbar(this,resources.getString(R.string.version_alert))

                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    // Update failed, handle the error
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }




    internal fun showProgressBar() {
        binding.progressOverlay.show()
    }

    internal fun hideProgressBar() {
        binding.progressOverlay.hide()
    }




    internal fun showToolbar() {
        toolbarLayout.visibility = View.VISIBLE
    }

    internal fun hideToolbar() {
        toolbarLayout.visibility = View.GONE

    }



    internal fun showBottomNavigationView() {
        binding.bottomNavigationView.show()
    }

    internal fun hideBottomNavigationView() {
        binding.bottomNavigationView.hide()
    }

    fun show_BackButton_title(text: String) {
        binding.layout.lTitleBack.visibility= View.VISIBLE
        binding.layout.txtTitle.visibility= View.VISIBLE
        binding.layout.txtTitle.text = text
    }

    fun show_BackButton() {
        binding.layout.txtTitle.visibility = GONE
        binding.layout.lTitleBack.visibility= View.VISIBLE
    }
     fun hide_BackButton_title() {
        binding.layout.lTitleBack.visibility= View.GONE
    }
     fun show_info() {
        binding.layout.imgInfo.visibility= View.VISIBLE
    }
     fun hide_info() {
        binding.layout.imgInfo.visibility= View.GONE
         binding.layout.txtAnnouncementCount.visibility = View.GONE
    }

     fun show_alert() {
        binding.layout.imgAlert.visibility= View.VISIBLE
    }
     fun hide_alert() {
        binding.layout.imgAlert.visibility= View.GONE
    }

    fun show_userprofile(text: String) {
        binding.layout.txtTitle.text = text
        binding.layout.lUser.visibility= View.VISIBLE
    }
    fun hide_userprofile() {
        binding.layout.lUser.visibility= View.GONE
    }


    fun show_app_title(text: String) {
        binding.layout.txtAppname.visibility= View.VISIBLE
        binding.layout.txtAppname.text = text
    }
    fun hide_app_title() {
        binding.layout.txtAppname.visibility= View.GONE
    }

    fun hide_progressbar() {
        binding.progressOverlay.visibility= View.GONE
    }

    fun show_progressbar() {
        binding.progressOverlay.visibility= View.VISIBLE
    }

    private fun setClickListeners() {
        binding.layout.lUser.setOnClickListener(this)
        binding.layout.imgAlert.setOnClickListener(this)
        binding.layout.imgInfo.setOnClickListener(this)

    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Handle the intent to navigate to Home Fragment
        if (intent != null && intent.getStringExtra("navigate_to") == "home_fragment") {
            setCurrentFragment(homeFragment)
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }






    private fun navigateToUserProfile() {
        replaceFragment(ProfileFragment(), R.id.flFragment, true)
    }


    private fun navigateToUserInfo() {
        replaceFragment(UserInfoFragment(), R.id.flFragment, true)
    }
    override fun onClick(p0: View?) {

        when (p0?.id) {
            R.id.l_user -> navigateToUserProfile()
            R.id.img_info->navigateToUserInfo()
            R.id.img_alert -> navigateToAlert()




        }

    }

    private fun navigateToAlert() {

    }


    override fun onResume() {
        super.onResume()

        hideProgressBar()



        appUpdateManager.appUpdateInfo.addOnSuccessListener {
                info ->

            if(info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){

                appUpdateManager.startUpdateFlowForResult(
                    info,
                    AppUpdateType.IMMEDIATE,
                    this,
                    MY_REQUEST_CODE
                )
            }

        }
    }




    /*
            if (wasInBackground) {
                // The app is returning from the background
                // You can perform actions here
                wasInBackground = false // Reset the flag

                Log.e("Home_background","Home_background_true")

                restartApp()
            }
            else{
                Log.e("Home_background","Home_background_false")
            }
    */


    private fun restartApp() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
    fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.flFragment)

        if(fragment is SettingsFragment ||fragment is ContactFragment || fragment is HelpFragment  ){

            setCurrentFragment(homeFragment)
            binding.bottomNavigationView.selectedItemId = R.id.navigation_home;
            // setCurrentFragment(homeFragment)

            /*  if(doubleBackToExitPressedOnce) {
                  binding.bottomNavigationView.selectedItemId = R.id.navigation_home;
              }*/
            // handleBackPressWithConfirmation()
        }

        else if(fragment is HomeFragmentNew){
            setCurrentFragment(loginFragment)
        }
        else if(fragment is LoginFragment){
            finishAffinity()
        }
        else  if (fragment is RequestsFragment) {
            val selectedTabPosition = fragment.selected_tab?.toInt() ?: 0
            replaceFragment(SelfServiceFragment(), R.id.flFragment, false)
            Handler(Looper.getMainLooper()).post {
                val selfServiceFragment =
                    supportFragmentManager.findFragmentById(R.id.flFragment) as? SelfServiceFragment
                val appliedviolations =
                    supportFragmentManager.findFragmentById(R.id.flFragment) as? ViolationsPendingFragment

                selfServiceFragment?.requireView()?.findViewById<TabLayout>(R.id.tab_layout)
                    ?.apply {
                        getTabAt(selectedTabPosition-1)?.select()
                    }
            }
        } else if (fragment is PdfGenaratorFragment) {
            val selectedTabPosition = fragment.selected_tab?.toInt() ?: 0
            replaceFragment(ReportsFragment(), R.id.flFragment, false)
            Handler(Looper.getMainLooper()).post {
                val reportsFragment =
                    supportFragmentManager.findFragmentById(R.id.flFragment) as? ReportsFragment
                reportsFragment?.requireView()?.findViewById<TabLayout>(R.id.tab_layout)
                    ?.apply {
                        getTabAt(selectedTabPosition)?.select()
                    }
            }
        } else
            if (fragment is SelfServiceFragment){
                val selfServiceFragment =
                    supportFragmentManager.findFragmentById(R.id.flFragment) as? SelfServiceFragment
                selfServiceFragment?.requireView()?.findViewById<TabLayout>(R.id.tab_layout)
                    ?.apply {
                        when(getTabAt(selectedTabPosition)?.position){
                            2->{
                                replaceFragment(SelfServiceFragment(), R.id.flFragment, false)
                            }
                            1->{
                                replaceFragment(SelfServiceFragment(), R.id.flFragment, false)
                            }
                            else ->{
                                supportFragmentManager.popBackStack();
                            }
                        }
                    }
            }
            else {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack();
                } else {
                    if (binding.bottomNavigationView.selectedItemId == R.id.navigation_settings) {
                        binding.bottomNavigationView.selectedItemId = R.id.navigation_home;

                    } else if (binding.bottomNavigationView.selectedItemId == R.id.navigation_contact) {
                        binding.bottomNavigationView.selectedItemId = R.id.navigation_home;

                    } else if (binding.bottomNavigationView.selectedItemId == R.id.navigation_help) {
                        binding.bottomNavigationView.selectedItemId = R.id.navigation_home;

                    } else {
                        super.onBackPressed()

                    }

                }


            }
        /*
                    if (fragment is HomeFragment) {
                        super.onBackPressed()
                    } else {
                        supportFragmentManager.popBackStack()

        */
        /*
                        if (fm.fragments.size > 2) {
                            val temp = fm.fragments[fm.fragments.size - 2]
                            temp.onResume()
                        }
        *//*


            }
*/

    }


    override fun onResumeFragments() {
        super.onResumeFragments()
        UserShardPrefrences.saveCountAnnouncementsUnRead(this,"")
    }

    override fun onDestroy() {
        super.onDestroy()
        UserShardPrefrences.saveCountAnnouncementsUnRead(this,"")

    }

    private fun handleBackPressWithConfirmation() {

        if (doubleBackToExitPressedOnce) {

            val currentTime = System.currentTimeMillis()
            if (currentTime - backPressedTime < 5000) {

                if(supportFragmentManager.backStackEntryCount==1){
                    finishAffinity()
                }
                else{
                    super.onBackPressed()
                }
                return
            }
        }
        doubleBackToExitPressedOnce = true
        backPressedTime = System.currentTimeMillis()

        if(supportFragmentManager.backStackEntryCount==1) {
            Toast.makeText(
                this,
                this.resources.getString(R.string.please_click_again),
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            super.onBackPressed()

        }
        // Reset the flag after 5 seconds
        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        }, 5000)
    }

}


