package com.friendly.calendar.domain.service

import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.persistence.FriendRequestRepository
import com.friendly.calendar.domain.service.impl.FriendStatusServiceImpl
import com.friendly.calendar.security.session.CalendarPrincipal
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class FriendStatusServiceTest @Autowired constructor(
    private val friendRequestRepository: FriendRequestRepository,
    private val friendStatusService: FriendStatusService
) {
    @Test
    fun `Fail request friend invalid user`() {
        val calendarUserRepository = mockk<CalendarUserRepository>()
        val testFriendStatusService = FriendStatusServiceImpl(calendarUserRepository, friendRequestRepository)

        every { calendarUserRepository.findByIdOrNull(any()) } returns null

        assertThrows<IllegalArgumentException> {
            testFriendStatusService.requestFriend(1L, 2L, "message")
        }.let {
            assertThat(it.message).isEqualTo("User not found")
        }
    }

    @Test
    @WithMockCalendarUser
    fun `Success request friend`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser: CalendarUser = calendarPrincipal.user

        assertDoesNotThrow {
            friendStatusService.requestFriend(calendarUser.id, 1L, "message")
        }
    }
}
