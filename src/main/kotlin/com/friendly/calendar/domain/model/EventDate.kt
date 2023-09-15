package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.BaseEntity
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

    var startDate: LocalDateTime,

    var endDate: LocalDateTime

) : BaseEntity()
