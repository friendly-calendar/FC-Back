package com.friendly.calendar.controller.v1

import com.friendly.calendar.domain.service.UserService
import com.friendly.calendar.enums.ADMIN_ROLE
import com.friendly.calendar.network.UserSignUpDTO
import com.friendly.calendar.network.toDto
import com.friendly.calendar.network.utils.ResponseDTO
import com.friendly.calendar.security.session.CalendarPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(
    private val userService: UserService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@Valid @RequestBody userSignUpDTO: UserSignUpDTO) = userService.createUser(userSignUpDTO)

    @GetMapping
    @PreAuthorize("hasRole('$ADMIN_ROLE')")
    fun getUsers() = ResponseDTO.ok(data = userService.getUsers())

    @GetMapping("/me")
    fun getMyInfo(@AuthenticationPrincipal calendarPrincipal: CalendarPrincipal): ResponseDTO =
        ResponseDTO.ok(data = calendarPrincipal.user.toDto())
}
