package com.friendly.calendar.domain.model.event

import com.friendly.calendar.domain.model.User

interface FriendEvent {
    val sender: User
    val receiver: User
}
