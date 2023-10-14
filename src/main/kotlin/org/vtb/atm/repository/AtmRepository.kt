package org.vtb.atm.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.jooq.JSONB
import org.vtb.jooq.Tables.ATM
import org.vtb.jooq.Tables.ATM_SERVICE
import org.vtb.atm.dto.AtmDto

@Repository
class AtmRepository(
    private val jooq: DSLContext,
    private val objectMapper: ObjectMapper
) {

    fun add(atms: List<AtmDto>): Int {
        return jooq.transactionResult { transaction ->
            val ids = transaction.dsl().insertInto(ATM)
                .columns(ATM.ADDRESS, ATM.LATITUDE, ATM.LONGITUDE, ATM.ALL_DAY)
                .apply {
                    atms.forEach {
                        values(it.address, it.latitude, it.longitude, it.allDay)
                    }
                }.onConflictDoNothing()
                .returningResult(ATM.ID)
                .fetchInto(Long::class.java)
            transaction.dsl().insertInto(ATM_SERVICE)
                .columns(ATM_SERVICE.ATM_ID, ATM_SERVICE.DATA)
                .apply {
                    ids.forEach {
                        values(it, JSONB.jsonb(objectMapper.writeValueAsString(atms[it.toInt() - 1].services)))
                    }
                }
                .onConflictDoNothing()
                .execute()
        }
    }
}