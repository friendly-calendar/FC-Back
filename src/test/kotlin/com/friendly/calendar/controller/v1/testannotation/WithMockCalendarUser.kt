package com.friendly.calendar.controller.v1.testannotation

import com.friendly.calendar.controller.v1.annoationimpl.WithMockCalendarUserSecurityContextFactory
import com.friendly.calendar.enum.UserRole
import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCalendarUserSecurityContextFactory::class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
annotation class WithMockCalendarUser(
    val name: String = "test",
    val email: String = "dokdogalmaegi@gmail.com",
    val username: String = "testUser",
    val password: String = "testPassword",
    val phoneNumber: String = "010-1234-5678",
    val roleAdmin: Boolean = false,
)
