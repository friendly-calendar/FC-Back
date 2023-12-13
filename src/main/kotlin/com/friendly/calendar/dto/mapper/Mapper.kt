package com.friendly.calendar.dto.mapper

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

abstract class Mapper {

    companion object {
        fun <T, R : Any> map(from: T, toClass: KClass<R>): R? {
            val instance = toClass.primaryConstructor?.callBy(emptyMap())
                ?: return null
            from!!::class.declaredMemberProperties.forEach { fromProperty ->
                val toProperty = toClass.declaredMemberProperties
                    .firstOrNull { it.name == fromProperty.name }
                    ?: return@forEach

                fromProperty.isAccessible = true
                toProperty.isAccessible = true

                if (toProperty is KMutableProperty<*>) {
                    val value = fromProperty.getter.call(from)
                    (toProperty as KMutableProperty<*>).setter.call(instance, value)
                }
            }
            return instance
        }
    }
}
