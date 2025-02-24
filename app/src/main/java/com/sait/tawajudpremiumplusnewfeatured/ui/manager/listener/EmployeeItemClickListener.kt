package com.sait.tawajudpremiumplusnewfeatured.ui.manager.listener

import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.AdminData
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.Manager_EmployeeData


interface EmployeeItemClickListener {
    fun onEmployeeItemClick(position: Int)

}


interface EmployeeItemClickListenerAdmin {
    fun onEmployeeItemClickAdmin(adminData: AdminData)

}

interface EmployeeItemClickListenerManager {
    fun onEmployeeItemClickManager(manager_EmployeeData: Manager_EmployeeData)

}