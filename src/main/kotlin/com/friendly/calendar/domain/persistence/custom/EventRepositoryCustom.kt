package com.friendly.calendar.domain.persistence.custom

import com.friendly.calendar.domain.model.Event

interface EventRepositoryCustom {
    fun findEventWithDetails(eventKey: Long): Event
}
