package com.friendly.calendar.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CalendarAuthFailHandler : AuthenticationFailureHandler{

    private val objectMapper = ObjectMapper()
    override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse, ex: AuthenticationException?) {

        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = HttpStatus.OK.value()

        val responseDto = ResponseDto.fail(null, ErrorCode.FAIL_LOGIN)
        val jsonBody: String = objectMapper.writeValueAsString(responseDto)
        response.writer.write(jsonBody)
    }
}