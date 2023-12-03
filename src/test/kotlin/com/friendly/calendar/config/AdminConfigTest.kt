package com.friendly.calendar.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AdminConfigTest @Autowired constructor(
    private val adminConfig: AdminConfig
) {

    @Test
    fun `load context`() {
        assertThat(adminConfig).isNotNull
    }

    @Test
    fun `username is not empty`() {
        assertThat(adminConfig.username).isNotNull
    }

    @Test
    fun `password is not empty`() {
        assertThat(adminConfig.password).isNotNull
    }

    @Test
    fun `name is not empty`() {
        assertThat(adminConfig.name).isNotNull
    }
}
