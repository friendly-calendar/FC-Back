package com.friendly.calendar.domain.service

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.model.User
import com.friendly.calendar.domain.persistence.FriendRelationRepository
import com.friendly.calendar.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class FriendService(
    private val friendRelationRepository: FriendRelationRepository,
    private val userRepository: UserRepository
) {
    fun getFriends(userKey: Long): List<FriendRelation> {
        val result: Optional<User> = userRepository.findById(userKey)

        val user: User? = result.orElse(null)

        if (user != null) {
            return friendRelationRepository.findFriendListByUser(user)
        } else {
            return emptyList()
        }
    }
}
