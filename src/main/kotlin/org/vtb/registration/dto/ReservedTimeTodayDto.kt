package org.vtb.registration.dto

import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.temporal.ChronoUnit

data class ReservedTimeTodayDto(
    val id: Long,
    val salePointName: String,
    val address: String,
    val dateTime: OffsetDateTime?,
    val service: ServiceType?,
    val goMinutes: Long?
) {
    fun getTimeStart() : OffsetTime? {
        return if (dateTime == null) null else dateTime.toOffsetTime()
    }

    fun getTimeEnd() : OffsetTime? {
        return if (dateTime == null) null else dateTime.plusMinutes(service!!.time).toOffsetTime()
    }

    fun getTime(): Long? {
        return if (getTimeEnd() == null) null else ChronoUnit.MINUTES.between(OffsetTime.now(), getTimeEnd())
    }
}
