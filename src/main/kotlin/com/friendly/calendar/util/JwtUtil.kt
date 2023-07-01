package com.friendly.calendar.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Claims
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails

import java.util.Date

class JwtUtil {
    private val secretKey = "김임김강준찬호준수"
    private val expirationTime: Int = 86400

    fun generateToken(authentication: Authentication): String {
        val userPrincipal: UserDetails = authentication.principal as UserDetails

        val expirationDate = Date(System.currentTimeMillis() + expirationTime)

        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims: Claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body

            !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getUserKeyFromToken(token: String): String? {
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