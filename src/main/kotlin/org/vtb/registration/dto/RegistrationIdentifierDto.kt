package org.vtb.registration.dto

import java.time.OffsetDateTime

data class RegistrationIdentifierDto(
    val identifier: Int,
    val dateTime: OffsetDateTime
)
