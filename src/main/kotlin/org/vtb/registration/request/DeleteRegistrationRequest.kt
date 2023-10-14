package org.vtb.registration.request

import jakarta.validation.constraints.Min

data class DeleteRegistrationRequest(
    val identifier: Int,
    @field:Min(1)
    val officeId: Long
)
