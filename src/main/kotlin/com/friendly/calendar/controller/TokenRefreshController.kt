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

        return try {
            when {
                accessToken.isEmpty() || refreshToken.isEmpty() -> ResponseDto.fail(data = null, ErrorCode.NOT_FOUND_TOKEN)
                jwtTokenManager.isAccessTokenValid(accessToken) && jwtTokenManager.isRefreshTokenValid(refreshToken) -> {
                    val accessTokenUsername: String = jwtTokenManager.getUsernameFromToken(accessToken)
                    val refreshTokenUsername: String = jwtTokenManager.getUsernameFromToken(refreshToken)
                    if (accessTokenUsername != refreshTokenUsername) {
                        throw Exception("Not matched username")
                    }

                    val newAccessToken: String = jwtTokenManager.generateNewAccessToken(accessToken)
                    ResponseDto.success(data = newAccessToken)
                }
                else -> ResponseDto.fail(data = null, ErrorCode.INVALID_TOKEN)
            }
        } catch (expiredTokenException: Exception) {
            ResponseDto.fail(data = null, ErrorCode.EXPIRED_TOKEN)
        } catch (otherException: Exception) {
            ResponseDto.fail(data = null, ErrorCode.INTERNAL_SERVER)
        }
    }
}
