package com.friendly.calendar.config

import com.friendly.calendar.domain.persistence.CalendarUserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CalendarDataInitializerTest @Autowired constructor(
    private val adminConfig: AdminConfig,
    private val userRepository: CalendarUserRepository,
) {

    @Test
    fun `Exists admin user`() {
        val adminUser = userRepository.findByUsername(adminConfig.username)

        assertAll(
            { assertThat(adminUser).isNotNull },
            { assertThat(adminUser?.username).isEqualTo(adminConfig.username) },
            { assertThat(adminUser?.name).isEqualTo(adminConfig.name) },
            { assertThat(adminUser?.roleAdmin).isTrue() }
        )
    }
}
