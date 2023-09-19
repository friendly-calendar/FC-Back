package com.friendly.calendar.controller

import com.friendly.calendar.domain.service.EventService
import com.friendly.calendar.network.EventDto
import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.ResponseDto.Companion.success
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
        val event = eventService.getEvent(eventKey)
        return success(event)
    }

    @PostMapping
    fun createEvent(@RequestBody eventDto: EventDto): ResponseDto<Any> {
        eventService.createEvent(eventDto)
        return success()
    }

    @PatchMapping
    fun updateEvent() {
        eventService.updateEvent()
    }

    @DeleteMapping
    fun deleteEvent(@RequestParam eventKey: Long): ResponseDto<Any> {
        eventService.deleteEvent(eventKey)
        return success()
    }
}
