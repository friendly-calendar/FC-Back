package com.friendly.calendar.controller.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.domain.service.UserService
import com.friendly.calendar.dto.UserSignInDTO
import com.friendly.calendar.dto.UserSignUpDTO
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
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
class AuthControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    val userService: UserService
) {
    @BeforeEach
    fun setup() {
        val userSignUpDTO = UserSignUpDTO(
            name = "user123",
            email = "user@example.com",
            username = "username1",
            password = "password123!",
            phoneNumber = "010-1234-5678"
        )

        userService.createUser(userSignUpDTO)
    }

    @Test
    fun signIn() {
        val userSignInDTO = UserSignInDTO(
            username = "username1",
            password = "password123!"
        )

        val userSignInDTOJson = objectMapper.writeValueAsString(userSignInDTO)

        mockMvc.post("/api/v1/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = userSignInDTOJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.OK.value()) }
            jsonPath("$.description") { value(HttpStatus.OK.reasonPhrase) }
            jsonPath("$.data") { exists() }
        }
    }

    @Test
    fun signInFail() {
        val userSignInDTO = UserSignInDTO(
            username = "username1",
            password = "password123"
        )

        val userSignInDTOJson = objectMapper.writeValueAsString(userSignInDTO)

        mockMvc.post("/api/v1/auth") {
            contentType = MediaType.APPLICATION_JSON
            content = userSignInDTOJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.UNAUTHORIZED.value()) }
            jsonPath("$.description") { value("Invalid username or password") }
            jsonPath("$.data") { value(null) }
        }
    }
}
