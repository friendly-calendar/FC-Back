package com.friendly.calendar.validator

import com.friendly.calendar.validator.annotation.Phone
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PhoneValidator : ConstraintValidator<Phone, String> {
    private val pattern: Pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}")

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val matcher = pattern.matcher(value)
        return matcher.matches()
    }
}