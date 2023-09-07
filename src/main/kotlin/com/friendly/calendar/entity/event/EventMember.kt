package com.friendly.calendar.entity.event

import com.friendly.calendar.entity.User
import com.friendly.calendar.entity.baseEntity.AuditBaseEntity
import com.friendly.calendar.entity.enum.Status
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

) : AuditBaseEntity()
