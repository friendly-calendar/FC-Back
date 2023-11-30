package com.friendly.calendar.security

import com.friendly.calendar.config.JwtConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtProviderTest @Autowired constructor(private val jwtProvider: JwtProvider) {
    @Test
    fun `load context`() {
        assertThat(jwtProvider).isNotNull
    }

    @Test
    fun `create token`() {
        val token = jwtProvider.createToken("username", listOf("ROLE_USER"))
        assertThat(token).isNotEmpty
    }
}