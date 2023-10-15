package org.vtb.registration.dto

enum class ServiceType(val time: Long) {
    CARD_AND_ACCOUNT(20),
    DEPOSIT_AND_INVEST(20),
    CREDIT(60),
    MORTGAGE(120),
    CURRENCY_EXCHANGE(5)
}