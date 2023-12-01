package com.friendly.calendar.validator

import com.friendly.calendar.validator.annotation.AlphaNumeric
import jakarta.validation.Validation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AlphaNumericValidatorTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator

    @ParameterizedTest
    @ValueSource(strings = ["test1252", "dokdo0614", "abc123"])
    fun `영문자와 숫자로만 이루어진 문자열이면 유효성 검사를 통과한다`() {
        // given
        val alphaNumericTestDTO = AlphaNumericTestDTO("abc123")

        // when
        val violations = validator.validate(alphaNumericTestDTO)

        // then
        assertThat(violations).isEmpty()
    }

    @ParameterizedTest
    @ValueSource(strings = ["abc", "123", "abc123!"])
    fun `영문자와 숫자로만 이루어지지 않은 문자열이면 유효성 검사를 통과하지 못한다`() {
        // given
        val alphaNumericTestDTO = AlphaNumericTestDTO("abc123!")

        // when
        val violations = validator.validate(alphaNumericTestDTO)

        // then
        assertThat(violations).isNotEmpty
    }

    companion object {
        private data class AlphaNumericTestDTO(
            @AlphaNumeric
            val value: String
        )
    }
}