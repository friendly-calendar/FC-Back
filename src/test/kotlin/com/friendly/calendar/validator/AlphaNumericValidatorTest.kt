package com.friendly.calendar.validator

import io.mockk.*
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import org.junit.jupiter.api.Assertions.*
import javax.validation.ConstraintValidatorContext

class AlphaNumericValidatorTest(

) : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        "value 에는 null, 알파벳, 숫자만 들어와야한다."{
            val validator = AlphaNumericValidator()
            val context = mockk<ConstraintValidatorContext>()

            every { context.buildConstraintViolationWithTemplate(any()) } returns mockk()

            val isTrueValid = validator.isValid("abc123", context)
            val isFalseValid = validator.isValid("abc123!@#", context)
            val isNullValid = validator.isValid(null, context)

            assertTrue(isTrueValid)
            assertFalse(isFalseValid)
            assertTrue(isNullValid)
        }
    }
}