package com.friendly.calendar.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class JwtAuthenticationFilter(private val jwtProvider: JwtProvider) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = jwtProvider.resolveToken(request as HttpServletRequest)

        token?.let {
            if (jwtProvider.validateToken(it)) {
                val currentAuthentication = SecurityContextHolder.getContext().authentication

                if (currentAuthentication == null || !currentAuthentication.isAuthenticated) {
                    SecurityContextHolder.getContext().authentication = jwtProvider.getAuthentication(it)
                }
            }
        }

        chain.doFilter(request, response)
    }
}
