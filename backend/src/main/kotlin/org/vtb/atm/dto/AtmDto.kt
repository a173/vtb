package org.vtb.atm.dto

import java.math.BigDecimal

data class AtmDto(
    val address: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val allDay: Boolean,
    val services: ServiceDto
)
