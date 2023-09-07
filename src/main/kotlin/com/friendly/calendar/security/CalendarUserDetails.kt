package com.friendly.calendar.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

// TODO :why jvm field
class CalendarUserDetails(@JvmField val id: String, @JvmField val password: String) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun getPassword() = password

    override fun getUsername() = id

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled(): Boolean = true
}
