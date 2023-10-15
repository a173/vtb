package org.vtb.office.dto

import java.math.BigDecimal

data class OfficeDto(
    val salePointName: String,
    val salePointFormat: String,
    val address: String,
    val openHours: List<OpenHoursDto>,
    val rko: String?,
    val openHoursIndividual: List<OpenHoursDto>,
    val officeType: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal
)
