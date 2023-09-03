package com.friendly.calendar.domain.model

import com.friendly.calendar.entity.baseEntity.BaseEntity
import com.friendly.calendar.entity.User
import com.friendly.calendar.entity.enum.FriendStatus
import javax.persistence.*

@Entity
class FriendRelation (

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "relation_key")
    val relationKey: Long = 0,

    @OneToOne
    @JoinColumn(name = "user_key", unique = true, nullable = false)
    val user: User,

    @OneToOne
    @JoinColumn(name = "friend_key", unique = true, nullable = false)
    val friend: User,

    @Enumerated(EnumType.STRING)
    val status : FriendStatus,

    val friendAlias: String? = null,

) : BaseEntity()
