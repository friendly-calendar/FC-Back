package com.friendly.calendar.domain.service

import com.friendly.calendar.dto.UserDTO

interface FriendService {
    fun requestFriend(senderId: Long, receiverId: Long)

    fun acceptFriend(senderId: Long, receiverId: Long)

    fun rejectFriend(senderId: Long, receiverId: Long, isBlock: Boolean = false)

    fun blockFriend(blockById: Long, blockToId: Long)

    fun getFriendList(userId: Long): List<UserDTO>
}
