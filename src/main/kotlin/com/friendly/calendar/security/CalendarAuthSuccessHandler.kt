package com.friendly.calendar.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.jwt.TokenResponse
import com.friendly.calendar.security.jwt.JwtAuthenticationToken
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CalendarAuthSuccessHandler : AuthenticationSuccessHandler {

    private val objectMapper = ObjectMapper()
    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse, authentication: Authentication?) {
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = HttpStatus.OK.value()

        val tokenResponse: TokenResponse = requireNotNull((authentication as JwtAuthenticationToken).getToken())
        val responseDto = ResponseDto.success(tokenResponse)
        val jsonBody: String = objectMapper.writeValueAsString(responseDto)
        response.writer.write(jsonBody)
    }
}
