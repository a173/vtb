package org.vtb.registration

import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.vtb.registration.dto.FreeTimesDto
import org.vtb.registration.dto.RegistrationIdentifierDto
import org.vtb.registration.request.DeleteRegistrationRequest
import org.vtb.registration.request.FreeTimeRequest
import org.vtb.registration.request.RegistrationRequest
import kotlin.random.Random
import io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER
import org.springframework.web.bind.annotation.ResponseStatus
import org.vtb.registration.dto.ReservedTimeTodayDto
import org.vtb.registration.request.FreeTimeTodayRequest
import java.time.temporal.ChronoUnit

@RestController
@RequestMapping("/registration")
class RegistrationController(
    private val registrationService: RegistrationService,
    @Value("rootSecret")
    private val rootSecret: String
) {

    @GetMapping("/free-time")
    fun freeTime(@Valid @ParameterObject request: FreeTimeRequest) : FreeTimesDto {
        return registrationService.getFreeTime(request.officeId, request.date, request.service)
    }

    /*
    TODO и так пойдет
     */
    @PostMapping("/free-time-today")
    fun freeTimeToday(@RequestBody @Valid request: FreeTimeTodayRequest) : Map<String, ArrayList<Map<String, ReservedTimeTodayDto>>> {
        return registrationService.getFreeTimeToday(request.officeData, request.service)
    }

    @DeleteMapping("/delete")
    @Parameter(name = "x-root-secret", `in` = HEADER)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@Valid @ParameterObject request: DeleteRegistrationRequest, @RequestHeader("x-root-secret") header: String){
        if (rootSecret != header) {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST)
        }
        registrationService.delete(request.officeId, request.identifier)
    }

    @PostMapping
    fun registration(@RequestBody @Valid request: RegistrationRequest): RegistrationIdentifierDto? {
        val dateTime = request.dateTime.let {
            var minute = request.dateTime.minute
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
            if (minute != 0 && minute != request.dateTime.minute) {
                return@let it.withMinute(minute)
            }
            return@let it
        }.truncatedTo(ChronoUnit.MINUTES)
        return registrationService.add(request.copy(identifier = Random.nextInt(0, 999), dateTime = dateTime))
            ?: throw HttpClientErrorException(HttpStatus.BAD_REQUEST)
    }
}