package com.friendly.calendar.domain.persistence.custom

import com.friendly.calendar.domain.model.FriendRelation

interface FriendRelationCustomRepository {
    fun findPendingRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation?

    fun findFriendRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation?

    fun findFriendRelationByUseridAndFriendIdWithDeleted(userId: Long, friendId: Long): FriendRelation?

    fun findAllByUserId(userId: Long): List<FriendRelation>
}
