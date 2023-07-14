package com.friendly.calendar.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.network.user.UserSignInReq
import com.friendly.calendar.security.jwt.JwtAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SignInAuthenticationFilter(
        private val authenticationManager: AuthenticationManager
) : AbstractAuthenticationProcessingFilter("/api/user/signIn", authenticationManager) {

    // 2. 로그인을 통해 발행한 token 을 검증하는 validation 로직을 구현
    private val objectMapper = ObjectMapper()

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val ( id: String?, password: String? ) = objectMapper.readValue(request.reader, UserSignInReq::class.java)
        requireNotNull(id) { "id can not be null" }
        requireNotNull(password) { "password can not be null "}
        return authenticationManager.authenticate(JwtAuthenticationToken(id, password))
    }
}