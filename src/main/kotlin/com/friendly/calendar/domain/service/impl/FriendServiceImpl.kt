package com.friendly.calendar.domain.service.impl

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.domain.service.FriendService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FriendServiceImpl(
    private val calendarUserRepository: CalendarUserRepository,
    private val friendRelationRepository: FriendRelationRepository
) : FriendService {
    override fun requestFriend(senderId: Long, receiverId: Long) {
        val sender = calendarUserRepository.findByIdOrNull(senderId)
        val receiver = calendarUserRepository.findByIdOrNull(receiverId)

        require(sender != null && receiver != null) {
            "User not found"
        }

        friendRelationRepository.save(FriendRelation(sender, receiver))
    }

    override fun acceptFriend(senderId: Long, receiverId: Long) {
        TODO("Not yet implemented")
    }
}
