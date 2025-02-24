package com.sait.tawajudpremiumplusnewfeatured.core

import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Result


open class BaseFragment() : Fragment() {

    /**
     * this method calls [BaseActivity.addFragment].
     * So, it will add fragment in Activity's container
     */
    open fun addFragment(fragment: Fragment?, containerId: Int, addToBackStack: Boolean) {
        if (fragment != null) {
            (activity as BaseActivity).addFragment(fragment, containerId, addToBackStack)
        }
    }

    /**
     * this method calls [BaseActivity.replaceFragment].
     * So, it will replace fragment in Activity's container
     */
    open fun replaceFragment(fragment: Fragment?, containerId: Int, addToBackStack: Boolean) {
        if (fragment != null) {
            (activity as BaseActivity).replaceFragment(fragment, containerId, addToBackStack)
        }

    }

    /**
     * this method uses [.getChildFragmentManager] and adds nested fragment inside Fragment's container.
     * using it with activity's container will throw [IllegalStateException] or may cause other errors.
     */
    protected open fun addChildFragment(
        fragment: Fragment,
        containerId: Int,
        addToBackStack: Boolean
    ) {
        val fragmentManager = this.childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(containerId, fragment)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }
        fragmentTransaction.commit()
    }

    /**
     * this method uses [.getChildFragmentManager] and replaces nested fragment inside Fragment's container
     * using it with activity's container will throw [IllegalStateException] or may cause other errors.
     */
    protected open fun replaceChildFragment(
        fragment: Fragment,
        containerId: Int,
        addToBackStack: Boolean
    ) {
        val fragmentManager = this.childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(containerId, fragment)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }
        fragmentTransaction.commit()
    }


    open fun onResult(result: Result) {}
}