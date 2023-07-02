package com.friendly.calendar.validator.annotation

import com.friendly.calendar.validator.AlphaNumericValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AlphaNumericValidator::class])
@MustBeDocumented
annotation class AlphaNumeric(
    val message: String = "알파벳과 숫자만 사용가능합니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
