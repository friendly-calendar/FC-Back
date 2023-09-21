package com.friendly.calendar.domain.persistence.custom

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.model.User

interface FriendRelationRepositoryCustom {
    fun findFriendListByUser(userKey: User): List<FriendRelation>

    fun isBlockedRelation(user: User, friend: User): Boolean

    fun isFriendRelation(user: User, friend: User): Boolean

    fun findBlockedFriendRelation(sender: User, receiver: User): FriendRelation?
}
