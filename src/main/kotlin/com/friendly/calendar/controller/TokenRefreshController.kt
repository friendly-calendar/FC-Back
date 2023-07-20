package com.friendly.calendar.controller

import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import com.friendly.calendar.security.jwt.JwtTokenManager
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/token")
class TokenRefreshController(private val jwtTokenManager: JwtTokenManager) {

    @PostMapping("/refresh", headers = ["Authorization"])
    fun refreshToken(@RequestBody params: HashMap<String, Any>, @RequestHeader("Authorization") authorization: String): ResponseDto<Any> {
        val accessToken: String = authorization.replace("Bearer ", "")
        val refreshToken: String = params["refreshToken"].toString()
        if (accessToken.isEmpty() || refreshToken.isEmpty()) {
            return ResponseDto.fail(data = null, ErrorCode.NOT_FOUND_TOKEN)
        } else if (!jwtTokenManager.isAccessTokenValid(accessToken) || !jwtTokenManager.isRefreshTokenValid(refreshToken)) {
            return ResponseDto.fail(data = null, ErrorCode.INVALID_TOKEN)
        }

        val accessTokenUsername: String = jwtTokenManager.getUsernameFromToken(accessToken)
        val refreshTokenUsername: String = jwtTokenManager.getUsernameFromToken(refreshToken)
        if (accessTokenUsername != refreshTokenUsername) {
            throw IllegalArgumentException("Not matched username")
        }

        val newAccessToken: String = jwtTokenManager.generateNewAccessToken(accessToken)
        return ResponseDto.success(data = newAccessToken)
    }
}
