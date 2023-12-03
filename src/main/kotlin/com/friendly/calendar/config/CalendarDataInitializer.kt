package com.friendly.calendar.config

import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class CalendarDataInitializer(
    private val userRepository: CalendarUserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val adminConfig: AdminConfig
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val adminUser = CalendarUser().apply {
            username = adminConfig.username
            password = bCryptPasswordEncoder.encode(adminConfig.password)
            name = adminConfig.name
            roleAdmin = true
        }
        userRepository.save(adminUser)
    }
}
