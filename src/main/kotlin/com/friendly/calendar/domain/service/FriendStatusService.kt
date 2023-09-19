package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.FriendLogStatus
import com.friendly.calendar.domain.model.FriendRequest
import com.friendly.calendar.domain.model.User
import com.friendly.calendar.domain.model.event.FriendRequestAcceptedEvent
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.domain.persistence.FriendRequestRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FriendStatusService(
    private val friendRequestRepository: FriendRequestRepository,
    private val friendRelationRepository: FriendRelationRepository,
    private val friendService: FriendService,
    private val userService: UserService,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    fun requestFriend(senderKey: Long, receiverKey: Long, requestMessage: String) {
        val sender: User = userService.findUserById(senderKey)
        val receiver: User = userService.findUserById(receiverKey)

        if (friendRelationRepository.isBlockedRelation(sender, receiver)) {
            throw IllegalArgumentException("You are blocked by this user.")
        }

        if (friendRelationRepository.isFriendRelation(sender, receiver)) {
            throw IllegalArgumentException("You are already friend with this user.")
        }

        val friendRequest: FriendRequest = friendService.getFriendRequest(sender, receiver, requestMessage, FriendLogStatus.PENDING)

        friendRequestRepository.save(friendRequest)
    }

    fun acceptFriend(senderKey: Long, receiverKey: Long, acceptMessage: String) {
        val sender: User = userService.findUserById(senderKey)
        val receiver: User = userService.findUserById(receiverKey)

        if (friendRelationRepository.isFriendRelation(sender, receiver)) {
            throw IllegalArgumentException("You are already friend with this user.")
        }

        val friendRequest: FriendRequest = friendService.getFriendRequest(sender, receiver, acceptMessage, FriendLogStatus.ACCEPT)

        friendRequestRepository.save(friendRequest)

        applicationEventPublisher.publishEvent(FriendRequestAcceptedEvent(sender, receiver))
    }

    fun rejectFriend(senderKey: Long, receiverKey: Long, rejectMessage: String) {
        val sender: User = userService.findUserById(senderKey)
        val receiver: User = userService.findUserById(receiverKey)
        val friendRequest: FriendRequest = friendService.getFriendRequest(sender, receiver, rejectMessage, FriendLogStatus.REJECT)

        friendRequestRepository.save(friendRequest)
    }
}
