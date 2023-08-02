package com.friendly.calendar.entity

import com.friendly.calendar.entity.baseEntity.AuditBaseEntity
import javax.persistence.*

@Entity
class Profile(

    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "userKey")
    val user: User,

    val path: String

) : AuditBaseEntity()
