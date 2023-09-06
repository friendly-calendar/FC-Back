package com.friendly.calendar.service

import com.friendly.calendar.entity.event.Event
import com.friendly.calendar.network.event.EventDto
import com.friendly.calendar.repository.EventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class EventService(val eventRepository: EventRepository) {

    @Transactional
    fun createEvent(eventDto: EventDto): Event {
        val event: Event = toEntity(eventDto)
        return eventRepository.save(event)
    }

    private fun toEntity(eventDto: EventDto): Event {
        return Event(
            title = eventDto.title,
            description = eventDto.description,
            startDate = eventDto.startDate,
            endDate = eventDto.endDate,
            location = eventDto.location,
            status = eventDto.status,
            invitedUser = eventDto.invitedMembers ?: emptyList()
        )
    }
}
