package org.vtb.office.request

import java.math.BigDecimal

data class AddOfficeRequest(
    val salePointName: String,
    val address: String,
    val officeType: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal
)
