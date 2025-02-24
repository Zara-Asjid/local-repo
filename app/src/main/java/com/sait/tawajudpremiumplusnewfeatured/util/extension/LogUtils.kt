package com.sait.tawajudpremiumplusnewfeatured.util.extension

import android.util.Log
import com.sait.tawajudpremiumplusnewfeatured.BuildConfig

inline fun Any.log(message: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message())
    }
}