package org.vtb.registration.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.OffsetDateTime
import java.time.OffsetTime

data class ReservedTimeDto(
    val officeId: Long,
    val service: ServiceType,
    @JsonIgnore
    val dateTime: OffsetDateTime
) {
    fun getTimeStart() : OffsetTime {
        return dateTime.toOffsetTime()
    }

    fun getTimeEnd() : OffsetTime {
        return dateTime.plusMinutes(service.time).toOffsetTime()
    }
}
