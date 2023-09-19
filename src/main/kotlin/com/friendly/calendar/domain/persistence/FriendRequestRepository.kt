package com.friendly.calendar.domain.persistence

import com.friendly.calendar.domain.model.FriendRequest
import com.friendly.calendar.domain.persistence.custom.FriendRequestRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FriendRequestRepository : JpaRepository<FriendRequest, Long>, FriendRequestRepositoryCustom
