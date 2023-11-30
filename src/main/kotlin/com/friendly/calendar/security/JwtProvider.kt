package com.friendly.calendar.security

import com.friendly.calendar.config.JwtConfig
import jakarta.annotation.PostConstruct
import org.springframework.security.core.Authentication
import java.util.Base64

class JwtProvider(private val jwtConfig: JwtConfig) {

    private var secretKey: String = ""
    private var expiration: Long = 0L

    @PostConstruct
    private fun init() {
        secretKey = Base64.getEncoder().encodeToString(jwtConfig.secret.toByteArray())
        expiration = jwtConfig.expiration.toLong()
    }

    fun createToken(username: String, roles: List<String>): String {
        TODO()
    }

    fun getAuthentication(token: String): Authentication {
        TODO()
    }

    fun resolveToken(request: String): String? {
        TODO()
    }

    fun validateToken(token: String): Boolean {
        TODO()
    }
}