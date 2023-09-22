package com.friendly.calendar.network

data class FriendRejectDto(
    val sender: Long,
    val receiver: Long,
    val message: String,
    val isBlock: Boolean
)
