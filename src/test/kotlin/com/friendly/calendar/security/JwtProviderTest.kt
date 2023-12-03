package com.friendly.calendar.security

import com.friendly.calendar.config.JwtConfig
import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.enum.UserRole
import com.friendly.calendar.security.session.CalendarPrincipal
import com.friendly.calendar.security.session.CalendarUserDetailsServiceImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import jakarta.servlet.http.HttpServletRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Date
import javax.crypto.SecretKey

@SpringBootTest
class JwtProviderTest @Autowired constructor(
    private val jwtProvider: JwtProvider,
    private val jwtConfig: JwtConfig
) {

    private val request: HttpServletRequest = mockk()

    @Test
    fun `load context`() {
        assertThat(jwtProvider).isNotNull
    }

    @Test
    fun `create token`() {
        val token = jwtProvider.createToken("username", listOf(UserRole.USER))
        assertThat(token).isNotEmpty
    }

    @Test
    @WithMockCalendarUser
    fun `get authentication from token`() {
        val mockPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val mockCalendarUserDetailsServiceImpl = mockk<CalendarUserDetailsServiceImpl>()
        every { mockCalendarUserDetailsServiceImpl.loadUserByUsername(any()) } returns mockPrincipal

        val testJwtProvider = JwtProvider(jwtConfig, mockCalendarUserDetailsServiceImpl)
        val token = jwtProvider.createToken(mockPrincipal.username, mockPrincipal.user.roles.toList())

        mockkStatic(Jwts::class)
        mockkStatic(Keys::class)
        every { Keys.hmacShaKeyFor(any<ByteArray>()) } returns mockk()
        every { Jwts.parser().verifyWith(any<SecretKey>()).build().parseSignedClaims(token) } answers {
            val mockedJwsClaims = mockk<Jws<Claims>>()
            every { mockedJwsClaims.payload.subject } returns mockPrincipal.username

            mockedJwsClaims
        }
        val authentication = testJwtProvider.getAuthentication(token)

        assertAll(
            { assertThat(authentication).isNotNull },
            { assertThat(authentication.principal).isNotNull },
            { assertThat(authentication.authorities).isNotNull },
            { assertThat(authentication.authorities.size).isEqualTo(1) },
            { assertThat(authentication.authorities.first().authority).isEqualTo("ROLE_USER") },
            { assertThat(authentication.principal == mockPrincipal) }
        )
    }

    @Test
    fun `Authorization header 값이 null 일 경우, null 이 반환된다`() {
        every { request.getHeader("Authorization") } returns null

        val token = jwtProvider.resolveToken(request)

        assertThat(token).isNull()
    }

    @Test
    fun `Authorization header 값이 Bearer 로 시작하지 않을 경우, null 이 반환된다`() {
        every { request.getHeader("Authorization") } returns "start "

        val token = jwtProvider.resolveToken(request)

        assertThat(token).isNull()
    }

    @Test
    fun `Authorization header 값에 token 값을 반환한다`() {
        every { request.getHeader("Authorization") } returns "Bearer token"

        val token = jwtProvider.resolveToken(request)

        assertThat(token).isEqualTo("token")
    }

    @Test
    fun `validateToken should return true for valid token`() {
        mockkStatic(Jwts::class)

        // JWT 토큰 파싱 로직 모킹
        val validToken = "valid.token.string"
        mockJwtsParser(validToken, true)

        val result = jwtProvider.validateToken(validToken)
        assertThat(result).isTrue()
    }

    @Test
    fun `validateToken should return false for invalid token`() {
        mockkStatic(Jwts::class)

        val invalidToken = "invalid.token.string"
        mockJwtsParser(invalidToken, false)

        val result = jwtProvider.validateToken(invalidToken)
        assertThat(result).isFalse()
    }

    @Test
    fun `validateToken should return false for expired token`() {
        mockkStatic(Jwts::class)

        val expiredToken = "expired.token.string"
        mockJwtsParser(expiredToken, isValid = true, isExpired = true)

        val result = jwtProvider.validateToken(expiredToken)
        assertThat(result).isFalse()
    }

    companion object {
        private fun mockJwtsParser(token: String, isValid: Boolean, isExpired: Boolean = false) {
            every { Jwts.parser().verifyWith(any<SecretKey>()).build().parseSignedClaims(token) } answers {
                if (isValid) {
                    val mockedJwsClaims = mockk<Jws<Claims>>()
                    val mockedClaims = mockk<Claims>()
                    every { mockedJwsClaims.payload } returns mockedClaims
                    every { mockedClaims.expiration } answers {
                        if (isExpired) {
                            Date(System.currentTimeMillis() - 10000)
                        } else {
                            Date(System.currentTimeMillis() + 10000)
                        }
                    }

                    mockedJwsClaims
                } else {
                    // 유효하지 않은 토큰에 대한 처리, 예외를 발생시킴
                    throw Exception("Invalid Token")
                }
            }
        }
    }
}
