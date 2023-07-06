package com.friendly.calendar.validator

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import javax.validation.ConstraintValidatorContext

class PhoneValidatorTest(
) : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        "value 에는 알맞은 휴대폰 번호 형식이 들어와야한다." {
            val validator = PhoneValidator()
            val context = mockk<ConstraintValidatorContext>()

            every { context.buildConstraintViolationWithTemplate(any()) } returns mockk()

            val isTrueValid = validator.isValid("010-1234-5678", context)
            val isFalseValid = validator.isValid("010-1234-56782", context)

            assertTrue(isTrueValid)
            assertFalse(isFalseValid)
        }
    }
}