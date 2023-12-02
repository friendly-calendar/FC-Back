package com.friendly.calendar.security.session

import com.friendly.calendar.domain.model.CalendarUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CalendarPrincipalTest {

    private val calendarUserPrincipal = CalendarPrincipal(CalendarUser())
    private val calendarAdminPrincipal = CalendarPrincipal(CalendarUser().apply { roleAdmin = true })

    @Test
    fun `get user getAuthorities`() {
        assertThat(calendarUserPrincipal.authorities.size).isEqualTo(1)
    }

    @Test
    fun `get admin getAuthorities`() {
        assertThat(calendarAdminPrincipal.authorities.size).isEqualTo(2)
    }

    @Test
    fun getPassword() {
        assertThat(calendarUserPrincipal.password).isEqualTo("")
    }

    @Test
    fun getUsername() {
        assertThat(calendarUserPrincipal.username).isEqualTo("")
    }

    @Test
    fun isAccountNonExpired() {
        assertThat(calendarUserPrincipal.isAccountNonExpired).isEqualTo(true)
    }

    @Test
    fun isAccountNonLocked() {
        assertThat(calendarUserPrincipal.isAccountNonLocked).isEqualTo(true)
    }

    @Test
    fun isCredentialsNonExpired() {
        assertThat(calendarUserPrincipal.isCredentialsNonExpired).isEqualTo(true)
    }

    @Test
    fun isEnabled() {
        assertThat(calendarUserPrincipal.isEnabled).isEqualTo(true)
    }

    @Test
    fun getUser() {
        assertThat(calendarUserPrincipal.user).isNotNull
    }
}