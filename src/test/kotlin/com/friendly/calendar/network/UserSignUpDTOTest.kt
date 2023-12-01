package com.friendly.calendar.network

import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class UserSignUpDTOTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `유효성 검사를 통과한다`() {
        val userSignUpDTO = UserSignUpDTO(
            nickname = "user123",
            email = "user@example.com",
            username = "username1",
            password = "password123!",
            phoneNumber = "010-1234-5678"
        )

        val violations = validator.validate(userSignUpDTO)

        assertThat(violations).isEmpty()
    }

    @ParameterizedTest
    @MethodSource("provideInvalidParam")
    fun `Invalid nickname`(nickname: String, email: String, username: String, password: String, phoneNumber: String) {
        val userSignUpDTO = UserSignUpDTO(
            nickname,
            email,
            username,
            password,
            phoneNumber
        )

        val violations = validator.validate(userSignUpDTO)

        assertThat(violations.size).isEqualTo(1)
    }

    companion object {
        @JvmStatic
        fun provideInvalidParam(): Stream<Arguments> {
            val validUserSignUpDTO = UserSignUpDTO(
                nickname = "dokdogal",
                email = "dokdogalmaegi@gmail.com",
                username = "dokdogalmaegi",
                password = "password123!",
                phoneNumber = "010-1234-5678"
            )

            return Stream.of(
                // invalid nickname
                Arguments.of("u", validUserSignUpDTO.email, validUserSignUpDTO.username, validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                Arguments.of("testtesttest", validUserSignUpDTO.email, validUserSignUpDTO.username, validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                Arguments.of("한글", validUserSignUpDTO.email, validUserSignUpDTO.username, validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                // invalid email
                Arguments.of(validUserSignUpDTO.nickname, "user@example.", validUserSignUpDTO.username, validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                Arguments.of(validUserSignUpDTO.nickname, "user", validUserSignUpDTO.username, validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                Arguments.of(validUserSignUpDTO.nickname, "한한", validUserSignUpDTO.username, validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                // invalid username
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, "u", validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, "testtesttesttest", validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, "한한한한", validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, "tes", validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, "tessttessttessttesst", validUserSignUpDTO.password, validUserSignUpDTO.phoneNumber),
                // invalid password
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, validUserSignUpDTO.username, "password", validUserSignUpDTO.phoneNumber),
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, validUserSignUpDTO.username, "password한글", validUserSignUpDTO.phoneNumber),
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, validUserSignUpDTO.username, "한하하하한하낳하하하", validUserSignUpDTO.phoneNumber),
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, validUserSignUpDTO.username, "tes", validUserSignUpDTO.phoneNumber),
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, validUserSignUpDTO.username, "teststeststeststeststests", validUserSignUpDTO.phoneNumber),
                // invalid phone number
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, validUserSignUpDTO.username, validUserSignUpDTO.password, "010-1234-567"),
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, validUserSignUpDTO.username, validUserSignUpDTO.password, "010-1234-56789"),
                Arguments.of(validUserSignUpDTO.nickname, validUserSignUpDTO.email, validUserSignUpDTO.username, validUserSignUpDTO.password, "010-1234-567890"),
            )
        }
    }
}