package com.friendly.calendar.domain.persistence.custom

import com.friendly.calendar.domain.model.FriendRelation

interface FriendRelationCustomRepository {
    fun findPendingRelationBetweenUserAndFriend(userId: Long, friendId: Long): List<FriendRelation>
}
