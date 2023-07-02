package com.friendly.calendar.validator.annotation

import com.friendly.calendar.validator.PhoneValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PhoneValidator::class])
@MustBeDocumented
annotation class Phone(
    val message: String = "휴대폰 번호가 올바르지 않습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
