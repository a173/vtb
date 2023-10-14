package org.vtb.office

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.vtb.office.dto.OfficesDto
import org.vtb.office.request.AddOfficeRequest

@RestController
@RequestMapping("/office")
class OfficeController(
    private val officeService: OfficeService,
    @Value("rootSecret")
    private val rootSecret: String
) {

    @GetMapping
    fun getOffices(city: String?): OfficesDto {
        return officeService.getOffices(city)
    }

    @PostMapping("/add")
    @Parameter(name = "x-root-secret", `in` = ParameterIn.HEADER)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun add(@Valid @ParameterObject request: AddOfficeRequest, @RequestHeader("x-root-secret") header: String){
        if (rootSecret != header) {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST)
        }
        officeService.add(request)
    }
}