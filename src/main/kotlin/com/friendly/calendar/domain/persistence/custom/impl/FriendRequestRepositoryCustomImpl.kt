package com.friendly.calendar.domain.persistence.custom.impl

import com.friendly.calendar.domain.persistence.custom.FriendRequestRepositoryCustom
import com.querydsl.jpa.impl.JPAQueryFactory

class FriendRequestRepositoryCustomImpl(
        private val queryFactory: JPAQueryFactory
): FriendRequestRepositoryCustom {
    override fun existsRequestFriend(userKey: Long, friendKey: Long): Boolean {
        TODO("Not yet implemented")
    }
}
