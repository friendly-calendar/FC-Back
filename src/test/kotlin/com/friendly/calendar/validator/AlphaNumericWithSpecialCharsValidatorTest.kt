package com.friendly.calendar.validator

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import javax.validation.ConstraintValidatorContext

class AlphaNumericWithSpecialCharsValidatorTest() : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        "value 에는 null, 알파벳, 숫자, 특수 문자만 들어와야한다." {
            val validator = AlphaNumericWithSpecialCharsValidator()
            val context = mockk<ConstraintValidatorContext>()

            every { context.buildConstraintViolationWithTemplate(any()) } returns mockk()

            val isTrueValid = validator.isValid("abc123!@#!@#", context)
            val isFalseValid = validator.isValid("아앙아ㅏ", context)
            val isNullValid = validator.isValid(null, context)

            assertTrue(isTrueValid)
            assertFalse(isFalseValid)
            assertTrue(isNullValid)
        }
    }
}
