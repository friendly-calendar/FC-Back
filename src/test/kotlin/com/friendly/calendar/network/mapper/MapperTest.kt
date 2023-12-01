package com.friendly.calendar.network.mapper

import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.network.UserSignUpDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MapperTest {

    @Test
    fun map() {
        val validUserSignUpDTO = UserSignUpDTO(
            name = "dokdogal",
            email = "dokdogalmaegi@gmail.com",
            username = "dokdogalmaegi",
            password = "password123!",
            phoneNumber = "010-1234-5678"
        )

        val calendarUser = Mapper.map(validUserSignUpDTO, CalendarUser::class)
        assertThat(calendarUser?.username).isEqualTo("dokdogalmaegi")
        assertThat(calendarUser?.name).isEqualTo("dokdogal")
        assertThat(calendarUser?.email).isEqualTo("dokdogalmaegi@gmail.com")
        assertThat(calendarUser?.password).isEqualTo("password123!")
        assertThat(calendarUser?.phoneNumber).isEqualTo("010-1234-5678")
    }
}
