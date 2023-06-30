package com.friendly.calendar.network

import com.friendly.calendar.network.enum.ErrorCode
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ResponseDto<T>(
    val code: Int,
    val description: String,
    val data: T?,
    val responseTime: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun <T> success(data: T? = null, description: String = "Success"): ResponseDto<T> =
            ResponseDto(HttpStatus.OK.value(), description, data)

        fun <T> fail(data: T? = null, errorCode: ErrorCode): ResponseDto<T> =
            ResponseDto(errorCode.code, errorCode.description, data)
    }
}