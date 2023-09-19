package com.friendly.calendar.domain.persistence.custom

import com.friendly.calendar.domain.model.User

interface FriendRequestRepositoryCustom {
    fun existsRequestFriend(sender: User, receiver: User): Boolean
}
