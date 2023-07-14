package com.friendly.calendar.exception

class UnexpectedTokenTypeException: Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}