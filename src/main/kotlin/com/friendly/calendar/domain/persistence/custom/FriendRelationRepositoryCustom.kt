package com.friendly.calendar.domain.persistence.custom

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.model.User

interface FriendRelationRepositoryCustom {
    fun findFriendListByUser(userKey: User): List<FriendRelation>
}
