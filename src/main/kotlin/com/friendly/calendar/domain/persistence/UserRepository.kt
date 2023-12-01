package com.friendly.calendar.domain.persistence

import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): CalendarUser?
}
