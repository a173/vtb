package org.vtb.registration.request

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Min
import org.vtb.registration.dto.Registration
import org.vtb.registration.dto.ServiceType
import java.time.OffsetDateTime

data class RegistrationRequest(
    @field:Min(1)
    override val officeId: Long,
    @field:Min(1)
    override val clientId: Long,
    @field:FutureOrPresent
    override val dateTime: OffsetDateTime,
    override val identifier: Int?,
    override val service: ServiceType
) : Registration
