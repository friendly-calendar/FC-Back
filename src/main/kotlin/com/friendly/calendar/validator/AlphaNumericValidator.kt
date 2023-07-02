package com.friendly.calendar.validator

import com.friendly.calendar.validator.annotation.AlphaNumeric
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class AlphaNumericValidator : ConstraintValidator<AlphaNumeric, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val pattern: Pattern = Pattern.compile("^[a-zA-Z0-9]+\$")
        val matcher = pattern.matcher(value)
        return matcher.matches()
    }
}