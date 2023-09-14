package com.friendly.calendar.network.friend

data class FriendRequestDto(
    val sender: Long,
    val receiver: Long,
    val message: String,
)
