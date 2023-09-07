package com.friendly.calendar.network.enum

enum class ErrorCode(val code: Int, val description: String) {
    INVALID_TOKEN(400, "유효하지 않은 토큰입니다."),

    NOT_FOUND(400, "값을 찾을 수 없습니다."),

    NO_PERMISSION(503, "권한이 없습니다."),

    FAIL_LOGIN(400, "로그인 실패"),

    NOT_FOUND_TOKEN(400, "토큰을 찾을 수 없습니다."),

    EXPIRED_TOKEN(400, "만료된 토큰입니다."),

    INVALID_REQUEST_ARGUMENT(400, "요청 인자 유효성 검증 실패"),

    INVALID_TOKEN_TYPE(400, "유효하지 않은 토큰 타입입니다."),

    INTERNAL_SERVER(500, "서버 내부 에러입니다."),
}
