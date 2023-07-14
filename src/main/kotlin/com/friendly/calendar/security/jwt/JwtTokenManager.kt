package com.friendly.calendar.security.jwt

import com.friendly.calendar.exception.UnexpectedTokenTypeException

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Claims
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

import java.util.Date

private const val REFRESH_TOKEN = "RefreshToken"

@Component
class JwtTokenManager(
    private val jwtConfig: JwtConfig
) {
    private val secretKey = jwtConfig.secret
    private val expirationTime: Int = jwtConfig.expiredTime.toInt()

    fun generateRefreshToken(username: String) = this.generateToken(username, expirationTime * 7)

    fun generateAccessToken(username: String) = this.generateToken(username)

    private fun generateToken(username: String, expiredTime: Int = expirationTime,
                              tokenIdentity: String? = if (expiredTime == expirationTime) null else REFRESH_TOKEN): String {
        val claims: Claims = Jwts.claims()
                .setSubject(username)
                .setId(tokenIdentity)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + expiredTime))

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

    fun isValidAccessToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body

            if (claims.id == REFRESH_TOKEN) {
                throw UnexpectedTokenTypeException("This is refresh token")
            }

            Date(System.currentTimeMillis()).before(claims.expiration)
        } catch (e: Exception) {
            false
        }
    }

    fun getUsernameFromToken(token: String): String? {
        return try {
            val claims: Claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body

            claims.subject
        } catch (e: Exception) {
            null
        }
    }
}