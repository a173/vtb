package org.vtb.office

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.vtb.office.dto.OfficeDto
import org.vtb.office.dto.OfficesDto
import org.vtb.office.repository.OfficeRepository
import org.vtb.office.request.AddOfficeRequest

@Service
class OfficeService(
    private val objectMapper: ObjectMapper,
    private val officeRepository: OfficeRepository,
    @Value("classpath:data/offices.json")
    private val resourceOfficeFile: Resource,
    @Value("\${parse.office}")
    private val parseOffice: Boolean
) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)

    @EventListener
    @Transactional
    fun onContextRefreshedEvent(contextRefreshedEvent: ContextRefreshedEvent) {
        if (parseOffice) {
            val count = officeRepository.add(objectMapper.readValue(resourceOfficeFile.file, object : TypeReference<List<OfficeDto>>() {}))
            logger.info("Добавлено {} отделений", count)
        }
    }

    fun getOffices(city: String?) : OfficesDto {
        return OfficesDto(officeRepository.all(city))
    }

    fun add(request: AddOfficeRequest) {
        logger.info("Добавление отделения, data={}", request)
        officeRepository.add(request)
    }
}