package com.sait.tawajudpremiumplusnewfeatured.ui.violations.listener

import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationData

interface ViolationItemClickListener {
    fun onPermissionItemClick(v: ViolationData, position: Int)
    fun onViolationSwipeItemClick(v: ViolationData, position: Int)

}