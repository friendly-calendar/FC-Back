package com.friendly.calendar.security

import com.friendly.calendar.config.JwtConfig
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.security.core.Authentication
import java.util.*

private val logger = mu.KotlinLogging.logger {}

class JwtProvider(private val jwtConfig: JwtConfig) {

    private var secretKey: String = ""
    private var expiration: Long = 0L

    @PostConstruct
    private fun init() {
        secretKey = Base64.getEncoder().encodeToString(jwtConfig.secret.toByteArray())
        expiration = jwtConfig.expiration.toLong()
    }

    fun createToken(username: String, roles: List<String>): String {
        logger.info { "create token username: $username, roles: $roles" }
        val now = Date()

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(Date(now.time + expiration))
            .claim("roles", roles)
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .compact()
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