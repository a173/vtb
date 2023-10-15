package com.example.vtb.models

import java.io.Serializable
import java.time.OffsetTime

data class DepartmentWithTime(
    val id: Int?,
    val salePointName: String?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val time: String?,
) : Serializable
