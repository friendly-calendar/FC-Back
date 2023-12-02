package com.friendly.calendar.network.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.modelmapper.ModelMapper

class LibMapperTest {

    private lateinit var fromDTO: PublicTestDTO

    private val modelMapper = ModelMapper()

    private val name = "dokdogal"
    private val email = "dokdogalmaegi@gmail.com"
    private val username = "dokdogalmaegi"
    private val password = "password123!"
    private val phoneNumber = "010-1234-5678"

    @BeforeEach
    fun setUp() {
        fromDTO = PublicTestDTO(
            name,
            email,
            username,
            password,
            phoneNumber
        )
    }

    @Test
    fun `public property를 변환할 수 있다`() {
        val testEntity = modelMapper.map(fromDTO, PublicEntity::class.java)
        assertAll(
            { assertThat(testEntity.username).isEqualTo(username) },
            { assertThat(testEntity.name).isEqualTo(name) },
            { assertThat(testEntity.email).isEqualTo(email) },
            { assertThat(testEntity.password).isEqualTo(password) },
            { assertThat(testEntity.phoneNumber).isEqualTo(phoneNumber) }
        )
    }

    @Test
    fun `private property를 변환할 수 없다`() {
        val testEntity = modelMapper.map(fromDTO, PrivateSetEntity::class.java)
        assertAll(
            { assertThat(testEntity.username).isNotEqualTo(username).isEmpty() },
            { assertThat(testEntity.name).isNotEqualTo(name).isEmpty() },
            { assertThat(testEntity.email).isNotEqualTo(email).isEmpty() },
            { assertThat(testEntity.password).isNotEqualTo(password).isEmpty() },
            { assertThat(testEntity.phoneNumber).isNotEqualTo(phoneNumber).isEmpty() }
        )
    }

    companion object {
        data class PublicTestDTO(
            var name: String,
            var email: String,
            var username: String,
            var password: String,
            var phoneNumber: String
        )

        class PublicEntity() {
            var name: String = ""
            var email: String = ""
            var username: String = ""
            var password: String = ""
            var phoneNumber: String = ""
        }

        class PrivateSetEntity() {
            var name: String = ""
                private set
            var email: String = ""
                private set
            var username: String = ""
                private set
            var password: String = ""
                private set
            var phoneNumber: String = ""
                private set
        }
    }
}