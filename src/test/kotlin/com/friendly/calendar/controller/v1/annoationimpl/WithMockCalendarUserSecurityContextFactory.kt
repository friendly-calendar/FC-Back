package com.friendly.calendar.controller.v1.annoationimpl

import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.security.session.CalendarPrincipal
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockCalendarUserSecurityContextFactory: WithSecurityContextFactory<WithMockCalendarUser> {
    override fun createSecurityContext(testCalendarUser: WithMockCalendarUser): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()

        val calendarPrincipal = CalendarPrincipal(
            CalendarUser().apply {
                name = testCalendarUser.name
                email = testCalendarUser.email
                username = testCalendarUser.username
                password = testCalendarUser.password
                phoneNumber = testCalendarUser.phoneNumber
                roleAdmin = testCalendarUser.roleAdmin
            }
        )

        val auth = UsernamePasswordAuthenticationToken(calendarPrincipal, "", calendarPrincipal.authorities)
        context.authentication = auth
        return context
    }
}