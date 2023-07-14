package com.friendly.calendar.network.enum

enum class ErrorCode(val code: Int, val description: String) {
    INVALID_TOKEN(400, "유효하지 않은 토큰입니다."),

    NOT_FOUND(400,"값을 찾을 수 없습니다."),

    NO_PERMISSION(503, "권한이 없습니다."),

    FAIL_LOGIN(400, "로그인 실패"),

    INVALID_TOKEN_TYPE(400, "유효하지 않은 토큰 타입입니다."),

    INTERNAL_SERVER(500, "서버 내부 에러입니다."),
}