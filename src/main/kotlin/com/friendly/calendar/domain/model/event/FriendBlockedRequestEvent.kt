package com.friendly.calendar.domain.model.event

import com.friendly.calendar.domain.model.User

data class FriendBlockedRequestEvent(
    override val sender: User,
    override val receiver: User
) : FriendEvent
