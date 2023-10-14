package org.vtb.registration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.vtb.registration.dto.*
import org.vtb.registration.repository.RegistrationRepository
import org.vtb.registration.request.OfficeData
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId

@Service
class RegistrationService(
    private val registrationRepository: RegistrationRepository
) {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)

    @Transactional
    fun add(registration: Registration): RegistrationIdentifierDto? {
        logger.info("Добавление записи в отделение {} на {}", registration.officeId, registration.dateTime)

        return registrationRepository.save(registration)
    }

    fun getFreeTime(officeId: Long, date: LocalDate, service: ServiceType) : FreeTimesDto {
        val zone = ZoneId.of("Europe/Moscow")
        val dateTimeBegin = date.atStartOfDay(zone).toOffsetDateTime().plusHours(8)
        val dateTimeEnd = date.atStartOfDay(zone).toOffsetDateTime().plusHours(18)
        val reserved = registrationRepository.getReservedTime(officeId, dateTimeBegin, dateTimeEnd)
        val free = ArrayList<FreeTimeDto>()
        var lastTimeBegin = dateTimeBegin.toOffsetTime()
        if (reserved.isNotEmpty()) {
            for (value in reserved) {
                val timeEnd = value.getTimeStart()
                var plusServiceTime = lastTimeBegin.plusMinutes(service.time)
                while (plusServiceTime <= timeEnd) {
                    free.add(FreeTimeDto(lastTimeBegin, plusServiceTime))
                    lastTimeBegin = plusServiceTime
                    plusServiceTime = lastTimeBegin.plusMinutes(service.time)
                }
                lastTimeBegin = value.getTimeEnd()
            }
            val timeEnd = dateTimeEnd.toOffsetTime()
            var plusServiceTime = lastTimeBegin.plusMinutes(service.time)
            while (plusServiceTime <= timeEnd) {
                free.add(FreeTimeDto(lastTimeBegin, plusServiceTime))
                lastTimeBegin = plusServiceTime
                plusServiceTime = lastTimeBegin.plusMinutes(service.time)
            }
        } else {
            val timeEnd = dateTimeEnd.toOffsetTime()
            var plusServiceTime = lastTimeBegin.plusMinutes(service.time)
            while (plusServiceTime <= timeEnd) {
                free.add(FreeTimeDto(lastTimeBegin, plusServiceTime))
                lastTimeBegin = plusServiceTime
                plusServiceTime = lastTimeBegin.plusMinutes(service.time)
            }
        }
        return FreeTimesDto(free)
    }

    fun delete(officeId: Long, identifier: Int) {
        logger.info("Удалние записи в отделение {}, идентификатор {}", officeId, identifier)
        registrationRepository.delete(officeId, identifier)
    }

    /*
    TODO 3 часа ночи
     */
    fun getFreeTimeToday(office: List<OfficeData>, service: ServiceType): Map<String, ArrayList<Map<String, ReservedTimeTodayDto>>> {
        val zone = ZoneId.of("Europe/Moscow")
        val now = OffsetDateTime.now()
        val dateTimeEnd = LocalDate.now().atStartOfDay(zone).toOffsetDateTime().plusHours(18)
        val free = ArrayList<Map<String, ReservedTimeTodayDto>>()
        office.forEach {
            val localFree = mutableMapOf<String, ReservedTimeTodayDto>()
            val foot = registrationRepository.getReservedTimeToday(
                it.officeId,
                now.plusMinutes(it.footTime),
                dateTimeEnd
            )
            if (foot.isEmpty()) {
                localFree.put("foot", registrationRepository.getReservedTimeToday(it.officeId)!!
                    .copy(goMinutes = it.footTime, dateTime = now.plusMinutes(it.footTime), service = service))
            } else {
                localFree.put("foot", foot.first().let { c ->
                    c.copy(goMinutes = it.footTime, dateTime = c.dateTime!!.plusMinutes(it.footTime))
                })
            }
            val car = registrationRepository.getReservedTimeToday(
                it.officeId,
                now.plusMinutes(it.onCarTime),
                dateTimeEnd
            )
            if (foot.isEmpty()) {
                localFree.put("car", registrationRepository.getReservedTimeToday(it.officeId)!!
                    .copy(goMinutes = it.onCarTime, dateTime = now.plusMinutes(it.onCarTime), service = service))
            } else {
                localFree.put("car", car.first().let { c ->
                    c.copy(goMinutes = it.onCarTime, dateTime = c.dateTime!!.plusMinutes(it.onCarTime))
                })
            }
            free.add(localFree)
        }
        return mapOf(Pair("results", free))
    }
}