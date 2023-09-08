package com.friendly.calendar.repository.custom

import com.friendly.calendar.domain.model.Event

interface EventRepositoryCustom {
    fun findEventWithDetails(eventKey: Long): Event?
}
