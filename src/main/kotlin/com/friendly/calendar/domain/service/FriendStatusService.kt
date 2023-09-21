package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.FriendRequest
import com.friendly.calendar.domain.model.User
import com.friendly.calendar.domain.model.event.FriendRequestAcceptedEvent
import com.friendly.calendar.domain.model.event.FriendRequestRejectedEvent
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

//        if (friendRelationRepository.isBlockedRelation(sender, receiver)) {
//            throw IllegalArgumentException("You are blocked by this user.")
//        }
//
//        if (friendRelationRepository.isFriendRelation(sender, receiver)) {
//            throw IllegalArgumentException("You are already friend with this user.")
//        }

        val friendRequest: FriendRequest = friendService.getFriendRequest(sender, receiver, requestMessage)

        friendRequestRepository.save(friendRequest)
    }

    fun acceptFriend(senderKey: Long, receiverKey: Long, acceptMessage: String) {
        val sender: User = userService.findUserById(senderKey)
        val receiver: User = userService.findUserById(receiverKey)

        if (friendRelationRepository.isFriendRelation(sender, receiver)) {
            throw IllegalArgumentException("You are already friend with this user.")
        }

        if (!friendRequestRepository.existsRequestFriend(sender, receiver)) {
            throw IllegalArgumentException("There is no friend request from this user.")
        }

        val acceptFriendRequest: FriendRequest = friendService.getAcceptFriendRequest(sender, receiver, acceptMessage)

        friendRequestRepository.save(acceptFriendRequest)

        applicationEventPublisher.publishEvent(FriendRequestAcceptedEvent(sender, receiver))
    }

    fun rejectFriend(senderKey: Long, receiverKey: Long, rejectMessage: String, isBlock: Boolean) {
        val sender: User = userService.findUserById(senderKey)
        val receiver: User = userService.findUserById(receiverKey)
        val rejectFriendRequest: FriendRequest = friendService.getRejectFriendRequest(sender, receiver, rejectMessage)

        friendRequestRepository.save(rejectFriendRequest)

        applicationEventPublisher.publishEvent(FriendRequestRejectedEvent(sender, receiver, isBlock))
    }

    fun blockFriend(sender: User, receiver: User) {
        if (friendRelationRepository.isBlockedRelation(sender, receiver)) {
            throw IllegalArgumentException("You are already blocked by this user.")
        }


    }
}
