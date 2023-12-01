package com.friendly.calendar.domain.service.impl

import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.service.UserService
import com.friendly.calendar.network.UserSignUpDTO
import com.friendly.calendar.network.mapper.Mapper
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val calendarUserRepository: CalendarUserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : UserService {
    override fun createUser(userSignUpDTO: UserSignUpDTO) {
        val encodedUserDTO = userSignUpDTO.copy(
            password = bCryptPasswordEncoder.encode(userSignUpDTO.password)
        )

        Mapper.map(encodedUserDTO, CalendarUser::class)?.let {
            calendarUserRepository.save(it)
        }
    }
}
