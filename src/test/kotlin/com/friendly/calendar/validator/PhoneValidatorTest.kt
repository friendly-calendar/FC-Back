package com.friendly.calendar.validator

import com.friendly.calendar.validator.annotation.Phone
import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PhoneValidatorTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `휴대폰 번호 형식이면 유효성 검사를 통과한다`() {
        // given
        val phoneTestDTO = TestDTO("010-1234-5678")

        // when
        val violations = validator.validate(phoneTestDTO)

        // then
        assertThat(violations).isEmpty()
    }

    @ParameterizedTest
    @ValueSource(strings = ["010-1234-567", "010-1234-56789", "010-123-5678", "010-12345678"])
    fun `휴대폰 번호 형식이 아니면 유효성 검사를 통과하지 못한다`(value: String) {
        // given
        val phoneTestDTO = TestDTO(value)

        // when
        val violations = validator.validate(phoneTestDTO)

        // then
        assertThat(violations).isNotEmpty
    }

    companion object {
        private data class TestDTO(
            @Phone
            val value: String
        )
    }
}