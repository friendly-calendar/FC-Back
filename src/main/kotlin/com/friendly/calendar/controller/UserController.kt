package com.friendly.calendar.controller

import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import com.friendly.calendar.network.user.UserSignUpReq
import com.friendly.calendar.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
class UserController(val userService: UserService) {

    //회원가입 실패
    // 1. 입력값검증시 예외
    // 2. 중복아이디, 중복이메일 vv
    // 3. DB 커넥션  (Internal Server Error)
    @PostMapping
    fun signUp(@Validated @RequestBody user: UserSignUpReq): ResponseDto<Any> {
        return try {
            userService.signUp(user)
            ResponseDto.success()
        } catch (e: RuntimeException) {
            ResponseDto.fail(errorCode = ErrorCode.NOT_FOUND)
        }
    }
}