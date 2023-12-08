package com.friendly.calendar.domain.service

import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.domain.model.FriendStatus
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.security.session.CalendarPrincipal
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
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
    @Transactional
    fun `failure requestFriend with not found user`() {
        val testFriend = calendarUserRepository.findByUsername("admin")!!

        Assertions.assertThatThrownBy {
            friendService.requestFriend(999, testFriend.id)
        }.message().isEqualTo("User not found")
    }

    @Test
    @Transactional
    fun `failure requestFriend with same user`() {
        val testUser = calendarUserRepository.findByUsername("admin")!!

        Assertions.assertThatThrownBy {
            friendService.requestFriend(testUser.id, testUser.id)
        }.message().isEqualTo("Cannot send friend request to yourself")
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
                assertThat(
                    friendRelationRepository.findAll().all {
                        it.status == FriendStatus.ACCEPTED
                    }
                ).isTrue()
            }
        )
    }

    @Test
    @Transactional
    fun `failure acceptFriend with not found friend request`() {
        val testFriend = calendarUserRepository.findByUsername("admin")!!

        Assertions.assertThatThrownBy {
            friendService.acceptFriend(1, testFriend.id)
        }.message().isEqualTo("Friend request not found")
    }
}
