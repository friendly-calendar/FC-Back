package com.friendly.calendar.domain.persistence

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.persistence.custom.FriendRelationRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FriendRelationRepository : JpaRepository<FriendRelation, Long>, FriendRelationRepositoryCustom
