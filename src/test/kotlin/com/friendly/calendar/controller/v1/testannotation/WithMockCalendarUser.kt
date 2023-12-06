package com.friendly.calendar.controller.v1.testannotation

import com.friendly.calendar.controller.v1.annoationimpl.WithMockCalendarUserSecurityContextFactory
import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCalendarUserSecurityContextFactory::class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
annotation class WithMockCalendarUser(
    val name: String = "test",
    val email: String = "dokdogalmaegi@gmail.com",
    val username: String = "testUser",
    val password: String = "testPassword",
    val phoneNumber: String = "010-9999-9999",
    val roleAdmin: Boolean = false,
)
