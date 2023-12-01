package com.friendly.calendar.security

import com.friendly.calendar.security.session.CalendarPrincipal
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class LoginListener {

    @EventListener
    fun onSuccess(event: AuthenticationSuccessEvent) {
        val user = (event.authentication.principal as CalendarPrincipal).user

        logger.info { "${user.username} 님이 로그인 하였습니다." }
    }
}