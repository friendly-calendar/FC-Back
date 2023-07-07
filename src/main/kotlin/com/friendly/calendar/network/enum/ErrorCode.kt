package com.friendly.calendar.network.enum

enum class ErrorCode(val code :Int , val description : String ) {

    INVALID_TOKEN(400, "Token 이 없습니다~~~"),

    NOT_FOUND(400,"실패~~"),

    NO_PERMISSION(503, "예 잘못들었습니다?"),
}