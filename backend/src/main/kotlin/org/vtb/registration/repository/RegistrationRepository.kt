package org.vtb.registration.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.vtb.jooq.Tables.OFFICE
import org.vtb.jooq.Tables.REGISTRATION
import org.vtb.registration.dto.Registration
import org.vtb.registration.dto.RegistrationIdentifierDto
import org.vtb.registration.dto.ReservedTimeDto
import org.vtb.registration.dto.ReservedTimeTodayDto
import java.time.OffsetDateTime

@Repository
class RegistrationRepository(private val jooq: DSLContext) {

    /**
     * Конечно тут надо возвращать модель, но некогда мапать
     */
    fun save(registration: Registration): RegistrationIdentifierDto? {
        return jooq.insertInto(REGISTRATION)
            .set(REGISTRATION.CLIENT_ID, registration.clientId)
            .set(REGISTRATION.OFFICE_ID, registration.officeId)
            .set(REGISTRATION.DATE_TIME, registration.dateTime)
            .set(REGISTRATION.SERVICE, registration.service.name)
            .set(REGISTRATION.IDENTIFIER, registration.identifier)
            .returningResult(REGISTRATION.IDENTIFIER, REGISTRATION.DATE_TIME)
            .fetchOne()
            ?.into(RegistrationIdentifierDto::class.java)
    }

    fun getReservedTime(officeId: Long, dateTimeBegin: OffsetDateTime, dateTimeEnd: OffsetDateTime): List<ReservedTimeDto> {
        return jooq.select(REGISTRATION.OFFICE_ID, REGISTRATION.DATE_TIME, REGISTRATION.SERVICE)
            .from(REGISTRATION)
            .where(
                REGISTRATION.OFFICE_ID.eq(officeId),
                REGISTRATION.DATE_TIME.ge(dateTimeBegin),
                REGISTRATION.DATE_TIME.le(dateTimeEnd),
            )
            .fetchInto(ReservedTimeDto::class.java)
    }

    fun delete(officeId: Long, identifier: Int) {
        jooq.deleteFrom(REGISTRATION).where(REGISTRATION.OFFICE_ID.eq(officeId), REGISTRATION.IDENTIFIER.eq(identifier))
    }

    fun getReservedTimeToday(officeId: Long, dateTimeBegin: OffsetDateTime, dateTimeEnd: OffsetDateTime): List<ReservedTimeTodayDto> {
        return jooq.select(OFFICE.ID, OFFICE.SALE_POINT_NAME, OFFICE.ADDRESS, REGISTRATION.DATE_TIME, REGISTRATION.SERVICE)
            .from(OFFICE)
            .leftJoin(REGISTRATION)
            .on(OFFICE.ID.eq(REGISTRATION.OFFICE_ID))
            .where(
                REGISTRATION.OFFICE_ID.eq(officeId),
                REGISTRATION.DATE_TIME.ge(dateTimeBegin),
                REGISTRATION.DATE_TIME.le(dateTimeEnd),
            )
            .fetchInto(ReservedTimeTodayDto::class.java)
    }

    fun getReservedTimeToday(officeId: Long): ReservedTimeTodayDto? {
        return jooq.select(OFFICE.ID, OFFICE.SALE_POINT_NAME, OFFICE.ADDRESS)
            .from(OFFICE)
            .where(OFFICE.ID.eq(officeId))
            .fetchOneInto(ReservedTimeTodayDto::class.java)
    }

}