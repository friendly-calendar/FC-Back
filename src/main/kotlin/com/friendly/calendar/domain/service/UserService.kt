package com.friendly.calendar.domain.service

import com.friendly.calendar.dto.UserDTO
import com.friendly.calendar.dto.UserSignInDTO
import com.friendly.calendar.dto.UserSignUpDTO

interface UserService {
    fun createUser(userSignUpDTO: UserSignUpDTO)

    fun createToken(userSignInDTO: UserSignInDTO): String

    fun getUsers(): List<UserDTO>
}
