package com.friendly.calendar.domain.service

import com.friendly.calendar.dto.domain.FriendDTO.FriendReturnDTO

interface FriendService {
    fun requestFriend(senderId: Long, receiverId: Long)

    fun acceptFriend(senderId: Long, receiverId: Long)

    fun rejectFriend(senderId: Long, receiverId: Long, isBlock: Boolean = false)

    fun blockFriend(blockById: Long, blockToId: Long)

    fun unblockFriend(unblockById: Long, unblockToId: Long)

    fun getFriendList(userId: Long): List<FriendReturnDTO>

    fun getBlockedList(userId: Long): List<FriendReturnDTO>
}
