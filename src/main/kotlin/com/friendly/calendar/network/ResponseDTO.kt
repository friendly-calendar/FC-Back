package com.friendly.calendar.network

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ResponseDTO(
    val code: Int,
    val description: String,
    val data: Any?,
    val responseTime: String = LocalDateTime.now().toString(),
) {
    companion object {
        fun ok(description: String = "OK", data: Any? = null) = ResponseDTO(HttpStatus.OK.value(), description, data)
        fun error(code: Int, description: String, data: Any? = null) = ResponseDTO(code, description, data)
    }
}
