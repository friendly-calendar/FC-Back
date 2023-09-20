package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.FriendLogStatus
import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.model.FriendRequest
import com.friendly.calendar.domain.model.User
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

    private fun getFriendRequest(sender: User, receiver: User, message: String, status: FriendLogStatus): FriendRequest {
        return FriendRequest(
            sender = sender,
            receiver = receiver,
            message = message,
            status = status
        )
    }
}
