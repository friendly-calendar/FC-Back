package com.friendly.calendar.repository.custom.impl

import com.friendly.calendar.entity.event.*
import com.friendly.calendar.entity.event.QEvent.*
import com.friendly.calendar.entity.event.QEventDate.*
import com.friendly.calendar.entity.event.QEventLocation.*
import com.friendly.calendar.entity.event.QEventMember.*
import com.friendly.calendar.repository.custom.EventRepositoryCustom
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired

class EventRepositoryImpl(
    @Autowired
    val queryFactory: JPAQueryFactory
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
