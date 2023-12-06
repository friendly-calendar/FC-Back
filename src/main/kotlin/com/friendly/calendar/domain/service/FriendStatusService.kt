package com.friendly.calendar.domain.service

interface FriendStatusService {
    fun requestFriend(senderId: Long, receiverId: Long, message: String)

    fun acceptFriend(senderId: Long, receiverId: Long)
}
