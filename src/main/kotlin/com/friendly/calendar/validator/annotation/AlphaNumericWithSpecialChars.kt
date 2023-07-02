package com.friendly.calendar.validator.annotation

import com.friendly.calendar.validator.AlphaNumericWithSpecialCharsValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AlphaNumericWithSpecialCharsValidator::class])
@MustBeDocumented
annotation class AlphaNumericWithSpecialChars(
    val message: String = "알파벳과 숫자와 특수 문자만 사용 가능합니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
