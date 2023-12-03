package com.friendly.calendar.domain.service

import com.friendly.calendar.network.UserDTO
import com.friendly.calendar.network.UserSignInDTO
import com.friendly.calendar.network.UserSignUpDTO

interface UserService {
    fun createUser(userSignUpDTO: UserSignUpDTO)

    fun createToken(userSignInDTO: UserSignInDTO): String

    fun getUsers(): List<UserDTO>
}
