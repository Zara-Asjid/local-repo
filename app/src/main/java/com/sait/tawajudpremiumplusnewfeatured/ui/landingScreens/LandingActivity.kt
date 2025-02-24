package com.sait.tawajudpremiumplusnewfeatured.ui.landingScreens
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.databinding.ActivityLandingScreensBinding
import com.sait.tawajudpremiumplusnewfeatured.ui.register.RegisterActivity
import com.sait.tawajudpremiumplusnewfeatured.util.applyTheme
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences
import java.security.MessageDigest

class LandingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLandingScreensBinding
    private lateinit var fragments: List<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyTheme()
        binding = ActivityLandingScreensBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
       /* if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, colorPrimary)
        }*/

        setClickListeners()
        setupViewPager()
        setupCarousel()

        if (!UserShardPrefrences.getUniqueIdOnce(this)) {
            val s: String = generateDeviceIdentifier(this).toString()

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
                UserShardPrefrences.setUniqueIdOnce(this, true)
            }
        }

    }
    private fun generateDeviceIdentifier(context: Context): String? {
        val pseudoId =
            "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
        val androidId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
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

    private fun setClickListeners() {
        binding.txtSkip.setOnClickListener(this)
    }

    private fun setupViewPager() {
        fragments = listOf(
            LandingLocationFragment(),
            LandingReportsFragment(),
            LandingCalenderFragment(),
            LandingAttendanceFragment()
        )

        val adapter = ViewPagerFragmentAdapter(this, fragments)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = true // Disable swipe

      //  val dotsIndicator = findViewById<WormDotsIndicator>(R.id.dotsIndicator)
       // dotsIndicator.setViewPager2(binding.viewPager)
    }

    private fun setupCarousel(){

        binding.viewPager.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(com.intuit.sdp.R.dimen._26sdp)
        val currentItemHorizontalMarginPx = resources.getDimension(com.intuit.sdp.R.dimen._82sdp)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
            page.alpha = 0.25f + (1 - kotlin.math.abs(position))
        }

        val itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.viewPager.setPageTransformer(pageTransformer)
        binding.viewPager.addItemDecoration(itemDecoration)


    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.txt_skip -> {
                startActivity(Intent(this@LandingActivity, RegisterActivity::class.java))
                finish()
            }
        }
    }

}

