package com.friendly.calendar.domain.persistence.custom.impl

import com.friendly.calendar.domain.model.Event
import com.friendly.calendar.domain.model.QEvent.event
import com.friendly.calendar.domain.model.QEventDate.eventDate
import com.friendly.calendar.domain.model.QEventLocation.eventLocation
import com.friendly.calendar.domain.model.QEventMember.eventMember
import com.friendly.calendar.domain.model.baseEntity.DelFlag
import com.friendly.calendar.domain.persistence.custom.EventRepositoryCustom
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
            .where(event.id.eq(eventKey).and(event.delFlag.eq(DelFlag.N)))
            .fetchOne()
    }
}
