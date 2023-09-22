package com.friendly.calendar.domain.model.event

import com.friendly.calendar.domain.model.User

data class FriendRequestRejectedEvent(
    val sender: User,
    val receiver: User,
    val isBlock: Boolean
)
