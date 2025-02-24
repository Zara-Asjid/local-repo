package com.example.mvvm_application.util.extension

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment


fun Context.applyColor(@ColorRes resId: Int): Int {
    return ResourcesCompat.getColor(resources, resId, null)
}

fun Fragment.applyColor(@ColorRes resId: Int): Int {
    return ResourcesCompat.getColor(resources, resId, null)
}

fun Context.applyDrawable(@DrawableRes resId: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, resId, null)
}

fun Fragment.applyDrawable(@DrawableRes resId: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, resId, null)
}

fun Fragment.toast(text: String) {
    Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun Activity.hideProgressBar(progressBar: ProgressBar?) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    progressBar?.hide()
}

fun Fragment.hideProgressBar(progressBar: ProgressBar?) {
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    progressBar?.hide()
}

fun Activity.hideKeyboard(view: View? = currentFocus) {
    if (currentFocus != null) {
        val manager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}

fun Activity.hideKeyBoard() {
    val mInputMethodManager: InputMethodManager =
        getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view: View? = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun Fragment.hideKeyboard(view: View? = activity?.currentFocus) {
    if (activity?.currentFocus != null) {
        val manager: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}

fun ScrollView.scrollToBottom() {
    val lastChild = getChildAt(childCount - 1)
    val bottom = lastChild.bottom + paddingBottom
    val delta = bottom - (scrollY + height)
    smoothScrollBy(0, delta)
}
