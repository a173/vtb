package org.vtb.registration.dto

import java.time.OffsetTime

data class FreeTimeDto(
    val timeStart: OffsetTime,
    val timeEnd: OffsetTime
)
