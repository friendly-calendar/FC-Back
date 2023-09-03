package com.friendly.calendar.entity

import com.friendly.calendar.entity.baseEntity.AuditBaseEntity
import javax.persistence.*

@Entity
class Profile(

    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_key")
    val user: User,

    val path: String

) : AuditBaseEntity()
