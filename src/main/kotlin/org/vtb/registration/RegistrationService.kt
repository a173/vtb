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
import java.time.temporal.ChronoUnit

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
        office.forEach { data ->
            val localFree = mutableMapOf<String, ReservedTimeTodayDto>()
            val foot = registrationRepository.getReservedTimeToday(
                data.officeId,
                now.plusMinutes(data.footTime),
                dateTimeEnd
            )
            if (foot.isEmpty()) {
                localFree.put("foot", registrationRepository.getReservedTimeToday(data.officeId)!!
                    .copy(goMinutes = data.footTime, dateTime = roundUpDateTime(now.plusMinutes(data.footTime)), service = service))
            } else {
                if (roundUpDateTime(now.plusMinutes(service.time + data.footTime)).isBefore(foot.first().dateTime)) {
                    localFree.put("foot", foot.first().copy(goMinutes = data.footTime, dateTime = roundUpDateTime(now.plusMinutes(data.footTime))))
                } else {
                    localFree.put("foot", foot.first().copy(goMinutes = data.footTime, dateTime = foot.first().dateTime!!.plusMinutes(data.footTime)))
                }
            }
            val car = registrationRepository.getReservedTimeToday(
                data.officeId,
                now.plusMinutes(data.onCarTime),
                dateTimeEnd
            )
            if (foot.isEmpty()) {
                localFree.put("car", registrationRepository.getReservedTimeToday(data.officeId)!!
                    .copy(goMinutes = data.onCarTime, dateTime = roundUpDateTime(now.plusMinutes(data.onCarTime)), service = service))
            } else {
                if (roundUpDateTime(now.plusMinutes(service.time + data.onCarTime)).isBefore(foot.first().dateTime)) {
                    localFree.put("foot", foot.first().copy(goMinutes = data.onCarTime, dateTime = roundUpDateTime(now.plusMinutes(data.onCarTime))))
                } else {
                    localFree.put("car", car.first().copy(goMinutes = data.onCarTime, dateTime = roundUpDateTime(car.first().dateTime!!.plusMinutes(data.onCarTime))))
                }
            }
            free.add(localFree)
        }
        return mapOf(Pair("results", free))
    }

    fun roundUpDateTime(dateTime: OffsetDateTime): OffsetDateTime {
        return dateTime.let {
            var minute = dateTime.minute
            if (minute % 15 != 0) {
                val ost = minute / 15
                minute = (ost + 1) * 15
            }
            /*
            TODO тут еще с днями разобраться, но мы принимаем тот факт, что филиалы работают максимум до 20.00
             */
            if (minute == 60) {
                return@let it.withMinute(0).withHour(it.hour + 1)
            }
            if (minute != 0 && minute != dateTime.minute) {
                return@let it.withMinute(minute)
            }
            return@let it
        }.truncatedTo(ChronoUnit.MINUTES)
    }
}