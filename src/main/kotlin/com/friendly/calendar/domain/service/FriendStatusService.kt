package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.FriendRequest
import com.friendly.calendar.domain.model.User
import com.friendly.calendar.domain.model.enum.FriendLogStatus
import com.friendly.calendar.domain.persistence.FriendRequestRepository
import com.friendly.calendar.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class FriendStatusService(
    private val friendRequestRepository: FriendRequestRepository,
    private val userRepository: UserRepository
) {
    fun requestFriend(senderKey: Long, receiverKey: Long, requestMessage: String): Unit {
        val sender: Optional<User> = userRepository.findById(senderKey)
        val receiver: Optional<User> = userRepository.findById(receiverKey)

        val friendRequest: FriendRequest = sender.map { sender ->
            receiver.map { receiver ->
                FriendRequest(
                    sender = sender,
                    receiver = receiver,
                    status = FriendLogStatus.PENDING,
                    message = requestMessage
                )
            }.orElseThrow { IllegalArgumentException("receiver not found") }
        }.orElseThrow { IllegalArgumentException("sender not found") }

        friendRequestRepository.save(friendRequest)
    }

    fun acceptFriend(userKey: Long): String {
        TODO("not implemented")
    }

    fun rejectFriend(userKey: Long): String {
        TODO("not implemented")
    }
}
