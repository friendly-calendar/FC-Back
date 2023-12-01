package com.friendly.calendar.validator

import com.friendly.calendar.validator.annotation.Phone
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

/**
 *  공통적으로 쓰는 로직이기 때문에 null 일 경우에도 허용하도록 설계
 *  if) null 을 허용하고 싶지 않는 경우 DTO 쪽에서 Non-Nullable Type 으로 선언
 */
class PhoneValidator : ConstraintValidator<Phone, String> {
    private val pattern: Pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}")

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val matcher = value?.let { pattern.matcher(value) }
        return matcher?.matches() ?: true
    }
}