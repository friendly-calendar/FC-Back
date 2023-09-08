package com.friendly.calendar.service.auth

import com.friendly.calendar.entity.User
import com.friendly.calendar.repository.UserRepository
import com.friendly.calendar.security.CalendarUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

// 인증에 필요한 사용자 정보를 반환해주는 시큐리티  클래스
@Service
@Transactional
class CalendarUserDetailService(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {

        val selectedUser: Optional<User> = userRepository.findById(username)

        return selectedUser.map { CalendarUserDetails(it.username, it.password) }
            .orElseThrow { throw RuntimeException("user not found") }
    }
}
