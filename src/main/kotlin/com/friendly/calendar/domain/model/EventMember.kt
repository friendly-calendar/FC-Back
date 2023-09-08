package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.LastModifiedLogEntity
import com.friendly.calendar.domain.model.enum.Status
import javax.persistence.*

@Entity
class EventMember(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_key")
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    val status: Status,

    @OneToOne
    @JoinColumn(name = "user_key")
    val invitedUser: User,

) : LastModifiedLogEntity()
