package com.friendly.calendar.service

import com.friendly.calendar.domain.model.FriendRequest
import com.friendly.calendar.domain.model.User
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.domain.persistence.FriendRequestRepository
import com.friendly.calendar.domain.service.FriendService
import com.friendly.calendar.domain.service.FriendStatusService
import com.friendly.calendar.domain.service.UserService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

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

        every { friendService.getFriendRequest(any(), any(), any(), any()) } answers {
            val args = it.invocation.args

            val sender = args[0] as User
            val receiver = args[1] as User
            val message = args[2] as String
            val status = args[3] as com.friendly.calendar.domain.model.FriendLogStatus

            FriendRequest(
                sender = sender,
                receiver = receiver,
                message = message,
                status = status
            )
        }
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

    @Test
    fun `Should success when sender and receiver are not blocked and not friend`() {
        val friendStatusService = FriendStatusService(friendRequestRepository, friendRelationRepository, friendService, userService, applicationEventPublisher)

        friendStatusService.requestFriend(existsUserId1, existsUserId2, "testMessage")
    }

    @Test
    fun `Should fail when sender is blocked by receiver`() {
        every { friendRelationRepository.isBlockedRelation(any(), any()) } returns true

        val friendStatusService = FriendStatusService(friendRequestRepository, friendRelationRepository, friendService, userService, applicationEventPublisher)

        shouldThrow<IllegalArgumentException> {
            friendStatusService.requestFriend(existsUserId1, existsUserId2, "testMessage")
        }.message shouldBe "You are blocked by this user."
    }

    @Test
    fun `Should fail when sender is already friend with receiver (request friend)`() {
        every { friendRelationRepository.isFriendRelation(any(), any()) } returns true

        val friendStatusService = FriendStatusService(friendRequestRepository, friendRelationRepository, friendService, userService, applicationEventPublisher)

        shouldThrow<IllegalArgumentException> {
            friendStatusService.requestFriend(existsUserId1, existsUserId2, "testMessage")
        }.message shouldBe "You are already friend with this user."
    }

    @Test
    fun `Should fail when sender is already friend with receiver (accept request)`() {
        every { friendRelationRepository.isFriendRelation(any(), any()) } returns true

        val friendStatusService = FriendStatusService(friendRequestRepository, friendRelationRepository, friendService, userService, applicationEventPublisher)

        shouldThrow<IllegalArgumentException> {
            friendStatusService.acceptFriend(notExistsUserId, existsUserId2, "testMessage")
        }
    }

    @Test
    fun `Should fail when there is no friend request from sender`() {
        every { friendRequestRepository.existsRequestFriend(any(), any()) } returns false

        val friendStatusService = FriendStatusService(friendRequestRepository, friendRelationRepository, friendService, userService, applicationEventPublisher)

        shouldThrow<IllegalArgumentException> {
            friendStatusService.acceptFriend(existsUserId1, existsUserId2, "testMessage")
        }.message shouldBe "There is no friend request from this user."
    }
}
