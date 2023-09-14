package com.friendly.calendar.service

import com.friendly.calendar.domain.model.User
import com.friendly.calendar.domain.persistence.FriendRequestRepository
import com.friendly.calendar.domain.persistence.UserRepository
import com.friendly.calendar.domain.service.FriendStatusService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.Optional

class FriendStatusServiceTest : AnnotationSpec() {

    private val userRepository = mockk<UserRepository>()

    private val friendRequestRepository = mockk<FriendRequestRepository>()

    private val existsUserId1: Long = 1
    private val existsUserId2: Long = 2
    private val notExistsUserId: Long = 3

    @BeforeEach
    fun setUp() {
        every { userRepository.findById(existsUserId1) } returns Optional.of(
            User(
                id = 1,
                username = "jylim",
                password = "jylim12345",
                email = "jylim@test.com",
                phoneNumber = "010-1234-5678",
                name = "TestName",
            )
        )

        every { userRepository.findById(existsUserId2) } returns Optional.of(
            User(
                id = 2,
                username = "jylim2",
                password = "jylim12345",
                email = "limjy@test.com",
                phoneNumber = "010-1111-2222",
                name = "TestName2",
            )
        )

        every { userRepository.findById(notExistsUserId) } returns Optional.empty()

        every { friendRequestRepository.save(any()) } returns mockk()
    }

    @Test
    fun `Should fail when sender is missing`() {
        val friendStatusService = FriendStatusService(friendRequestRepository, userRepository)

        shouldThrow<IllegalArgumentException> {
            friendStatusService.requestFriend(notExistsUserId, existsUserId2, "testMessage")
        }.message shouldBe "sender not found"
    }

    @Test
    fun `Should fail when receiver is missing`() {
        val friendStatusService = FriendStatusService(friendRequestRepository, userRepository)

        shouldThrow<IllegalArgumentException> {
            friendStatusService.requestFriend(existsUserId1, notExistsUserId, "testMessage")
        }.message shouldBe "receiver not found"
    }
}
