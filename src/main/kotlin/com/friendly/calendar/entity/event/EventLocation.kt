package com.friendly.calendar.entity.event

import com.friendly.calendar.entity.baseEntity.AuditBaseEntity
import org.hibernate.envers.Audited
import javax.persistence.*

@Entity
class EventLocation (

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "location_key")
    val id: Long,

    val location: String?,

) : AuditBaseEntity()
