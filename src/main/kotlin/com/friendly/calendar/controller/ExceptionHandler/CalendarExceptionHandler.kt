package com.friendly.calendar.controller.ExcpetionHandler

import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.LoggerFactory
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CalendarExceptionHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(value = [Exception::class, NullPointerException::class])
    fun handle(ex: Exception) : ResponseDto<Any> {
        logger.error("internal server exception : {}", ex.message);
        return ResponseDto.fail(data = null , ErrorCode.INTERNAL_SERVER)
    }

    @ExceptionHandler(value = [ExpiredJwtException::class])
    fun handle(ex: ExpiredJwtException) : ResponseDto<Any> {
        logger.error("expired token exception : {}", ex.message);
        return ResponseDto.fail(data = null , ErrorCode.EXPIRED_TOKEN)
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handle(ex: MethodArgumentNotValidException) : ResponseDto<Any> {
        logger.error("internal server exception : {}", ex.bindingResult.allErrors[0].defaultMessage);
        return ResponseDto.fail(data = null , ErrorCode.INVALID_REQUEST_ARGUMENT)
    }
}

