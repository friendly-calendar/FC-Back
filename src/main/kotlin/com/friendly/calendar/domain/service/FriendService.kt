package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.*
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.domain.persistence.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class FriendService(
    private val friendRelationRepository: FriendRelationRepository,
    private val userRepository: UserRepository
) {
    fun getFriends(userKey: Long): List<FriendRelation> {
        val result: Optional<User> = userRepository.findById(userKey)

        return result.map { friendRelationRepository.findFriendListByUser(it) }
            .orElse(emptyList())
    }

    fun getAcceptFriendRequest(sender: User, receiver: User, message: String): FriendRequest {
        return getFriendRequest(sender, receiver, message, FriendLogStatus.ACCEPT)
    }

    fun getRejectFriendRequest(sender: User, receiver: User, message: String): FriendRequest {
        return getFriendRequest(sender, receiver, message, FriendLogStatus.REJECT)
    }

    fun getFriendRequest(sender: User, receiver: User, message: String): FriendRequest {
        return getFriendRequest(sender, receiver, message, FriendLogStatus.PENDING)
    }

    fun successFriend(sender: User, receiver: User) {
        friendRelationRepository.findByUserAndFriend(sender, receiver)?.apply {
            successFriend()
        } ?: FriendRelation(
            user = sender,
            friend = receiver,
            status = FriendStatus.SUCCESS
        ).let { friendRelationRepository.save(it) }

        friendRelationRepository.findByUserAndFriend(receiver, sender)?.apply {
            successFriend()
        } ?: FriendRelation(
            user = receiver,
            friend = sender,
            status = FriendStatus.SUCCESS
        ).let { friendRelationRepository.save(it) }
    }

    fun blockFriend(sender: User, receiver: User) {
        friendRelationRepository.findByUserAndFriend(sender, receiver)?.apply {
            blockFriend()
        } ?: FriendRelation(
            user = sender,
            friend = receiver,
            status = FriendStatus.BLOCKED
        ).let { friendRelationRepository.save(it) }
    }

    private fun getFriendRequest(sender: User, receiver: User, message: String, status: FriendLogStatus): FriendRequest {
        return FriendRequest(
            sender = sender,
            receiver = receiver,
            message = message,
            status = status
        )
    }
}
