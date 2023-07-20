package com.friendly.calendar.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import io.jsonwebtoken.ExpiredJwtException
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
    private val WHITE_LIST: List<String> = listOf("/api/user/signIn","/api/user/signUp", "/api/token/refresh")

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain?) {
        requireNotNull(chain) { "FilterChain is null" }

        request as HttpServletRequest
        if(isWhiteListUrl(request)) {
            chain.doFilter(request,response)
            return
        }

        val accessToken: String? = request.getHeader("Authorization")?.replace("Bearer ", "")
        val errorCode: ErrorCode? = try {
            when {
                accessToken == null -> ErrorCode.NOT_FOUND_TOKEN
                !jwtTokenManager.isAccessTokenValid(accessToken) -> ErrorCode.INVALID_TOKEN
                else -> null
            }
        } catch (expiredTokenException: ExpiredJwtException) {
            ErrorCode.EXPIRED_TOKEN
        }

        if (errorCode != null) {
            response as HttpServletResponse
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()

            val responseDto = ResponseDto.fail(errorCode = errorCode, data = null)
            val jsonBody: String = objectMapper.writeValueAsString(responseDto)
            response.writer.write(jsonBody)
        }

        chain.doFilter(request,response)
    }

    private fun isWhiteListUrl(request: HttpServletRequest) : Boolean = WHITE_LIST.contains(request.requestURI)
}
