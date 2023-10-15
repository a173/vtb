package com.example.vtb.interfaces

import com.example.vtb.models.DepartmentWithTime

interface ListenerDepartment {

    fun onClick(department: DepartmentWithTime, isMan:Boolean)

}