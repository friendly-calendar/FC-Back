package com.friendly.calendar.controller.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.network.UserSignUpDTO
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("dev")
class UserControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    @Test
    fun signUp() {
        val userSignUpDTO = UserSignUpDTO(
            name = "user123",
            email = "user@example.com",
            username = "username1",
            password = "password123!",
            phoneNumber = "010-1234-5678"
        )

        val userSignUpDTOJson = objectMapper.writeValueAsString(userSignUpDTO)

        mockMvc.post("/api/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = userSignUpDTOJson
        }.andExpect { status { isCreated() } }
    }

    @Test
    fun signUpFail() {
        val userSignUpDTO = UserSignUpDTO(
            name = "user123",
            email = "user@example.com",
            username = "t",
            password = "password123!",
            phoneNumber = "010-1234-5678"
        )

        val userSignUpDTOJson = objectMapper.writeValueAsString(userSignUpDTO)

        mockMvc.post("/api/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = userSignUpDTOJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.BAD_REQUEST.value()) }
            jsonPath("$.description") { value("아이디는 4 ~ 15 사이로 입력해주세요.") }
            jsonPath("$.data") { value(null) }
        }
    }
}