package com.friendly.calendar.controller

import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import com.friendly.calendar.network.EventDto
import com.friendly.calendar.domain.service.EventService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
class EventController(val eventService: EventService) {

    @GetMapping
    fun getEvent(@RequestParam eventKey: Long): ResponseDto<Any> {
        return try {
            val event = eventService.getEvent(eventKey)
            ResponseDto.success(event)
        } catch (e: Exception) {
            ResponseDto.fail(errorCode = ErrorCode.NOT_FOUND)
        }
    }

    @PostMapping
    fun createEvent(@RequestBody eventDto: EventDto): ResponseDto<Any> {
        return try {
            eventService.createEvent(eventDto)
            ResponseDto.success()
        } catch (e: Exception) {
            ResponseDto.fail(errorCode = ErrorCode.NOT_FOUND)
        }
    }

    @PatchMapping
    fun updateEvent() {
        eventService.updateEvent()
    }

    @DeleteMapping
    fun deleteEvent() {
        eventService.deleteEvent()
    }
}
