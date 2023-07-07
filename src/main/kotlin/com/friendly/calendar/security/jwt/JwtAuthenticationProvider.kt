package com.friendly.calendar.security.jwt

import org.mindrot.jbcrypt.BCrypt
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService


class JwtAuthenticationProvider(val userDetailService: UserDetailsService , val jwtTokenManager: JwtTokenManager) :AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {

        // db user select
        val foundUser = userDetailService.loadUserByUsername(authentication.name)

        // password validation
        authentication.isAuthenticated = BCrypt.checkpw(authentication.credentials.toString(), foundUser.password)
        if (!authentication.isAuthenticated) {
            throw BadCredentialsException("")
        }

        return JwtAuthenticationToken(authentication.name,
                authentication.credentials.toString(),
                jwtTokenManager.generateToken(authentication.name))

    }

    // authentication
    override fun supports(authentication: Class<*>?): Boolean  =
            JwtAuthenticationToken::class.java == authentication
}