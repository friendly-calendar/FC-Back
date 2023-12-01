package com.friendly.calendar.network

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UserSignUpDTO(
    @field:Size(min = 2, max = 10, message = "닉네임은 {min} ~ {max} 사이로 입력해주세요.")
    val nickname: String?,

    @field:Email
    val email: String?,

    @field:Size(min = 4, max = 15, message = "아이디는 {min} ~ {max} 사이로 입력해주세요.")
    val username: String,

    @field:Size(min = 9, max = 20, message = "비밀번호는 {min} ~ {max} 사이로 입력해주세요.")
    val password: String,

    val phoneNumber: String?,
)
