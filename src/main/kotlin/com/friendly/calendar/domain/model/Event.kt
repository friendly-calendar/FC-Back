package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.BaseEntity
import javax.persistence.*

@Entity
class Event(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "event_key")
    val id: Long = 0,

    var title: String,

    @Column(length = 4000)
    var description: String?,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "date_key")
    var eventDate: EventDate?,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "location_key")
    var eventLocation: EventLocation?,

    @OneToMany(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "event_key")
    var members: List<EventMember?> = listOf()

) : BaseEntity()
