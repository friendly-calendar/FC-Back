package com.friendly.calendar.domain.service

import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.domain.model.FriendLogStatus
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.persistence.FriendRequestRepository
import com.friendly.calendar.domain.service.impl.FriendStatusServiceImpl
import com.friendly.calendar.security.session.CalendarPrincipal
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
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
    private val friendStatusService: FriendStatusService,
    private val calendarUserRepository: CalendarUserRepository
) {

    @Test
    @WithMockCalendarUser
    fun `Success request friend`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser: CalendarUser = calendarPrincipal.user
        calendarUserRepository.save(calendarUser)

        assertDoesNotThrow {
            friendStatusService.requestFriend(calendarUser.id, 1L, "message")
        }
    }

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
    fun `Success accept friend request`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser: CalendarUser = calendarPrincipal.user
        calendarUserRepository.save(calendarUser)

        friendStatusService.requestFriend(calendarUser.id, 1L, "message")

        assertDoesNotThrow {
            friendStatusService.acceptFriend(calendarUser.id, 1L)
        }

        assertAll(
            { assertThat(friendRequestRepository.findAll().size).isEqualTo(1) },
            { assertThat(friendRequestRepository.findAll()[0].status).isEqualTo(FriendLogStatus.ACCEPT) }
        )
    }

    @Test
    fun `Fail accept friend invalid friend request`() {
        val testFriendRequestRepository = mockk<FriendRequestRepository>()
        val testFriendStatusService = FriendStatusServiceImpl(calendarUserRepository, testFriendRequestRepository)

        every { testFriendRequestRepository.findBySenderIdAndReceiverId(any(), any()) } returns null

        assertThrows<IllegalArgumentException> {
            testFriendStatusService.acceptFriend(1L, 2L)
        }.let {
            assertThat(it.message).isEqualTo("Friend request not found")
        }
    }
}
