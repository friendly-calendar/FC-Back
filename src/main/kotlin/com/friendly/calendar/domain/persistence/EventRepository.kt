package com.friendly.calendar.domain.persistence

import com.friendly.calendar.domain.model.Event
import com.friendly.calendar.domain.persistence.custom.EventRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long>, EventRepositoryCustom
