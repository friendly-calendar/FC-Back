package com.friendly.calendar.security

import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.context.SecurityContextHolder

@SpringBootTest
class LoginListenerTest @Autowired constructor(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    @Test
    @WithMockCalendarUser
    fun `Login Listener Test`() {
        val event = AuthenticationSuccessEvent(SecurityContextHolder.getContext().authentication)

        assertDoesNotThrow {
            applicationEventPublisher.publishEvent(event)
        }
    }
}
