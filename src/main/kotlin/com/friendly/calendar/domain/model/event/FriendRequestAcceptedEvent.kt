package com.friendly.calendar.domain.model.event

import com.friendly.calendar.domain.model.User

data class FriendRequestAcceptedEvent(
    val sender: User,
    val receiver: User
)
