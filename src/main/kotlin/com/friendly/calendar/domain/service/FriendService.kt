package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class FriendService(
    private val friendRelationRepository: FriendRelationRepository,
    private val userRepository: UserRepository
) {
    fun getFriends(userId: Long): List<FriendRelation> {
        val user = userRepository.findById(userId).get()

        return friendRelationRepository.findFriendListByUser(user)
    }
}
