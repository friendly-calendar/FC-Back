package com.friendly.calendar.security.jwt

import com.friendly.calendar.exception.UnexpectedTokenTypeException

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

import java.util.Date

private const val REFRESH_TOKEN = "RefreshToken"
private const val ACCESS_TOKEN = "AccessToken"

@Component
class JwtTokenManager(
    private val jwtConfig: JwtConfig
) {
    private val secretKey = jwtConfig.secret
    private val expirationTime: Int = jwtConfig.expiredTime.toInt()

    fun generateRefreshToken(username: String) = this.generateToken(username, expirationTime * 7)

    fun generateAccessToken(username: String) = this.generateToken(username)

    fun generateNewAccessToken(token: String): String = generateAccessToken(getUsernameFromToken(token))

    private fun generateToken(username: String, expiredTime: Int = expirationTime,
                              tokenIdentity: String? = if (expiredTime == expirationTime) ACCESS_TOKEN else REFRESH_TOKEN): String {
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

    private fun getClaimsFromToken(token: String) = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body

    fun isAccessTokenValid(token: String): Boolean {
        return try {
            val claims: Claims = getClaimsFromToken(token)

            claims.id == ACCESS_TOKEN
        } catch (expiredException: ExpiredJwtException) {
            throw expiredException
        } catch (e: Exception) {
            false
        }
    }

    fun isRefreshTokenValid(token: String): Boolean {
        return try {
            val claims: Claims = getClaimsFromToken(token)

            claims.id == REFRESH_TOKEN
        } catch (expiredException: ExpiredJwtException) {
            throw expiredException
        } catch (e: Exception) {
            false
        }
    }

    fun getUsernameFromToken(token: String): String {
        return try {
            val claims: Claims = getClaimsFromToken(token)

            claims.subject
        } catch (e: Exception) {
            ""
        }
    }
}
