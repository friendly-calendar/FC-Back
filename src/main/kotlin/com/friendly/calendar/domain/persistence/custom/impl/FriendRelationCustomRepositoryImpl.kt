package com.friendly.calendar.domain.persistence.custom.impl

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.model.FriendStatus.BLOCKED
import com.friendly.calendar.domain.model.FriendStatus.PENDING
import com.friendly.calendar.domain.model.QCalendarUser
import com.friendly.calendar.domain.model.QFriendRelation.friendRelation
import com.friendly.calendar.domain.model.base.DelFlag
import com.friendly.calendar.domain.persistence.custom.FriendRelationCustomRepository
import com.friendly.calendar.dto.domain.FriendDTO.FriendReturnDTO
import com.friendly.calendar.dto.domain.toFriendDto
import com.querydsl.jpa.impl.JPAQueryFactory

class FriendRelationCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : FriendRelationCustomRepository {
    override fun findPendingRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation? {
        val userEntity = QCalendarUser("userEntity")
        val friendEntity = QCalendarUser("friendEntity")

        return queryFactory
            .from(friendRelation)
            .innerJoin(friendRelation.user, userEntity)
            .innerJoin(friendRelation.friend, friendEntity)
            .where(
                friendRelation.delFlag.eq(DelFlag.N)
                    .and(friendRelation.status.eq(PENDING))
                    .and(friendRelation.user.id.eq(userId))
                    .and(friendRelation.friend.id.eq(friendId))
            )
            .fetchOne() as FriendRelation? ?: return null
    }

    override fun findBlockedRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation? {
        val userEntity = QCalendarUser("userEntity")
        val friendEntity = QCalendarUser("friendEntity")

        return queryFactory
            .from(friendRelation)
            .innerJoin(friendRelation.user, userEntity)
            .innerJoin(friendRelation.friend, friendEntity)
            .where(
                friendRelation.delFlag.eq(DelFlag.N)
                    .and(friendRelation.status.eq(BLOCKED))
                    .and(friendRelation.user.id.eq(userId))
                    .and(friendRelation.friend.id.eq(friendId))
            )
            .fetchOne() as FriendRelation? ?: return null
    }

    override fun findFriendRelationByUserIdAndFriendId(userId: Long, friendId: Long): FriendRelation? {
        val userEntity = QCalendarUser("userEntity")
        val friendEntity = QCalendarUser("friendEntity")

        return queryFactory
            .from(friendRelation)
            .innerJoin(friendRelation.user, userEntity)
            .innerJoin(friendRelation.friend, friendEntity)
            .where(
                friendRelation.delFlag.eq(DelFlag.N)
                    .and(friendRelation.user.id.eq(userId))
                    .and(friendRelation.friend.id.eq(friendId))
            )
            .fetchOne() as FriendRelation? ?: return null
    }

    override fun findFriendListByUserId(userId: Long): List<FriendReturnDTO> {
        val userEntity = QCalendarUser("userEntity")
        val friendEntity = QCalendarUser("friendEntity")

        val result = queryFactory
            .select(friendRelation)
            .from(friendRelation)
            .innerJoin(friendRelation.user, userEntity)
            .innerJoin(friendRelation.friend, friendEntity)
            .where(
                friendRelation.delFlag.eq(DelFlag.N)
                    .and(friendRelation.user.id.eq(userId))
            ).fetch()

        return result.map(FriendRelation::toFriendDto)
    }
}
