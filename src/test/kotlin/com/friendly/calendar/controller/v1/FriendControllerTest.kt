package com.friendly.calendar.controller.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.network.FriendRequestDTO
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("dev")
class FriendControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    @Test
    @WithMockCalendarUser
    fun `Request friend`() {
        val friendRequestDTO = FriendRequestDTO(
            receiverId = 1L,
            message = "Hello"
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
            message = "Hello"
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
}
