package com.friendly.calendar.domain.service

import com.friendly.calendar.network.UserSignUpDTO

interface UserService {
    fun createUser(userSignUpDTO: UserSignUpDTO)
}
