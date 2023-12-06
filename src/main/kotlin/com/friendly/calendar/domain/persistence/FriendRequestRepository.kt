package com.friendly.calendar.domain.persistence

import com.friendly.calendar.domain.model.FriendRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FriendRequestRepository : JpaRepository<FriendRequest, Long> {

    fun findBySenderIdAndReceiverId(senderId: Long, receiverId: Long): FriendRequest?
}
