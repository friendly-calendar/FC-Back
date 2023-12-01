package com.friendly.calendar.security.session

import com.friendly.calendar.domain.persistence.CalendarUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CalendarUserDetailsServiceImpl(
    private val calendarUserRepository: CalendarUserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        calendarUserRepository.findByUsername(username)?.let {
            CalendarPrincipal(it)
        } ?: throw UsernameNotFoundException(username)
}
