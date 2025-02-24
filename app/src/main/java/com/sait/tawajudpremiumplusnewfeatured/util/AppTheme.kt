package com.sait.tawajudpremiumplusnewfeatured.util

import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.UserShardPrefrences

fun AppCompatActivity.applyTheme() {

    when (UserShardPrefrences.getCurrentTheme(this)) {
        R.style.AppTheme_green -> {
            this.setTheme(R.style.AppTheme_green)
        }
        R.style.AppTheme_skyblue -> {
            this.setTheme(R.style.AppTheme_skyblue)
        }
        R.style.AppTheme_brown -> this.setTheme(R.style.AppTheme_brown)
        else -> this.setTheme(R.style.AppTheme_skyblue)
    }

    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
}