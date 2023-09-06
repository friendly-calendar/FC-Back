package com.friendly.calendar.service

import com.friendly.calendar.entity.event.Event
import com.friendly.calendar.network.event.EventDto
import com.friendly.calendar.repository.EventRepository
import com.friendly.calendar.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class EventService(val eventRepository: EventRepository, val userRepository: UserRepository) {

    @Transactional
    fun createEvent(eventDto: EventDto): Event {
        val invitedMembers = eventDto.invitedMembersId?.map { userRepository.findById(it).get() }
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
