package com.friendly.calendar.controller.exceptionhandler

import com.friendly.calendar.dto.utils.ResponseDTO
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class CalendarExceptionHandler {

    @ExceptionHandler(value = [Exception::class, NullPointerException::class])
    fun handle(otherException: Exception): ResponseDTO {
        logger.error(otherException) { HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase }
        return ResponseDTO.error(code = HttpStatus.INTERNAL_SERVER_ERROR.value(), description = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun handle(accessDeniedException: AccessDeniedException): ResponseDTO {
        logger.error(accessDeniedException) { "접근 권한이 없습니다." }
        return ResponseDTO.error(code = HttpStatus.FORBIDDEN.value(), description = HttpStatus.FORBIDDEN.reasonPhrase)
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
