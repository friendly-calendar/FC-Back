package com.friendly.calendar.controller

import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import com.friendly.calendar.security.jwt.JwtTokenManager
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/token")
class TokenRefreshController(private val jwtTokenManager: JwtTokenManager) {

    @PostMapping("/refresh", headers = ["Authorization"])
    fun refreshToken(@RequestBody params: HashMap<String, Object>, @RequestHeader("Authorization") authorization: String): ResponseDto<Any> {
        val accessToken: String = authorization.replace("Bearer ", "")
        val refreshToken: String = params["refreshToken"].toString()

        if (jwtTokenManager.isValidAccessToken(accessToken) && jwtTokenManager.isRefreshTokenValidAndNonExpired(refreshToken)) {
            val username = jwtTokenManager.getUsernameFromToken(accessToken)
            val newAccessToken: String = jwtTokenManager.generateAccessToken(username)

            return ResponseDto.success(data = newAccessToken)
        } else {
            return ResponseDto.fail(data = null, ErrorCode.INVALID_TOKEN)
        }
    }
}
