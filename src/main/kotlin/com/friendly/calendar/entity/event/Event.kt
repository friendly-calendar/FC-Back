package com.friendly.calendar.entity.event

import com.friendly.calendar.entity.baseEntity.BaseEntity
import javax.persistence.*

@Entity
class Event (

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "event_key")
    val id: Long,

    val title: String?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_key")
    val eventDate: EventDate,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_key")
    val eventLocation: EventLocation,

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "event_key")
    val members: List<EventMember> = listOf()

) : BaseEntity()
