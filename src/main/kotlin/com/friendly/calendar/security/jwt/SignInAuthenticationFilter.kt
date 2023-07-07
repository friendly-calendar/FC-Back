package com.friendly.calendar.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.network.user.UserSignInReq
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SignInAuthenticationFilter(
        private val jwtTokenManager: JwtTokenManager,
        private val authenticationManager: AuthenticationManager
) : AbstractAuthenticationProcessingFilter("/api/user/signIn", authenticationManager) {

    // 2. 로그인을 통해 발행한 token 을 검증하는 validation 로직을 구현

    private val objectMapper = ObjectMapper();
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val loginDto = objectMapper.readValue(request.reader, UserSignInReq::class.java)
        // Authentication
        val authentication  = authenticationManager.authenticate(JwtAuthenticationToken(loginDto.id!!, loginDto.password!!))

        return authentication
    }
}