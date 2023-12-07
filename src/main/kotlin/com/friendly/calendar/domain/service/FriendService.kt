package com.friendly.calendar.domain.service

interface FriendService {
    fun requestFriend(senderId: Long, receiverId: Long)

    fun acceptFriend(senderId: Long, receiverId: Long)
}
