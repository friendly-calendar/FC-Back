package com.friendly.calendar.repository.custom

import com.friendly.calendar.entity.event.Event

interface EventRepositoryCustom {
    fun findEventWithDetails(eventId: Long) : Event?
}
