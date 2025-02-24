package com.sait.tawajudpremiumplusnewfeatured.core

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.util.Const
import com.sait.tawajudpremiumplusnewfeatured.ui.changelanguage.LocaleHelper
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.scheduleAppTerminateJob


open class BaseActivity : AppCompatActivity() {

    internal var lang: String = ""

    override fun attachBaseContext(base: Context) {
        lang = PrefUtils.getStringWithContext(base, Const.SharedPrefs.SELECTED_LANGUAGE).toString()
        if (lang == "")
            lang = Const.Other.ENGLISH_LANG_CODE
        super.attachBaseContext(LocaleHelper.onAttach(base, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * to add fragment in container
     * tag will be same as class name of fragment
     *
     * @param containerId    id of fragment container
     * @param addToBackStack should be added to back stack?
     */
    open fun addFragment(fragment: Fragment, containerId: Int, addToBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(containerId, fragment)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }
        fragmentTransaction.commit()
    }

    /**
     * to replace fragment in container
     * tag will be same as class name of fragment
     *
     * @param containerId        id of fragment container
     * @param isAddedToBackStack should be added to back stack?
     */
    open fun replaceFragment(fragment: Fragment, containerId: Int, isAddedToBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_to_left, R.anim.slide_in_from_left,
            R.anim.slide_out_to_right);
        fragmentTransaction.replace(containerId, fragment, fragment.javaClass.name)
        if (isAddedToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }
        fragmentTransaction.commit()
    }
   /* companion object {
        var wasInBackground = false
    }

    override fun onPause() {
        super.onPause()
        wasInBackground = true
    }*/

}
