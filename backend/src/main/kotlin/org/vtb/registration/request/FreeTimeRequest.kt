package org.vtb.registration.request

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Min
import org.vtb.registration.dto.ServiceType
import java.time.LocalDate

data class FreeTimeRequest(
    @field:Min(1)
    val officeId: Long,
    @field:Future
    val date: LocalDate,
    val service: ServiceType
)
