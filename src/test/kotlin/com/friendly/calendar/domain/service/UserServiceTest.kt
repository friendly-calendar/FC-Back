package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.service.impl.UserServiceImpl
import com.friendly.calendar.network.UserSignUpDTO
import com.friendly.calendar.security.JwtProvider
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val calendarUserRepository: CalendarUserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtProvider: JwtProvider,
    private val userService: UserService
) {

    @Test
    fun `email 또는 username 또는 phone number 가 동일한 유저가 존재할 경우 에러가 발생한다`() {
        val calendarUserRepositoryMock = mockk<CalendarUserRepository>()
        every { calendarUserRepositoryMock.findByEmailOrUsernameOrPhoneNumber(any(), any(), any()) } returns mockk<CalendarUser>()

        val testUserService = UserServiceImpl(calendarUserRepositoryMock, bCryptPasswordEncoder, jwtProvider)

        Assertions.assertThatThrownBy {
            testUserService.createUser(
                UserSignUpDTO(
                    username = "test",
                    password = "test",
                    name = "test",
                    email = "test",
                    phoneNumber = "test",
                )
            )
        }.hasMessage("User already exists")
    }

    @Test
    @Transactional
    fun `정상적으로 유저를 저장한다`() {
        val userSignUpDTO = UserSignUpDTO(
            name = "user123",
            email = "user@example.com",
            username = "username1",
            password = "password123!",
            phoneNumber = "010-1234-5678"
        )

        userService.createUser(userSignUpDTO)
    }
}