package com.friendly.calendar.domain.service.impl

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
}
