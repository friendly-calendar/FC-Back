package com.friendly.calendar.validator

import com.friendly.calendar.validator.annotation.AlphaNumericWithSpecialChars
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 *  공통적으로 쓰는 로직이기 때문에 null 일 경우에도 허용하도록 설계
 *  if) null 을 허용하고 싶지 않는 경우 DTO 쪽에서 Non-Nullable Type 으로 선언
 */
class AlphaNumericWithSpecialCharsValidator : ConstraintValidator<AlphaNumericWithSpecialChars, String> {
    private val pattern: Pattern = Pattern.compile("^[a-zA-Z0-9!@#\\\$%^&*()-=_+\\\\[\\\\]{}|;:'\\\",.<>/?]*\$")

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val matcher = value?.let { pattern.matcher(value) }
        return matcher?.matches() ?: true
    }
}
