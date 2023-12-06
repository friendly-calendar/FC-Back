package com.friendly.calendar.domain.service.impl

import com.friendly.calendar.domain.model.FriendLogStatus
import com.friendly.calendar.domain.model.FriendRequest
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.persistence.FriendRequestRepository
import com.friendly.calendar.domain.service.FriendStatusService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FriendStatusServiceImpl(
    private val calendarUserRepository: CalendarUserRepository,
    private val friendRequestRepository: FriendRequestRepository
) : FriendStatusService {
    override fun requestFriend(senderId: Long, receiverId: Long, message: String) {
        val sender = calendarUserRepository.findByIdOrNull(senderId)
        val receiver = calendarUserRepository.findByIdOrNull(receiverId)

        require(sender != null && receiver != null) {
            "User not found"
        }

        friendRequestRepository.save(FriendRequest(sender, receiver, message))
    }

    override fun acceptFriend(senderId: Long, receiverId: Long) {
        val friendRequest = friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId)

        require(friendRequest != null) {
            "Friend request not found"
        }

        require(friendRequest.status == FriendLogStatus.PENDING) {
            "Friend request already accepted or rejected"
        }

        friendRequest.accept()
    }
}
