package com.friendly.calendar.domain.service.impl

import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.service.UserService
import com.friendly.calendar.network.UserSignUpDTO
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val calendarUserRepository: CalendarUserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : UserService {
    override fun createUser(userSignUpDTO: UserSignUpDTO) {
        val (nickname, email, username, password, phoneNumber) = userSignUpDTO

        TODO("Not yet implemented")
    }
}
