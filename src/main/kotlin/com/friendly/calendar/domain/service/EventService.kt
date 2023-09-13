package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.Event
import com.friendly.calendar.network.EventDto
import com.friendly.calendar.domain.persistence.EventRepository
import com.friendly.calendar.domain.persistence.UserRepository
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
            eventInvitationStatus = eventDto.eventInvitationStatus,
            invitedUser = invitedMembers ?: emptyList()
        )
        return eventRepository.save(event)
    }
}
