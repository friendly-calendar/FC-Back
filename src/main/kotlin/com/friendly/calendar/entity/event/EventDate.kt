package com.friendly.calendar.entity.event

import com.friendly.calendar.entity.baseEntity.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class EventDate(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "date_key")
    val id: Long = 0,

    val startDate: LocalDateTime,

    val endDate: LocalDateTime

) : BaseEntity()
