package com.friendly.calendar.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
class JwtAuthenticationFilter(val jwtTokenManager: JwtTokenManager) :GenericFilterBean() {

    private val objectMapper = ObjectMapper()
    private val WHITE_LIST: List<String> = listOf("/api/user/signIn","/api/user/signUp")

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain?) {
        request as HttpServletRequest

        if(isWhiteListUrl(request)){
            chain!!.doFilter(request,response)
            return
        }
        if(!isValidTokenByRequest(request)){
            response as HttpServletResponse

            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()

            val responseDto = ResponseDto.fail(errorCode = ErrorCode.INVALID_TOKEN, data = null)
            val jsonBody: String = objectMapper.writeValueAsString(responseDto)
            response.writer.write(jsonBody)

            return
        }
        chain!!.doFilter(request,response)
    }

    private fun isWhiteListUrl(request: HttpServletRequest) : Boolean = WHITE_LIST.contains(request.requestURI)

    private fun isValidTokenByRequest(request: HttpServletRequest): Boolean {
        val token = request.getHeader("Authorization")?.replace("Bearer ", "")?: throw IllegalArgumentException("Token is not found")
        return jwtTokenManager.isValidAccessToken(token)
    }
}