package org.vtb.registration.dto

data class FreeTimeToday(
    val officeId: Long,
    val officeAddress: String,
    val officeTitle: String,
    val footTime: Int,
    val onCarTime: Int,
    val freeTimeDto: FreeTimeDto
)
