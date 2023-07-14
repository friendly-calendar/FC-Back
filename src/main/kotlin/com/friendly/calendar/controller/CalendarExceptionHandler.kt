package com.friendly.calendar.controller

import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CalendarExceptionHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(value = [Exception::class, NullPointerException::class])
    fun handle(ex: Exception) : ResponseDto<Any> {
        logger.error("internal server exception : {}", ex);
        return ResponseDto.fail(data = null , ErrorCode.INTERNAL_SERVER)
    }

}