package com.friendly.calendar.network.jwt

data class TokenResponse (
    val accessToken :String,
    val refreshToken: String
)