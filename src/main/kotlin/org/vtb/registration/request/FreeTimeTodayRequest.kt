package org.vtb.registration.request

import org.vtb.registration.dto.ServiceType

data class FreeTimeTodayRequest(
    val officeData: List<OfficeData>,
    val service: ServiceType
)
