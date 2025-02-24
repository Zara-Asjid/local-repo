package com.sait.tawajudpremiumplusnewfeatured.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class CustomSwipeRefreshLayout(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {

    private var startX = 0f
    private var startY = 0f

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - startX
                val deltaY = event.y - startY

                // Check if the horizontal swipe is more significant than vertical
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    // Disable refreshing for horizontal swipe
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }
}
