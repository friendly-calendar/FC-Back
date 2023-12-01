package com.friendly.calendar.domain.persistence

import com.friendly.calendar.domain.model.CalendarUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarUserRepository : JpaRepository<CalendarUser, Long> {
    fun findByUsername(username: String): CalendarUser?
}
