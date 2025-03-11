package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.math.abs

class CustomViewPager(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), View.OnTouchListener {

    private var currentPosition = 0
    private var startX = 0f
    private val swipeThreshold = 100f
    private val fragments = mutableListOf<Fragment>()
    private lateinit var fragmentManager: FragmentManager
    private var containerId: Int = 0
    private var fragmentContainer: FrameLayout? = null
    var tabChangeListener: ((Int) -> Unit)? = null

    init {
        // Set the OnTouchListener to handle swipe actions
        setOnTouchListener(this)

        // Initialize the FrameLayout container for fragments
        fragmentContainer = FrameLayout(context)

        // Assign a unique ID to the fragment container for fragment transactions
        fragmentContainer!!.id = View.generateViewId()

        // Add the fragment container to the CustomViewPager layout
        addView(fragmentContainer)
    }

    fun setUp(
        fragmentManager: FragmentManager,
        containerId: Int,
        fragments: List<Fragment>
    ) {
        this.fragmentManager = fragmentManager
        this.containerId = containerId
        this.fragments.clear()
        this.fragments.addAll(fragments)

        loadFragment(currentPosition)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return onTouch(this, ev ?: return false)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                return true
            }

            MotionEvent.ACTION_UP -> {
                val endX = event.x
                val diffX = endX - startX

                if (abs(diffX) > swipeThreshold) {
                    if (diffX > 0) {
                        navigateTo(currentPosition - 1)
                    } else {
                        navigateTo(currentPosition + 1)
                    }
                }
                return true
            }
        }
        return false
    }

    fun navigateTo(position: Int) {
        if (position in fragments.indices && position != currentPosition) {
            loadFragment(position)
            currentPosition = position
            tabChangeListener?.invoke(position)
        }
    }

    private fun loadFragment(position: Int) {
        val transaction = fragmentManager.beginTransaction()

        // Find the fragment to hide
        val currentFragment = fragmentManager.findFragmentByTag("Fragment_$currentPosition")
        val nextFragment = fragments[position]

        // Hide the current fragment if it's not the same
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }

        // Check if the next fragment is already in the fragmentManager
        var nextFragmentInManager = fragmentManager.findFragmentByTag("Fragment_$position")
        if (nextFragmentInManager == null) {
            // If the fragment isn't found in the manager, add it
            transaction.add(fragmentContainer!!.id, nextFragment, "Fragment_$position")
        } else {
            // If it's already in the manager, show it
            transaction.show(nextFragmentInManager)
        }

        // Commit the transaction
        transaction.commit()

        // Update the current position
        currentPosition = position
    }
}
