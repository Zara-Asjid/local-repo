package com.sait.tawajudpremiumplusnewfeatured.util.extension

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import android.os.Handler
import android.os.Looper

object Global {
    fun loadImagesUsingGlide(context: Context, strUrl: String?, imageView: ImageView) {
        Glide.with(context).load(strUrl).into(imageView as ImageView)
    }

}


object GlobalHandler {
    private val handler = Handler(Looper.getMainLooper())

    fun postDelayedTask(task: Runnable, delayMillis: Long) {
        handler.postDelayed(task, delayMillis)
    }

    fun cancelTask(task: Runnable) {
        handler.removeCallbacks(task)
    }
}
