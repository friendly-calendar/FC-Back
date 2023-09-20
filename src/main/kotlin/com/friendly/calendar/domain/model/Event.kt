package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.BaseEntity
import javax.persistence.*

@Entity
class Event(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "event_key")
    val id: Long = 0,

    title: String,
    description: String?,
    eventDate: EventDate?,
    eventLocation: EventLocation?,
    members: List<EventMember?> = listOf()

) : BaseEntity() {
    var title: String = title
        private set

    @Column(length = 4000)
    var description: String? = description
        private set

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "date_key")
    var eventDate: EventDate? = eventDate
        private set

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "location_key")
    var eventLocation: EventLocation? = eventLocation
        private set

    @OneToMany(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "event_key")
    var members: List<EventMember?> = members
        private set

    fun update(title: String, description: String?, eventDate: EventDate, eventLocation: EventLocation?, members: List<EventMember>) {
        this.title = title
        this.description = description
        this.eventDate = eventDate
        this.eventLocation = eventLocation
        this.members = members
    }
}
