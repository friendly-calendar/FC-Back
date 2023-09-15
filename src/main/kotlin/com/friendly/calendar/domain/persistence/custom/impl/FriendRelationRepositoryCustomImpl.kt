package com.friendly.calendar.domain.persistence.custom.impl

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.model.FriendStatus
import com.friendly.calendar.domain.model.QFriendRelation.friendRelation
import com.friendly.calendar.domain.model.QUser
import com.friendly.calendar.domain.model.User
import com.friendly.calendar.domain.model.baseEntity.DelFlag
import com.friendly.calendar.domain.persistence.custom.FriendRelationRepositoryCustom
import com.querydsl.jpa.impl.JPAQueryFactory

class FriendRelationRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : FriendRelationRepositoryCustom {
    override fun findFriendListByUser(user: User): List<FriendRelation> {
        return queryFactory
            .selectFrom(friendRelation)
            .leftJoin(friendRelation.user, QUser.user).fetchJoin()
            .leftJoin(friendRelation.friend, QUser.user).fetchJoin()
            .where(
                friendRelation.user.eq(user)
                    .and(friendRelation.delFlag.eq(DelFlag.N))
                    .and(friendRelation.status.eq(FriendStatus.SUCCESS))
            )
            .fetch()
    }
}
