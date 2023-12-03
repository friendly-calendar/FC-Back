package com.friendly.calendar.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AdminConfigTest @Autowired constructor(
    private val adminConfig: AdminConfig
) {

    @Test
    fun `load context`() {
        assertNotNull(adminConfig)
    }

    @Test
    fun `username is not empty`() {
        assertNotNull(adminConfig.username)
    }

    @Test
    fun `password is not empty`() {
        assertNotNull(adminConfig.password)
    }

    @Test
    fun `name is not empty`() {
        assertNotNull(adminConfig.name)
    }

}