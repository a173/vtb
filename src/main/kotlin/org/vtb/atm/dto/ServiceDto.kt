package org.vtb.atm.dto

class ServiceDto(
    val wheelchair: ServiceSupportDto,
    val blind: ServiceSupportDto,
    val nfcForBankCards: ServiceSupportDto,
    val qrRead: ServiceSupportDto,
    val supportsUsd: ServiceSupportDto,
    val supportsChargeRub: ServiceSupportDto,
    val supportsEur: ServiceSupportDto,
    val supportsRub: ServiceSupportDto
)