package com.friendly.calendar.controller

import com.friendly.calendar.domain.service.UserService
import com.friendly.calendar.network.UserSignUpDTO
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(
    private val userService: UserService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody userSignUpDTO: UserSignUpDTO) {
        userService.createUser(userSignUpDTO)
    }
}
