package com.friendly.calendar.controller.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.domain.model.FriendStatus
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.domain.service.FriendService
import com.friendly.calendar.network.domain.FriendDTO.FriendPatchDTO
import com.friendly.calendar.network.domain.FriendDTO.FriendRejectDTO
import com.friendly.calendar.network.domain.FriendDTO.FriendRequestDTO
import com.friendly.calendar.security.session.CalendarPrincipal
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("dev")
class FriendControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val userRepository: CalendarUserRepository,
    private val friendService: FriendService,
    private val friendRelationRepository: FriendRelationRepository
) {

    @Test
    @WithMockCalendarUser
    fun `Request friend`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser = calendarPrincipal.user
        userRepository.save(calendarUser)

        val friendRequestDTO = FriendRequestDTO(
            receiverId = 1L,
        )

        val friendRequestDTOJson = objectMapper.writeValueAsString(friendRequestDTO)

        mockMvc.post("/api/v1/friends") {
            contentType = MediaType.APPLICATION_JSON
            content = friendRequestDTOJson
        }.andExpect { status { isCreated() } }
    }

    @Test
    @WithMockCalendarUser
    fun `Request friend with invalid receiver id`() {
        val friendRequestDTO = FriendRequestDTO(
            receiverId = 0L,
        )

        val friendRequestDTOJson = objectMapper.writeValueAsString(friendRequestDTO)

        mockMvc.post("/api/v1/friends") {
            contentType = MediaType.APPLICATION_JSON
            content = friendRequestDTOJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.BAD_REQUEST.value()) }
            jsonPath("$.description") { value("User not found") }
            jsonPath("$.data") { doesNotExist() }
        }
    }

    @Test
    @WithMockCalendarUser
    fun `Accept friend`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser = calendarPrincipal.user
        userRepository.save(calendarUser)

        val findUser = userRepository.findByUsername(calendarUser.username)

        friendService.requestFriend(1L, findUser!!.id)

        val friendRequestPatchDTO = FriendPatchDTO(
            senderId = 1L,
        )

        val friendRequestAcceptDTOJson = objectMapper.writeValueAsString(friendRequestPatchDTO)

        mockMvc.patch("/api/v1/friends/accept") {
            contentType = MediaType.APPLICATION_JSON
            content = friendRequestAcceptDTOJson
        }.andExpect { status { isOk() } }
    }

    @Test
    @WithMockCalendarUser
    fun `Accept friend failure not exists friend request`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser = calendarPrincipal.user
        userRepository.save(calendarUser)

        val findUser = userRepository.findByUsername(calendarUser.username)

        val friendRequestPatchDTO = FriendPatchDTO(
            senderId = findUser!!.id,
        )

        val friendRequestAcceptDTOJson = objectMapper.writeValueAsString(friendRequestPatchDTO)

        mockMvc.patch("/api/v1/friends/accept") {
            contentType = MediaType.APPLICATION_JSON
            content = friendRequestAcceptDTOJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.BAD_REQUEST.value()) }
            jsonPath("$.description") { value("Friend request not found") }
            jsonPath("$.data") { doesNotExist() }
        }
    }

    @Test
    @WithMockCalendarUser
    fun `Accept friend with invalid sender id`() {
        val friendRequestPatchDTO = FriendPatchDTO(
            senderId = 0L,
        )

        val friendRequestAcceptDTOJson = objectMapper.writeValueAsString(friendRequestPatchDTO)

        mockMvc.patch("/api/v1/friends/accept") {
            contentType = MediaType.APPLICATION_JSON
            content = friendRequestAcceptDTOJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.BAD_REQUEST.value()) }
            jsonPath("$.description") { value("Friend request not found") }
            jsonPath("$.data") { doesNotExist() }
        }
    }

    @Test
    @WithMockCalendarUser
    fun `Reject friend`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser = calendarPrincipal.user
        userRepository.save(calendarUser)

        val findUser = userRepository.findByUsername(calendarUser.username)

        friendService.requestFriend(1L, findUser!!.id)

        val friendRequestPatchDTO = FriendRejectDTO(
            senderId = 1L,
        )

        val friendRequestRejectDTOJson = objectMapper.writeValueAsString(friendRequestPatchDTO)

        mockMvc.patch("/api/v1/friends/reject") {
            contentType = MediaType.APPLICATION_JSON
            content = friendRequestRejectDTOJson
        }.andExpect { status { isOk() } }
    }

    @Test
    @WithMockCalendarUser
    fun `Reject friend with block`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser = calendarPrincipal.user
        userRepository.save(calendarUser)

        val findUser = userRepository.findByUsername(calendarUser.username)

        friendService.requestFriend(1L, findUser!!.id)

        val friendRequestPatchDTO = FriendRejectDTO(
            senderId = 1L,
            isBlock = true
        )

        val friendRequestRejectDTOJson = objectMapper.writeValueAsString(friendRequestPatchDTO)

        mockMvc.patch("/api/v1/friends/reject") {
            contentType = MediaType.APPLICATION_JSON
            content = friendRequestRejectDTOJson
        }.andExpect { status { isOk() } }

        friendRelationRepository.findAll().let {
            assertThat(it[0].user.id).isEqualTo(1L)
            assertThat(it[0].friend.id).isEqualTo(findUser.id)
            assertThat(it[0].status).isEqualTo(FriendStatus.BLOCKED)
        }
    }

    @Test
    @WithMockCalendarUser
    fun `Reject friend failure not exists friend request`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser = calendarPrincipal.user
        userRepository.save(calendarUser)

        val findUser = userRepository.findByUsername(calendarUser.username)

        val friendRequestPatchDTO = FriendRejectDTO(
            senderId = findUser!!.id,
        )

        val friendRequestRejectDTOJson = objectMapper.writeValueAsString(friendRequestPatchDTO)

        mockMvc.patch("/api/v1/friends/reject") {
            contentType = MediaType.APPLICATION_JSON
            content = friendRequestRejectDTOJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.BAD_REQUEST.value()) }
            jsonPath("$.description") { value("Friend request not found") }
            jsonPath("$.data") { doesNotExist() }
        }
    }
}
