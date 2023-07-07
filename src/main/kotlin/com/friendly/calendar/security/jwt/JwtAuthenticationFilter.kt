package com.friendly.calendar.security.jwt

import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import lombok.extern.slf4j.Slf4j
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Slf4j
class JwtAuthenticationFilter(val jwtTokenManager: JwtTokenManager) :GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        request as HttpServletRequest
        if(!isValidationRequireUrl(request)){
            chain!!.doFilter(request,response)
            return
        }
        if(isValidTokenByRequest(request)){
            ResponseDto.fail(errorCode = ErrorCode.INVALID_TOKEN, data = null)
            return
        }
        chain!!.doFilter(request,response)
    }

    fun isValidationRequireUrl(request: HttpServletRequest?) : Boolean = request!!.requestURI.startsWith("/api") && request.requestURI != "/api/user/signIn"

    fun isValidTokenByRequest(request: HttpServletRequest?): Boolean {
        val token = request!!.getHeader("Authorization")?.replace("Bearer ", "")?: throw IllegalArgumentException("Token is not found")
        return jwtTokenManager.validateToken(token)
    }
}