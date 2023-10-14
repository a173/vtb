package org.vtb.office.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.jooq.JSONB
import org.jooq.impl.DSL.trueCondition
import org.vtb.jooq.Tables.OFFICE
import org.vtb.office.dto.OfficeDto
import org.vtb.office.dto.ShortOfficeDto
import org.vtb.office.request.AddOfficeRequest

@Repository
class OfficeRepository(
    private val jooq: DSLContext,
    private val objectMapper: ObjectMapper
) {

    fun add(offices: List<OfficeDto>): Int {
        return jooq.insertInto(OFFICE)
            .columns(OFFICE.SALE_POINT_NAME, OFFICE.SALE_POINT_FORMAT, OFFICE.ADDRESS, OFFICE.OPEN_HOURS, OFFICE.RKO, OFFICE.OPEN_HOURS_INDIVIDUAL, OFFICE.OFFICE_TYPE, OFFICE.LATITUDE, OFFICE.LONGITUDE)
            .apply { offices.forEach {
                values(it.salePointName, it.salePointFormat, it.address, JSONB.jsonb(objectMapper.writeValueAsString(it.openHours)), it.rko, JSONB.jsonb(objectMapper.writeValueAsString(it.openHoursIndividual)), it.officeType, it.latitude, it.longitude)
            } }
            .onConflictDoNothing()
            .execute()
    }

    fun add(office: AddOfficeRequest) {
        jooq.insertInto(OFFICE)
            .columns(OFFICE.SALE_POINT_NAME, OFFICE.SALE_POINT_FORMAT, OFFICE.ADDRESS, OFFICE.OPEN_HOURS, OFFICE.RKO, OFFICE.OPEN_HOURS_INDIVIDUAL, OFFICE.OFFICE_TYPE, OFFICE.LATITUDE, OFFICE.LONGITUDE)
            .values(office.salePointName, "format", office.address, JSONB.jsonb("{}"), "rko", JSONB.jsonb("{}"), office.officeType, office.latitude, office.longitude)
            .onConflictDoNothing()
            .execute()
    }

    fun all(city: String?): List<ShortOfficeDto> {
        return jooq.select(
            OFFICE.ID,
            OFFICE.SALE_POINT_NAME,
            OFFICE.ADDRESS,
            OFFICE.LATITUDE,
            OFFICE.LONGITUDE
        )
            .from(OFFICE)
            .where(if (city == null) trueCondition() else OFFICE.ADDRESS.like("%$city%"))
            .fetchInto(ShortOfficeDto::class.java)
    }
}