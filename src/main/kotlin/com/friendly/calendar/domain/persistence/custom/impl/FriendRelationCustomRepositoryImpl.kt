package com.friendly.calendar.domain.persistence.custom.impl

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.model.FriendStatus.PENDING
import com.friendly.calendar.domain.model.QCalendarUser.calendarUser
import com.friendly.calendar.domain.model.QFriendRelation.friendRelation
import com.friendly.calendar.domain.model.base.DelFlag
import com.friendly.calendar.domain.persistence.custom.FriendRelationCustomRepository
import com.querydsl.jpa.impl.JPAQueryFactory

class FriendRelationCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : FriendRelationCustomRepository {
    override fun findPendingRelationBetweenUserAndFriend(userId: Long, friendId: Long): List<FriendRelation> = queryFactory
        .selectFrom(friendRelation)
        .innerJoin(friendRelation.user, calendarUser).fetchJoin()
        .innerJoin(friendRelation.friend, calendarUser).fetchJoin()
        .where(
            friendRelation.status.eq(PENDING).and(
                friendRelation.delFlag.eq(DelFlag.N)
            ).and
            (
                friendRelation.user.id.eq(userId)
                    .and(friendRelation.friend.id.eq(friendId))
            ).or(
                friendRelation.user.id.eq(friendId)
                    .and(friendRelation.friend.id.eq(userId))
            )
        )
        .fetch()
}
