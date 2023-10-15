package org.vtb.registration.dto

data class FreeTimesDto(
    val time: List<FreeTimeDto>
) {
    fun getCount(): Int {
        return time.size
    }
}
