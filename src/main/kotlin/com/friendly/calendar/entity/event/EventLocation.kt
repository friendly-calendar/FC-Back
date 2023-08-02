package com.friendly.calendar.entity.event

import com.friendly.calendar.entity.baseEntity.AuditBaseEntity
import javax.persistence.*

@Entity
class EventLocation (

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "location_key")
    val id: Long,

    val location: String?,

) : AuditBaseEntity()
