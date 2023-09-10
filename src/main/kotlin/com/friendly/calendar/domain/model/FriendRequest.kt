package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.BaseEntity
import com.friendly.calendar.domain.model.enum.FriendLogStatus
import javax.persistence.*

@Entity
class FriendRequest(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "req_key")
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_key")
    val sender: User,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_key")
    val receiver: User,

    @Enumerated(EnumType.STRING)
    val status: FriendLogStatus,

    @Column(length = 100)
    val message: String
) : BaseEntity()
