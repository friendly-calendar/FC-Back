package com.friendly.calendar.validator

import com.friendly.calendar.validator.annotation.Phone
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PhoneValidator : ConstraintValidator<Phone, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val pattern: Pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}")
        val matcher = pattern.matcher(value)
        return matcher.matches()
    }
}