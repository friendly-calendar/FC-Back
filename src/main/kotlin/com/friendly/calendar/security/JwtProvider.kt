package com.friendly.calendar.security

import com.friendly.calendar.config.JwtConfig
import com.friendly.calendar.enums.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
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
    private var refreshExpiration: Long = 0L

    @PostConstruct
    private fun init() {
        secretKey = Base64.getEncoder().encodeToString(jwtConfig.secret.toByteArray())
        expiration = jwtConfig.expiration.toLong()
        refreshExpiration = jwtConfig.refreshExpiration.toLong()
    }

    fun createToken(username: String, roles: List<UserRole>): String {
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

    fun createRefreshToken(username: String): String {
        val now = Date()

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(Date(now.time + refreshExpiration))
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val parseSignedClaims = parseSignedClaim(token)
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
            val parseSignedClaims = parseSignedClaim(token)

            require(parseSignedClaims.payload.expiration.after(Date())) {
                "Expired token"
            }

            true
        } catch (expiredToken: IllegalArgumentException) {
            throw expiredToken
        } catch (otherException: Exception) {
            val errorMessage = when (otherException) {
                is SecurityException, is MalformedJwtException -> "Invalid JWT Token"
                is UnsupportedJwtException -> "Unsupported JWT Token"
                else -> "validateToken error"
            }

            logger.error(otherException) { errorMessage }
            false
        }
    }

    fun validateRefreshToken(refreshToken: String, accessToken: String): Boolean {
        val accessClaim = parseSignedClaim(accessToken)
        val refreshClaim = parseSignedClaim(refreshToken)

        return validateToken(refreshToken) && accessClaim.payload.subject == refreshClaim.payload.subject
    }

    fun createToken(accessToken: String, refreshToken: String): String {
        require(validateRefreshToken(refreshToken, accessToken)) { "Not valid refresh token" }

        val accessClaims = parseSignedClaim(accessToken)

        return createToken(accessClaims.payload.subject, accessClaims.payload["roles"] as List<UserRole>)
    }

    private fun parseSignedClaim(token: String): Jws<Claims> =
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.toByteArray())).build().parseSignedClaims(token)
}
