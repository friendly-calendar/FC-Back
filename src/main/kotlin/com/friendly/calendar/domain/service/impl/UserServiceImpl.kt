package com.friendly.calendar.domain.service.impl

import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.service.UserService
import com.friendly.calendar.dto.UserDTO
import com.friendly.calendar.dto.UserSignInDTO
import com.friendly.calendar.dto.UserSignUpDTO
import com.friendly.calendar.dto.mapper.Mapper
import com.friendly.calendar.dto.toDto
import com.friendly.calendar.security.JwtProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val calendarUserRepository: CalendarUserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtProvider: JwtProvider
) : UserService {
    override fun createUser(userSignUpDTO: UserSignUpDTO) {
        val existsUser = calendarUserRepository.findByEmailOrUsernameOrPhoneNumber(
            userSignUpDTO.email,
            userSignUpDTO.username,
            userSignUpDTO.phoneNumber
        )
        require(existsUser == null) {
            "User already exists"
        }

        val encodedUserDTO = userSignUpDTO.copy(
            password = bCryptPasswordEncoder.encode(userSignUpDTO.password)
        )

        val calendarUser = Mapper.map(encodedUserDTO, CalendarUser::class)
        require(calendarUser != null) {
            "Failed to map UserSignUpDTO to CalendarUser"
        }

        calendarUserRepository.save(calendarUser)
    }

    override fun createToken(userSignInDTO: UserSignInDTO): String {
        val findUser = calendarUserRepository.findByUsername(userSignInDTO.username)

        val isUserValid = findUser != null && bCryptPasswordEncoder.matches(userSignInDTO.password, findUser.password)
        if (!isUserValid) {
            throw BadCredentialsException("아이디나 비밀번호를 확인해주세요.")
        }

        return jwtProvider.createToken(findUser!!.username, findUser.roles.toList())
    }

    override fun createRefreshToken(username: String): String {
        val findUser = calendarUserRepository.findByUsername(username)
        require(findUser != null) { "Not exists user" }

        return jwtProvider.createRefreshToken(username)
    }

    @Transactional(readOnly = true)
    override fun getUsers(): List<UserDTO> = calendarUserRepository.findAll().map(CalendarUser::toDto)
}
