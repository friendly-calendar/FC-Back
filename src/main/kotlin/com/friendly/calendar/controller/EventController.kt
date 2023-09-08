package com.friendly.calendar.controller

import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import com.friendly.calendar.network.event.EventDto
import com.friendly.calendar.service.EventService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
class EventController(val eventService: EventService) {

    @PostMapping
    fun createEvent(@RequestBody eventDto: EventDto): ResponseDto<Any> {
        return try {
            eventService.createEvent(eventDto)
            ResponseDto.success()
        } catch (e: Exception) {
            ResponseDto.fail(errorCode = ErrorCode.NOT_FOUND)
        }
    }
}
