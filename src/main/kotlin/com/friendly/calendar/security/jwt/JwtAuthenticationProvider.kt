package com.friendly.calendar.security.jwt

import com.friendly.calendar.network.jwt.TokenResponse
import org.mindrot.jbcrypt.BCrypt
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService

class JwtAuthenticationProvider(private val userDetailService: UserDetailsService, private val jwtTokenManager: JwtTokenManager) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {

        // db user select
        val foundUser = userDetailService.loadUserByUsername(authentication.name)

        // password validation
        authentication.isAuthenticated = BCrypt.checkpw(authentication.credentials.toString(), foundUser.password)
        if (!authentication.isAuthenticated) {
            throw BadCredentialsException("")
        }

        val accessToken = jwtTokenManager.generateAccessToken(authentication.name)
        val refreshToken = jwtTokenManager.generateRefreshToken(authentication.name)

        return JwtAuthenticationToken(
            authentication.name,
            authentication.credentials.toString(),
            TokenResponse(accessToken, refreshToken)
        )
    }

    // authentication
    override fun supports(authentication: Class<*>?): Boolean =
        JwtAuthenticationToken::class.java == authentication
}
