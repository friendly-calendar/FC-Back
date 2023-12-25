package com.friendly.calendar.domain.service

import com.friendly.calendar.config.AdminConfig
import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.service.impl.UserServiceImpl
import com.friendly.calendar.dto.UserDTO
import com.friendly.calendar.dto.UserSignInDTO
import com.friendly.calendar.dto.UserSignUpDTO
import com.friendly.calendar.security.JwtProvider
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.Transactional
import java.util.Date
import javax.crypto.SecretKey
import kotlin.reflect.full.declaredMemberProperties

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtProvider: JwtProvider,
    private val userService: UserService,
    private val adminConfig: AdminConfig
) {

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

    @Test
    fun `email 또는 username 또는 phone number 가 동일한 유저가 존재할 경우 에러가 발생한다`() {
        val calendarUserRepositoryMock = mockk<CalendarUserRepository>()
        every { calendarUserRepositoryMock.findByEmailOrUsernameOrPhoneNumber(any(), any(), any()) } returns mockk<CalendarUser>()

        val testUserService = UserServiceImpl(calendarUserRepositoryMock, bCryptPasswordEncoder, jwtProvider)

        assertThatThrownBy {
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
    fun `정상적으로 토큰을 생성한다`() {
        val userSignInDTO = UserSignInDTO(
            username = adminConfig.username,
            password = adminConfig.password
        )

        val token = userService.createToken(userSignInDTO)
        assertThat(token).isNotEmpty
    }

    @Test
    fun `존재하지 않는 유저로 토큰 생성 시도 시 BadCredentialsException 이 발생한다`() {
        val invalidSignInDTO = UserSignInDTO(
            username = "invalid",
            password = "invalid"
        )

        assertThatThrownBy {
            userService.createToken(invalidSignInDTO)
        }.hasMessage("아이디나 비밀번호를 확인해주세요.")
    }

    @Test
    fun `존재하는 유저지만 비밀번호가 일치하지 않을 경우 BadCredentialsException 이 발생한다`() {
        val invalidSignInDTO = UserSignInDTO(
            username = adminConfig.username,
            password = "invalid"
        )

        assertThatThrownBy {
            userService.createToken(invalidSignInDTO)
        }.hasMessage("아이디나 비밀번호를 확인해주세요.")
    }

    @Test
    fun `정상적으로 token 을 생성한다`() {
        val userSignInDTO = UserSignInDTO(
            username = adminConfig.username,
            password = adminConfig.password
        )

        val accessToken = userService.createToken(userSignInDTO)
        val refreshToken = userService.createRefreshToken(userSignInDTO.username)

        val newAccessToken = userService.createToken(accessToken, refreshToken)
        assertThat(newAccessToken).isNotEmpty()
    }

    @Test
    fun `만료된 refresh token 일 경우 IllegalArgumentException 이 발생한다`() {
        mockkStatic(Jwts::class)

        val accessToken = "valid.access.token"
        val refreshToken = "expired.refresh.token"
        every { Jwts.parser().verifyWith(any<SecretKey>()).build().parseSignedClaims(accessToken) } answers {
            val mockedJwsClaims = mockk<Jws<Claims>>()
            val mockedClaims = mockk<Claims>()
            every { mockedJwsClaims.payload } returns mockedClaims
            every { mockedClaims.subject } returns "testUser1"
            every { mockedClaims.expiration } answers {
                Date(System.currentTimeMillis() + 10000)
            }

            mockedJwsClaims
        }

        every { Jwts.parser().verifyWith(any<SecretKey>()).build().parseSignedClaims(refreshToken) } answers {
            val mockedJwsClaims = mockk<Jws<Claims>>()
            val mockedClaims = mockk<Claims>()
            every { mockedJwsClaims.payload } returns mockedClaims
            every { mockedClaims.expiration } answers {
                Date(System.currentTimeMillis() - 10000)
            }

            mockedJwsClaims
        }

        val expiredException = assertThrows<IllegalArgumentException> { userService.createToken(accessToken, refreshToken) }
        assertThat(expiredException.message).isEqualTo("Expired token")

        unmockkStatic(Jwts::class)
    }

    @Test
    fun `access token과 refresh token의 subject가 다를 경우 IllegalArgumentException 이 발생한다`() {
        mockkStatic(Jwts::class)

        val accessToken = "valid.access.token"
        val refreshToken = "invalid.refresh.token"

        every { Jwts.parser().verifyWith(any<SecretKey>()).build().parseSignedClaims(accessToken) } answers {
            val mockedJwsClaims = mockk<Jws<Claims>>()
            val mockedClaims = mockk<Claims>()
            every { mockedJwsClaims.payload } returns mockedClaims
            every { mockedClaims.subject } returns "testUser1"
            every { mockedClaims.expiration } answers {
                Date(System.currentTimeMillis() + 10000)
            }

            mockedJwsClaims
        }

        every { Jwts.parser().verifyWith(any<SecretKey>()).build().parseSignedClaims(refreshToken) } answers {
            val mockedJwsClaims = mockk<Jws<Claims>>()
            val mockedClaims = mockk<Claims>()
            every { mockedJwsClaims.payload } returns mockedClaims
            every { mockedClaims.subject } returns "testUser"
            every { mockedClaims.expiration } answers {
                Date(System.currentTimeMillis() + 10000)
            }

            mockedJwsClaims
        }

        val invalidTokenException = assertThrows<IllegalArgumentException> { userService.createToken(accessToken, refreshToken) }
        assertThat(invalidTokenException.message).isEqualTo("Not valid refresh token")

        unmockkStatic(Jwts::class)
    }

    @Test
    fun `정상적으로 refresh token 을 생성한다`() {
        val refreshToken = userService.createRefreshToken("admin")

        assertThat(refreshToken).isNotEmpty()
    }

    @Test
    fun `존재하지 않는 username 으로 refresh token 을 발급할 경우 IllegalArgumentException 이 발생한다`() {
        assertThrows<IllegalArgumentException> {
            userService.createRefreshToken("test")
        }
    }

    @Test
    fun `유저 리스트를 정상적으로 가져온다`() {
        val users = userService.getUsers()
        val testUser = users[0]

        UserDTO::class.declaredMemberProperties.forEach {
            assertThat(hasProperty(testUser, it.name)).isTrue()
        }
    }

    private fun hasProperty(testObject: Any, propId: String): Boolean =
        testObject::class.declaredMemberProperties.any { it.name == propId }
}
