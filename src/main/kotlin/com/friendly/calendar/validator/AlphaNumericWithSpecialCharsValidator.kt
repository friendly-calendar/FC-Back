package com.friendly.calendar.validator

import com.friendly.calendar.validator.annotation.AlphaNumericWithSpecialChars
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class AlphaNumericWithSpecialCharsValidator : ConstraintValidator<AlphaNumericWithSpecialChars, String> {
    private val pattern: Pattern = Pattern.compile("^[a-zA-Z0-9!@#\\\$%^&*()-=_+\\\\[\\\\]{}|;:'\\\",.<>/?]*\$")

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val matcher = pattern.matcher(value)
        return matcher.matches()
    }
}