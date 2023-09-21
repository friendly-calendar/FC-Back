package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.event.FriendRequestAcceptedEvent
import com.friendly.calendar.domain.model.event.FriendRequestRejectedEvent
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class FriendEventListener(
    private val friendService: FriendService
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun onFriendRequestAcceptedEvent(event: FriendRequestAcceptedEvent) {
        friendService.successFriend(event.sender, event.receiver)
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun onFriendRequestRejectedEvent(event: FriendRequestRejectedEvent) {
        if (event.isBlock) {
            friendService.blockFriend(event.sender, event.receiver)
        }
    }
}
