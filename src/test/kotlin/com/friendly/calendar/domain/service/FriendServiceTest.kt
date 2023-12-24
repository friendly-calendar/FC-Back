package com.friendly.calendar.domain.service

import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.domain.model.FriendStatus
import com.friendly.calendar.domain.model.base.DelFlag
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.dto.domain.FriendDTO.FriendReturnDTO
import com.friendly.calendar.security.session.CalendarPrincipal
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class FriendServiceTest @Autowired constructor(
    private val calendarUserRepository: CalendarUserRepository,
    private val friendService: FriendService,
    private val friendRelationRepository: FriendRelationRepository
) {

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `success requestFriend`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val testUser = calendarUserRepository.findByUsername(calendarPrincipal.username)!!
        val testFriend = calendarUserRepository.findByUsername("admin")!!

        assertAll(
            {
                assertThatNoException().isThrownBy {
                    friendService.requestFriend(testUser.id, testFriend.id)
                }
            },
            { assertThat(friendRelationRepository.findAll().size).isEqualTo(1) }
        )
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `success request friend with after unblock`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val testUser = calendarUserRepository.findByUsername(calendarPrincipal.username)!!
        val testFriend = calendarUserRepository.findByUsername("admin")!!

        friendService.blockFriend(testUser.id, testFriend.id)
        friendService.unblockFriend(testUser.id, testFriend.id)

        assertAll(
            {
                assertDoesNotThrow {
                    friendService.requestFriend(testUser.id, testFriend.id)
                }
            },
            {
                val friendRelation = friendRelationRepository.findPendingRelationByUserIdAndFriendId(testUser.id, testFriend.id)!!

                assertThat(friendRelation.delFlag).isEqualTo(DelFlag.N)
                assertThat(friendRelation.status).isEqualTo(FriendStatus.PENDING)
            }
        )
    }

    @Test
    fun `failure requestFriend with not found user`() {
        val testFriend = calendarUserRepository.findByUsername("admin")!!

        assertThatThrownBy {
            friendService.requestFriend(999, testFriend.id)
        }.message().isEqualTo("User not found22")
    }

    @Test
    fun `failure requestFriend with same user`() {
        val testUser = calendarUserRepository.findByUsername("admin")!!

        assertThatThrownBy {
            friendService.requestFriend(testUser.id, testUser.id)
        }.message().isEqualTo("Cannot send friend request to yourself")
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `failure requestFriend with blocked user`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val testUser = calendarUserRepository.findByUsername(calendarPrincipal.username)!!
        val testFriend = calendarUserRepository.findByUsername("admin")!!

        friendService.requestFriend(testUser.id, testFriend.id)

        friendService.rejectFriend(testUser.id, testFriend.id, true)

        assertThatThrownBy {
            friendService.requestFriend(testUser.id, testFriend.id)
        }.message().isEqualTo("Cannot send friend request to blocked user")
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `success acceptFriend`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val testUser = calendarUserRepository.findByUsername(calendarPrincipal.username)!!
        val testFriend = calendarUserRepository.findByUsername("admin")!!

        friendService.requestFriend(testUser.id, testFriend.id)

        assertAll(
            {
                assertThatNoException().isThrownBy {
                    friendService.acceptFriend(testUser.id, testFriend.id)
                }
            },
            {
                assertThat(friendRelationRepository.findAll().size).isEqualTo(2)
            },
            {
                assertThat(
                    friendRelationRepository.findAll().all {
                        it.status == FriendStatus.ACCEPTED
                    }
                ).isTrue()
            }
        )
    }

    @Test
    fun `failure acceptFriend with not found friend request`() {
        val testFriend = calendarUserRepository.findByUsername("admin")!!

        assertThatThrownBy {
            friendService.acceptFriend(1, testFriend.id)
        }.message().isEqualTo("Friend request not found")
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `success rejectFriend`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val testUser = calendarUserRepository.findByUsername("admin")!!
        val testFriend = calendarUserRepository.findByUsername(calendarPrincipal.username)!!

        friendService.requestFriend(testUser.id, testFriend.id)

        assertAll(
            {
                assertThatNoException().isThrownBy {
                    friendService.rejectFriend(testUser.id, testFriend.id)
                }
            },
            {
                assertThat(friendRelationRepository.findAll().size).isEqualTo(1)
            },
            {
                assertThat(
                    friendRelationRepository.findAll().all {
                        it.status == FriendStatus.REJECTED
                    }
                ).isTrue()
            }
        )
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `success rejectFriend with block`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val testUser = calendarUserRepository.findByUsername("admin")!!
        val testFriend = calendarUserRepository.findByUsername(calendarPrincipal.username)!!

        friendService.requestFriend(testUser.id, testFriend.id)

        assertAll(
            {
                assertThatNoException().isThrownBy {
                    friendService.rejectFriend(testUser.id, testFriend.id, true)
                }
            },
            {
                assertThat(friendRelationRepository.findAll().size).isEqualTo(2)
            },
            {
                assertThat(
                    friendRelationRepository.findAll().all {
                        it.status == FriendStatus.BLOCKED
                    }
                ).isTrue()
            }
        )
    }

    @Test
    @Transactional
    fun `failure rejectFriend with not found friend request`() {
        val testFriend = calendarUserRepository.findByUsername("admin")!!

        assertThatThrownBy {
            friendService.rejectFriend(1, testFriend.id)
        }.message().isEqualTo("Friend request not found")
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `success blockFriend`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val testUser = calendarUserRepository.findByUsername("admin")!!
        val testFriend = calendarUserRepository.findByUsername(calendarPrincipal.username)!!

        friendService.requestFriend(testUser.id, testFriend.id)

        assertAll(
            {
                assertThatNoException().isThrownBy {
                    friendService.blockFriend(testUser.id, testFriend.id)
                }
            },
            {
                assertThat(friendRelationRepository.findAll().size).isEqualTo(2)
            },
            {
                assertThat(
                    friendRelationRepository.findAll().all {
                        it.status == FriendStatus.BLOCKED
                    }
                ).isTrue()
            }
        )
    }

    @Test
    @Transactional
    fun `failure blockFriend with same user`() {
        val testFriend = calendarUserRepository.findByUsername("admin")!!

        assertThatThrownBy {
            friendService.blockFriend(testFriend.id, testFriend.id)
        }.message().isEqualTo("Cannot block friend to yourself")
    }

    @Test
    @Transactional
    fun `failure blockFriend with not exists friend`() {
        val testUser = calendarUserRepository.findByUsername("admin")!!

        assertAll(
            {
                assertThatThrownBy {
                    friendService.blockFriend(testUser.id, 999L)
                }.message().isEqualTo("User not found")
            },
            {
                assertThatThrownBy {
                    friendService.blockFriend(999L, testUser.id)
                }.message().isEqualTo("User not found")
            }
        )
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `success unblockFriend`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val testUser = calendarUserRepository.findByUsername("admin")!!
        val testFriend = calendarUserRepository.findByUsername(calendarPrincipal.username)!!

        friendService.blockFriend(testUser.id, testFriend.id)
        assertAll(
            { assertDoesNotThrow { friendService.unblockFriend(testUser.id, testFriend.id) } },
            {
                val friendRelations = friendRelationRepository.findAll()
                assertThat(friendRelations.size).isEqualTo(2)

                val expectedList = friendRelations.filter {
                    it.status == FriendStatus.BLOCKED && it.delFlag == DelFlag.Y
                }
                assertThat(expectedList.size).isEqualTo(2)
            },
        )
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `failure unblock with not block user`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val testUser = calendarUserRepository.findByUsername("admin")!!
        val testFriend = calendarUserRepository.findByUsername(calendarPrincipal.username)!!

        friendService.blockFriend(testUser.id, testFriend.id)
        val otherException = assertThrows<IllegalArgumentException> {
            friendService.unblockFriend(testFriend.id, testUser.id)
        }
        assertThat(otherException.message).isEqualTo("User is not blocked by this user")
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `failure unblock with not exists block relation`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val testUser = calendarUserRepository.findByUsername("admin")!!
        val testFriend = calendarUserRepository.findByUsername(calendarPrincipal.username)!!
        val otherException = assertThrows<IllegalArgumentException> {
            friendService.unblockFriend(testUser.id, testFriend.id)
        }
        assertThat(otherException.message).isEqualTo("Not exists block relation")
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `success get friend list`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val adminUser = calendarUserRepository.findByUsername("admin")!!
        val testUser = calendarUserRepository.findByUsername(calendarPrincipal.username)!!

        friendService.requestFriend(testUser.id, adminUser.id)
        friendService.acceptFriend(testUser.id, adminUser.id)

        val testUserFriendList: List<FriendReturnDTO> = friendService.getFriendList(testUser.id)
        val adminUserFriendList: List<FriendReturnDTO> = friendService.getFriendList(adminUser.id)

        assertAll(
            { assertThat(testUserFriendList.size).isEqualTo(1) },
            { assertThat(adminUserFriendList.size).isEqualTo(1) },
            { assertThat(testUserFriendList[0].friendAlias).isEqualTo("admin") },
            { assertThat(adminUserFriendList[0].friendAlias).isEqualTo(testUser.username) },
            { assertThat(testUserFriendList[0].path).isNull() },
            { assertThat(adminUserFriendList[0].path).isNull() }
        )
    }

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `success get friend list friend alias`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        calendarUserRepository.save(calendarPrincipal.user)

        val adminUser = calendarUserRepository.findByUsername("admin")!!
        val testUser = calendarUserRepository.findByUsername(calendarPrincipal.username)!!

        friendService.requestFriend(testUser.id, adminUser.id)
        friendService.acceptFriend(testUser.id, adminUser.id)

        val testUserFriendRelation = friendRelationRepository.findFriendRelationByUserIdAndFriendId(testUser.id, adminUser.id)!!
        testUserFriendRelation.friendAlias = "aliasTest"
        friendRelationRepository.save(testUserFriendRelation)

        val testUserFriendList = friendService.getFriendList(testUser.id)

        assertThat(testUserFriendList[0].friendAlias).isEqualTo("aliasTest")
    }
}
