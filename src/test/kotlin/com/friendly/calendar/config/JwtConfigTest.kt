package com.friendly.calendar.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtConfigTest @Autowired constructor(private val jwtConfig: JwtConfig) {

    @Test
    fun `load context`() {
        assertThat(jwtConfig).isNotNull()
    }

    @Test
    fun `secret is not empty`() {
        assertThat(jwtConfig.secret).isNotEmpty()
    }

    @Test
    fun `expiration is not empty`() {
        assertThat(jwtConfig.expiration).isNotEmpty()
    }
}