package com.friendly.calendar.network.enum

enum class ErrorCode(val code :Int , val description : String ) {

    NOT_FOUND(400,"실패~~"),

    NO_PERMISSION(503, "예 잘못들었습니다?"),
}