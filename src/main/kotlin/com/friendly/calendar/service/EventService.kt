package com.friendly.calendar.service

import com.friendly.calendar.entity.event.Event
import com.friendly.calendar.entity.event.EventDate
import com.friendly.calendar.entity.event.EventLocation
import com.friendly.calendar.entity.event.EventMember
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
        val eventDate = EventDate(
            startDate = eventDto.startDate,
            endDate = eventDto.endDate,
        )
        val eventLocation = EventLocation(
            location = eventDto.location
        )
        val members: List<EventMember> = eventDto.invitedMembers?.map {
            EventMember(invitedUser = it, status = eventDto.status)
        } ?: emptyList()

        return Event(
            title = eventDto.title,
            description = eventDto.description,
            eventDate = eventDate,
            eventLocation = eventLocation,
            members = members
        )
    }
}
