package com.friendly.calendar.network

data class FriendRequestDto(
    val sender: Long,
    val receiver: Long,
    val message: String,
)
