package com.friendly.calendar.service.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

// 인증에 필요한 사용자 정보를 반환해주는 시큐리티  클래스
@Service
class CalendarUserDetailService : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }
}