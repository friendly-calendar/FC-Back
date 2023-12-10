package com.friendly.calendar.domain.service.impl

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.model.FriendStatus
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.domain.persistence.util.findByIdOrThrow
import com.friendly.calendar.domain.service.FriendService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendServiceImpl(
    private val calendarUserRepository: CalendarUserRepository,
    private val friendRelationRepository: FriendRelationRepository
) : FriendService {

    override fun requestFriend(senderId: Long, receiverId: Long) {
        val sender = calendarUserRepository.findByIdOrThrow(senderId, "User not found")
        val receiver = calendarUserRepository.findByIdOrThrow(receiverId, "User not found")

        require(canRequestFriend(senderId, receiverId)) {
            "Cannot send friend request to blocked user"
        }

        friendRelationRepository.save(FriendRelation(sender, receiver))
    }

    @Transactional
    override fun acceptFriend(senderId: Long, receiverId: Long) {
        val pendingFriendRelation: FriendRelation? = friendRelationRepository.findPendingRelationByUserIdAndFriendId(senderId, receiverId)

        require(pendingFriendRelation != null) {
            "Friend request not found"
        }

        pendingFriendRelation.accept()
        FriendRelation(pendingFriendRelation.friend, pendingFriendRelation.user).accept()
    }

    @Transactional
    override fun rejectFriend(senderId: Long, receiverId: Long, isBlock: Boolean) {
        val pendingFriendRelation: FriendRelation? = friendRelationRepository.findPendingRelationByUserIdAndFriendId(senderId, receiverId)

        require(pendingFriendRelation != null) {
            "Friend request not found"
        }

        isBlock.takeIf { it }?.let {
            pendingFriendRelation.block(receiverId)

            val friendRelation = friendRelationRepository.findFriendRelationByUserIdAndFriendId(receiverId, senderId)
                ?: FriendRelation(pendingFriendRelation.friend, pendingFriendRelation.user)
            friendRelation.block(receiverId)
            friendRelationRepository.save(friendRelation)
        }
            ?: pendingFriendRelation.reject()
    }

    private fun canRequestFriend(senderId: Long, receiverId: Long): Boolean {
        require(senderId != receiverId) {
            "Cannot send friend request to yourself"
        }

        val friendRelation =
            friendRelationRepository.findFriendRelationByUserIdAndFriendId(senderId, receiverId) ?: return true

        return friendRelation.status == FriendStatus.REJECTED
    }
}
