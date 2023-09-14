package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.User
import com.friendly.calendar.network.UserSignUpReq
import com.friendly.calendar.domain.persistence.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class UserService(val userRepository: UserRepository) {

    fun signUp(user: UserSignUpReq): Long {
        val isExistUser = userRepository.existsByEmailOrPhoneNumberOrUsername(
            email = user.email!!,
            phoneNumber = user.phoneNumber,
            username = user.username
        )
        if (isExistUser) {
            throw RuntimeException()
        }
        val salt = BCrypt.gensalt()
        val hashPw = BCrypt.hashpw(user.password, salt)

        return userRepository.save(
            User(
                username = user.username,
                password = hashPw,
                phoneNumber = user.phoneNumber,
                email = user.email,
                name = user.nickName ?: getDefaultNickName(),
            )
        ).id
    }

    // TODO
    fun getDefaultNickName(): String {
        val DEFAULT_NAME_LIST: List<String> = listOf("김강호")
        return DEFAULT_NAME_LIST[0]
    }
}
