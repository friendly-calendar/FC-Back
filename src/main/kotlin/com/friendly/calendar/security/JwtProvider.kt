package com.friendly.calendar.security

import com.friendly.calendar.config.JwtConfig
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.Base64
import java.util.Date

private val logger = mu.KotlinLogging.logger {}

@Component
class JwtProvider(private val jwtConfig: JwtConfig, private val userDetailsService: UserDetailsService) {

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
        val parseSignedClaims =
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.toByteArray())).build().parseSignedClaims(token)
        val username = parseSignedClaims.payload.subject

        val userDetails = userDetailsService.loadUserByUsername(username)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val authInfo = request.getHeader("Authorization") ?: return null

        return authInfo.startsWith("Bearer ").let {
            if (!it) null else authInfo.substring(7)
        }
    }

    fun validateToken(token: String): Boolean {
        return try {
            val parseSignedClaims =
                Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.toByteArray())).build().parseSignedClaims(token)

            return parseSignedClaims.payload.expiration.after(Date())
        } catch (otherException: Exception) {
            val errorMessage = when (otherException) {
                is SecurityException, is MalformedJwtException -> "Invalid JWT Token"
                is ExpiredJwtException -> "Expired JWT Token"
                is UnsupportedJwtException -> "Unsupported JWT Token"
                is IllegalArgumentException -> "JWT claims string is empty"
                else -> "validateToken error"
            }

            logger.error(otherException) { errorMessage }
            false
        }
    }
}
