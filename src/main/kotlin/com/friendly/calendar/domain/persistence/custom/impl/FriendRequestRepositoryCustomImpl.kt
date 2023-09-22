package com.friendly.calendar.domain.persistence.custom.impl

import com.friendly.calendar.domain.model.FriendLogStatus
import com.friendly.calendar.domain.model.FriendRequest
import com.friendly.calendar.domain.model.QFriendRequest.friendRequest
import com.friendly.calendar.domain.model.User
import com.friendly.calendar.domain.model.baseEntity.DelFlag
import com.friendly.calendar.domain.persistence.custom.FriendRequestRepositoryCustom
import com.querydsl.jpa.impl.JPAQueryFactory

class FriendRequestRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : FriendRequestRepositoryCustom {
    override fun existsRequestFriend(sender: User, receiver: User): Boolean {
        val lastRequestLog: FriendRequest = queryFactory.selectFrom(friendRequest)
            .where(
                friendRequest.sender.eq(sender)
                    .and(friendRequest.receiver.eq(receiver))
                    .and(friendRequest.delFlag.eq(DelFlag.N))
            ).orderBy(friendRequest.createdDate.desc()).fetchFirst()

        return lastRequestLog.status == FriendLogStatus.PENDING
    }
}
