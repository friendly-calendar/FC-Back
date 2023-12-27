package com.friendly.calendar.domain.persistence.custom

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.dto.domain.FriendDTO.FriendReturnDTO

interface FriendRelationCustomRepository {
    fun findPendingRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation?

    fun findBlockedRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation?

    fun findFriendRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation?

    fun findFriendRelationByUseridAndFriendIdWithDeleted(userId: Long, friendId: Long): FriendRelation?

    fun findFriendListByUserId(userId: Long): List<FriendReturnDTO>

    fun findAllByUserId(userId: Long): List<FriendRelation>
}
