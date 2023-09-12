package com.friendly.calendar.service

import com.friendly.calendar.domain.model.Event
import com.friendly.calendar.network.event.EventDto
import com.friendly.calendar.repository.EventRepository
import com.friendly.calendar.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class EventService(val eventRepository: EventRepository, val userRepository: UserRepository) {

    fun getEvent(eventId: Long): Event? {
        return eventRepository.findEventWithDetails(eventId)
    }

    @Transactional
    fun createEvent(eventDto: EventDto): Event {
        val invitedMembers = eventDto.invitedMembersId?.map { userRepository.findByUsername(it).get() }
        val event = Event(
            title = eventDto.title,
            description = eventDto.description,
            startDate = eventDto.startDate,
            endDate = eventDto.endDate,
            location = eventDto.location,
            status = eventDto.status,
            invitedUser = invitedMembers ?: emptyList()
        )
        return eventRepository.save(event)
    }
}
