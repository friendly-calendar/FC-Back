package com.friendly.calendar.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.network.ResponseDto
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationSuccess :AuthenticationSuccessHandler{

    private val objectMapper = ObjectMapper()
    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse, authentication: Authentication?) {
        val token = (authentication as JwtAuthenticationToken).getToken()

        response.setHeader("Authorization", "Bearer $token")
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = HttpStatus.OK.value()

        val responseDto = ResponseDto.success("로그인 성공")
        val jsonBody = objectMapper.writeValueAsString(responseDto)
        response.writer.write(jsonBody)
    }

}