package com.friendly.calendar.entity.event

import com.friendly.calendar.entity.baseEntity.BaseEntity
import javax.persistence.*

@Entity
class Event (

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "event_key")
    val id: Long = 0,

    val title: String?,

    val description: String?,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "event_key")
    val eventDate: EventDate?,

    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "event_key")
    val eventLocation: EventLocation?,

    @OneToMany(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "event_key")
    val members: List<EventMember?> = listOf()

) : BaseEntity()
