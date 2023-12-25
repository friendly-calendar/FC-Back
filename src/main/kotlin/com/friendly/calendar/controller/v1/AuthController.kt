package com.friendly.calendar.controller.v1

import com.friendly.calendar.domain.service.UserService
import com.friendly.calendar.dto.UserSignInDTO
import com.friendly.calendar.dto.utils.ResponseDTO
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController(
    private val userService: UserService
) {

    @PostMapping
    fun signIn(@RequestBody userSignInDTO: UserSignInDTO): ResponseDTO {
        val accessToken = userService.createToken(userSignInDTO)
        val refreshToken = userService.createRefreshToken(userSignInDTO.username)

        return ResponseDTO.ok(data = mapOf("accessToken" to accessToken, "refreshToken" to refreshToken))
    }
}
