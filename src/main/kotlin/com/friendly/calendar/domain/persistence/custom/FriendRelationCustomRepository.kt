package com.friendly.calendar.domain.persistence.custom

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.dto.UserDTO

interface FriendRelationCustomRepository {
    fun findPendingRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation?

    fun findBlockedRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation?

    fun findFriendRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation?

    fun findFriendListByUserId(userId: Long): List<UserDTO>
}
