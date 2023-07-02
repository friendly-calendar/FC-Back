package com.friendly.calendar.validator

import com.friendly.calendar.validator.annotation.AlphaNumericWithSpecialChars
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class AlphaNumericWithSpecialCharsValidator : ConstraintValidator<AlphaNumericWithSpecialChars, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val pattern: Pattern = Pattern.compile("^[a-zA-Z0-9!@#\\\$%^&*()-=_+\\\\[\\\\]{}|;:'\\\",.<>/?]*\$")
        val matcher = pattern.matcher(value)
        return matcher.matches()
    }
}