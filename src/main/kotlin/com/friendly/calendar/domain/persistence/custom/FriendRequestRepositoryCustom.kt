package com.friendly.calendar.domain.persistence.custom

interface FriendRequestRepositoryCustom {
    fun existsRequestFriend(userKey: Long, friendKey: Long): Boolean
}
