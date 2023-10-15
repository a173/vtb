package org.vtb.registration.dto

import java.time.OffsetDateTime

interface Registration {
    val officeId: Long
    val clientId: Long?
    val dateTime: OffsetDateTime
    val identifier: Int?
    val service: ServiceType
}