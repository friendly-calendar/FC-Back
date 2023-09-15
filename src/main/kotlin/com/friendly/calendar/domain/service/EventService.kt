package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.*
import com.friendly.calendar.domain.model.baseEntity.DelFlag.*
import com.friendly.calendar.domain.model.enum.EventInvitationStatus
import com.friendly.calendar.domain.model.enum.EventInvitationStatus.*
import com.friendly.calendar.network.EventDto
import com.friendly.calendar.domain.persistence.EventRepository
import com.friendly.calendar.domain.persistence.UserRepository
import com.friendly.calendar.network.EventUpdateDto
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
        val eventDate = EventDate(
            startDate = eventDto.startDate,
            endDate = eventDto.endDate,
        )
        val eventLocation = eventDto.location?.let {
            EventLocation(location = it)
        }
        val members: List<EventMember> = eventDto.invitedMembersId?.map {
            val invitedUser = userRepository.findByUsername(it).get()
            EventMember(invitedUser = invitedUser, eventInvitationStatus = INVITED)
        } ?: emptyList()
         val event = Event(
            title = eventDto.title,
            description = eventDto.description,
            eventDate = eventDate,
            eventLocation = eventLocation,
            members = members
        )
        return eventRepository.save(event)
    }

    fun updateEvent(eventUpdateDto: EventUpdateDto) {
        val event = eventRepository.findEventWithDetails(eventUpdateDto.eventKey)

        event?.let {
            val eventDate = EventDate(
                startDate = eventUpdateDto.startDate,
                endDate = eventUpdateDto.endDate,
            )
            val eventLocation = eventUpdateDto.location?.let {
                EventLocation(location = it)
            }
            val members: List<EventMember> = eventUpdateDto.eventMemberDto?.map {
                val invitedUser = userRepository.findByUsername(it.invitedMembersId).get()
                EventMember(invitedUser = invitedUser, eventInvitationStatus = it.eventInvitationStatus as EventInvitationStatus)
            } ?: emptyList()
            event.title = eventUpdateDto.title
            event.description = eventUpdateDto.description
            event.eventDate = eventDate
            event.eventLocation = eventLocation
            event.members = members
        }
    }

    fun deleteEvent(eventKey: Long) {
        val event = eventRepository.findById(eventKey)

        event.map {
            it.delFlag = Y
            eventRepository.save(it)
        }.orElseThrow { IllegalArgumentException("event not found") }
    }
}
