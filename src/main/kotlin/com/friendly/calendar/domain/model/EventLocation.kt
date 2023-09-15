package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.LastModifiedLogEntity
import javax.persistence.*

@Entity
class EventLocation(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "location_key")
    val id: Long = 0,

    var location: String,

    ) : LastModifiedLogEntity()
