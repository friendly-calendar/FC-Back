package com.friendly.calendar.controller.exceptionhandler

import com.friendly.calendar.network.ResponseDTO
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class CalendarExceptionHandler {

    @ExceptionHandler(value = [Exception::class, NullPointerException::class])
    fun handle(otherException: Exception): ResponseDTO {
        logger.error(otherException) { "Internal server exception" }
        return ResponseDTO.error(code = HttpStatus.INTERNAL_SERVER_ERROR.value(), description = "Internal server error")
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun handle(illegalArgumentException: IllegalArgumentException): ResponseDTO {
        val message = illegalArgumentException.message ?: "invalid arguments"

        logger.error(illegalArgumentException) { message }
        return ResponseDTO.error(code = HttpStatus.BAD_REQUEST.value(), description = message)
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(methodArgumentNotValidException: MethodArgumentNotValidException): ResponseDTO {
        val fieldErrors = methodArgumentNotValidException.bindingResult.fieldErrors
        logger.error(methodArgumentNotValidException) { "Invalid arguments $fieldErrors" }

        val errors = fieldErrors.joinToString("\n") { it.defaultMessage ?: "" }
        return ResponseDTO.error(code = HttpStatus.BAD_REQUEST.value(), description = errors)
    }

    @ExceptionHandler(value = [BadCredentialsException::class])
    fun handle(badCredentialsException: BadCredentialsException): ResponseDTO {
        logger.error(badCredentialsException) { "Invalid user" }
        return ResponseDTO.error(code = HttpStatus.UNAUTHORIZED.value(), description = "Invalid username or password")
    }
}
