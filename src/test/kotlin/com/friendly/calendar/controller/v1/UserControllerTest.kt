package com.friendly.calendar.controller.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.config.AdminConfig
import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.domain.service.UserService
import com.friendly.calendar.network.UserSignInDTO
import com.friendly.calendar.network.UserSignUpDTO
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.stream.Stream

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("dev")
class UserControllerTest @Autowired constructor(
    private val userService: UserService,
    private val adminConfig: AdminConfig,
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
    fun `Signup Failure with Invalid Arguments`() {
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
            jsonPath("$.data") { doesNotExist() }
        }
    }

    @ParameterizedTest
    @MethodSource("provideInvalidParam")
    fun `Signup Failure with Existing User`(existsUserSignUpDTO: UserSignUpDTO, userSignUpDTO: UserSignUpDTO) {
        userService.createUser(existsUserSignUpDTO)

        val userSignUpDTOJson = objectMapper.writeValueAsString(userSignUpDTO)

        mockMvc.post("/api/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = userSignUpDTOJson
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.BAD_REQUEST.value()) }
            jsonPath("$.description") { value("User already exists") }
            jsonPath("$.data") { doesNotExist() }
        }
    }

    @Test
    @WithMockCalendarUser
    fun `USER ROLE is not allowed to access get user list api`() {
        mockMvc.get("/api/v1/users").andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.FORBIDDEN.value()) }
            jsonPath("$.description") { value(HttpStatus.FORBIDDEN.reasonPhrase) }
            jsonPath("$.data") { doesNotExist() }
        }
    }

    @Test
    fun `ADMIN ROLE is allowed to access get user list api`() {
        val userSignInDTO = UserSignInDTO(
            username = adminConfig.username,
            password = adminConfig.password
        )
        val token = userService.createToken(userSignInDTO)

        mockMvc.get("/api/v1/users") {
            header("Authorization", "Bearer $token")
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.OK.value()) }
            jsonPath("$.description") { value(HttpStatus.OK.reasonPhrase) }
            jsonPath("$.data") { exists() }
        }
    }

    @Test
    @WithMockCalendarUser
    fun `get my info`() {
        mockMvc.get("/api/v1/users/me").andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.OK.value()) }
            jsonPath("$.description") { value(HttpStatus.OK.reasonPhrase) }
            jsonPath("$.data") { exists() }
        }
    }

    @Test
    fun `get my info fail`() {
        mockMvc.get("/api/v1/users/me").andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.FORBIDDEN.value()) }
            jsonPath("$.description") { value(HttpStatus.FORBIDDEN.reasonPhrase) }
            jsonPath("$.data") { doesNotExist() }
        }
    }

    companion object {
        @JvmStatic
        fun provideInvalidParam(): Stream<Arguments> {
            val userSignUpDTO: UserSignUpDTO = UserSignUpDTO(
                name = "user123",
                email = "user@example.com",
                username = "username1",
                password = "password123!",
                phoneNumber = "010-1234-5678"
            )

            return Stream.of(
                Arguments.of(
                    userSignUpDTO,
                    UserSignUpDTO(
                        "user1234",
                        userSignUpDTO.email,
                        "username2",
                        "password1234!",
                        "010-8765-4321"
                    )
                ),
                Arguments.of(
                    userSignUpDTO,
                    UserSignUpDTO(
                        "user1234",
                        "user1@example.com",
                        userSignUpDTO.username,
                        "password1234!",
                        "010-8765-4321"
                    )
                ),
                Arguments.of(
                    userSignUpDTO,
                    UserSignUpDTO(
                        "user1234",
                        "user1@example.com",
                        "username2",
                        "password1234!",
                        userSignUpDTO.phoneNumber
                    )
                )
            )
        }
    }
}
