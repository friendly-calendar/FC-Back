package com.friendly.calendar.controller.v1

import com.friendly.calendar.domain.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController(
    private val userService: UserService
) {

    @PostMapping
    fun signIn() {
        TODO("Not yet implemented")
    }
}
