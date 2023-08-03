package com.friendly.calendar.security

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class SpringSecurityAuditorAware : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
//        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
//
//        if (authentication == null || !authentication.isAuthenticated) {
//            return Optional.empty()
//        }
//
//        val username: String = authentication.principal as String
//        return Optional.of(username)
        return Optional.of("test")
    }
}
