package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.LastModifiedLogEntity
import com.friendly.calendar.domain.model.enum.EventInvitationStatus
import javax.persistence.*

@Entity
class EventMember(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_key")
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    val eventInvitationStatus: EventInvitationStatus,

    @OneToOne
    @JoinColumn(name = "user_key")
    val invitedUser: User,

    ) : LastModifiedLogEntity()
