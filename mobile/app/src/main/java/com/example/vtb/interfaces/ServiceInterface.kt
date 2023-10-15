package com.example.vtb.interfaces

import com.example.vtb.enums.ServiceType

interface ServiceInterface {

    fun getService(): ServiceType
    fun setService(parameter: ServiceType)

}