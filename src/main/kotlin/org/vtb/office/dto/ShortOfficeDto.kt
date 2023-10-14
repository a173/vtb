package org.vtb.office.dto

import java.math.BigDecimal

data class ShortOfficeDto(
    val id: Long,
    val salePointName: String,
    val address: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal
)
