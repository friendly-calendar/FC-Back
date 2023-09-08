package com.friendly.calendar.repository

import com.friendly.calendar.domain.model.Event
import com.friendly.calendar.repository.custom.EventRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long>, EventRepositoryCustom
