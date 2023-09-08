package com.friendly.calendar.repository.custom.impl

import com.friendly.calendar.domain.model.Event
import com.friendly.calendar.entity.event.QEvent.*
import com.friendly.calendar.entity.event.QEventDate.*
import com.friendly.calendar.entity.event.QEventLocation.*
import com.friendly.calendar.entity.event.QEventMember.*
import com.friendly.calendar.repository.custom.EventRepositoryCustom
import com.querydsl.jpa.impl.JPAQueryFactory

class EventRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : EventRepositoryCustom {
    override fun findEventWithDetails(eventKey: Long): Event? {
        return queryFactory
            .selectFrom(event)
            .leftJoin(event.eventDate, eventDate).fetchJoin()
            .leftJoin(event.eventLocation, eventLocation).fetchJoin()
            .leftJoin(event.members, eventMember).fetchJoin()
            .where(event.id.eq(eventKey))
            .fetchOne()
    }
}
