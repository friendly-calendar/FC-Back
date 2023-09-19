package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.Event
import com.friendly.calendar.domain.model.baseEntity.DelFlag.*
import com.friendly.calendar.domain.persistence.EventRepository
import com.friendly.calendar.domain.persistence.UserRepository
import com.friendly.calendar.network.EventDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class EventService(val eventRepository: EventRepository, val userRepository: UserRepository) {

    @Transactional(readOnly = true)
    fun getEvent(eventKey: Long): Event? {
        return eventRepository.findEventWithDetails(eventKey)
    }

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

    fun updateEvent() {
        TODO()
    }

    fun deleteEvent(eventKey: Long) {
        val event = eventRepository.findById(eventKey)

        event.map {
            it.delFlag = Y
            eventRepository.save(it)
        }.orElseThrow { IllegalArgumentException("event not found") }
    }
}
