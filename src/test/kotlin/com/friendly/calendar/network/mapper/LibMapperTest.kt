package com.friendly.calendar.network.mapper

import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.network.UserSignUpDTO
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class LibMapperTest {

    @Test
    fun map() {
        val validUserSignUpDTO = UserSignUpDTO(
            name = "dokdogal",
            email = "dokdogalmaegi@gmail.com",
            username = "dokdogalmaegi",
            password = "password123!",
            phoneNumber = "010-1234-5678"
        )

        val modelMapper = ModelMapper()
        val calendarUser = modelMapper.map(validUserSignUpDTO, CalendarUser::class.java)
        Assertions.assertThat(calendarUser.username).isEqualTo("dokdogalmaegi")
        Assertions.assertThat(calendarUser?.name).isEqualTo("dokdogal")
        Assertions.assertThat(calendarUser?.email).isEqualTo("dokdogalmaegi@gmail.com")
        Assertions.assertThat(calendarUser?.password).isEqualTo("password123!")
        Assertions.assertThat(calendarUser?.phoneNumber).isEqualTo("010-1234-5678")
    }
}