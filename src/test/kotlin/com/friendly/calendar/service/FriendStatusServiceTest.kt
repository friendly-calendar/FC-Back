package com.friendly.calendar.service

import com.friendly.calendar.domain.model.User
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.domain.persistence.FriendRequestRepository
import com.friendly.calendar.domain.persistence.UserRepository
import com.friendly.calendar.domain.service.FriendService
import com.friendly.calendar.domain.service.FriendStatusService
import com.friendly.calendar.domain.service.UserService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.Optional

class FriendStatusServiceTest : AnnotationSpec() {

    private val userService = mockk<UserService>()

    private val friendRequestRepository = mockk<FriendRequestRepository>()

    private val friendRelationRepository = mockk<FriendRelationRepository>()

    private val friendService = mockk<FriendService>()

    private val applicationEventPublisher = mockk<org.springframework.context.ApplicationEventPublisher>()

    private val existsUserId1: Long = 1
    private val existsUserId2: Long = 2
    private val notExistsUserId: Long = 3

    @BeforeEach
    fun setUp() {
        every { userService.findUserById(existsUserId1) } returns User(
            id = 1,
            username = "jylim",
            password = "jylim12345",
            email = "jylim@test.com",
            phoneNumber = "010-1234-5678",
            name = "TestName",
        )

        every { userService.findUserById(existsUserId2) } returns User(
            id = 2,
            username = "jylim2",
            password = "jylim12345",
            email = "limjy@test.com",
            phoneNumber = "010-1111-2222",
            name = "TestName2",
        )

        every { userService.findUserById(notExistsUserId) } throws IllegalArgumentException("User with id $notExistsUserId not found")

        every { friendRequestRepository.save(any()) } returns mockk()

        every { friendRelationRepository.isBlockedRelation(any(), any()) } returns false

        every { friendRelationRepository.isFriendRelation(any(), any()) } returns false
    }

    @Test
    fun `Should fail when sender is missing`() {
        val friendStatusService = FriendStatusService(friendRequestRepository, friendRelationRepository, friendService, userService, applicationEventPublisher)

        shouldThrow<IllegalArgumentException> {
            friendStatusService.requestFriend(notExistsUserId, existsUserId2, "testMessage")
        }
    }

    @Test
    fun `Should fail when receiver is missing`() {
        val friendStatusService = FriendStatusService(friendRequestRepository, friendRelationRepository, friendService, userService, applicationEventPublisher)

        shouldThrow<IllegalArgumentException> {
            friendStatusService.requestFriend(existsUserId1, notExistsUserId, "testMessage")
        }
    }
}
