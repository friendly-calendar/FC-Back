package com.friendly.calendar.domain.model.event

import com.friendly.calendar.domain.model.User

data class FriendRequestRejectedEvent(
    override val sender: User,
    override val receiver: User,
    val isBlock: Boolean
) : FriendEvent
