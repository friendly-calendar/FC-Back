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
    override fun findFriendListByUser(user: User): List<FriendRelation> = queryFactory
        .selectFrom(friendRelation)
        .innerJoin(friendRelation.user, QUser.user).fetchJoin()
        .innerJoin(friendRelation.friend, QUser.user).fetchJoin()
        .where(
            friendRelation.user.eq(user)
                .and(friendRelation.delFlag.eq(DelFlag.N))
                .and(friendRelation.status.eq(FriendStatus.SUCCESS))
        )
        .fetch()

    override fun isBlockedRelation(user: User, friend: User): Boolean =
        queryFactory
            .selectFrom(friendRelation)
            .where(
                friendRelation.user.eq(user)
                    .and(friendRelation.friend.eq(friend))
                    .and(friendRelation.delFlag.eq(DelFlag.N))
                    .and(friendRelation.status.eq(FriendStatus.BLOCKED))
            ).fetchOne() != null

    override fun isFriendRelation(user: User, friend: User): Boolean =
        queryFactory
            .selectFrom(friendRelation)
            .where(
                friendRelation.user.eq(user)
                    .and(friendRelation.friend.eq(friend))
                    .and(friendRelation.delFlag.eq(DelFlag.N))
                    .and(friendRelation.status.eq(FriendStatus.SUCCESS))
            ).fetchOne() != null

    override fun findByUserAndFriend(user: User, friend: User): FriendRelation? =
        queryFactory
            .selectFrom(friendRelation)
            .where(
                friendRelation.user.eq(user)
                    .and(friendRelation.friend.eq(friend))
                    .and(friendRelation.delFlag.eq(DelFlag.N))
            ).fetchOne()
}
