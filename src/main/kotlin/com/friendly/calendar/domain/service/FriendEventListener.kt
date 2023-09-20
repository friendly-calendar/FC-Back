package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.model.FriendStatus
import com.friendly.calendar.domain.model.event.FriendRequestAcceptedEvent
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class FriendEventListener(
    private val friendRelationRepository: FriendRelationRepository
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleFriendRequestAcceptedEvent(event: FriendRequestAcceptedEvent) {
        val senderFriendRelation = FriendRelation(
            user = event.sender,
            friend = event.receiver,
            status = FriendStatus.SUCCESS
        )
        friendRelationRepository.save(senderFriendRelation)

        val receiverFriendRelation = FriendRelation(
            user = event.receiver,
            friend = event.sender,
            status = FriendStatus.SUCCESS
        )
        friendRelationRepository.save(receiverFriendRelation)
    }
}
