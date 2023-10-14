package org.vtb.atm

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.vtb.atm.dto.AtmsDto
import org.vtb.atm.repository.AtmRepository

@Service
class AtmService(
    private val objectMapper: ObjectMapper,
    private val atmRepository: AtmRepository,
    @Value("classpath:data/atms.json")
    private val resourceAtmFile: Resource,
    @Value("\${parse.atm}")
    private val parseAtm: Boolean,
) {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)

    @EventListener
    @Transactional
    fun onContextRefreshedEvent(contextRefreshedEvent: ContextRefreshedEvent) {
        if (parseAtm) {
            val count = atmRepository.add(objectMapper.readValue(resourceAtmFile.file, AtmsDto::class.java).atms)
            logger.info("Добавлено {} банкоматов", count)
        }
    }
}