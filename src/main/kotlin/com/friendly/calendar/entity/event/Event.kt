package com.friendly.calendar.entity.event

import com.friendly.calendar.entity.User
import com.friendly.calendar.entity.baseEntity.BaseEntity
import com.friendly.calendar.entity.enum.Status
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Event(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "event_key")
    val id: Long = 0,

    val title: String,

    @Column(length = 4000)
    val description: String?,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "date_key")
    val eventDate: EventDate?,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "location_key")
    val eventLocation: EventLocation?,

    @OneToMany(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "event_key")
    val members: List<EventMember?> = listOf()

) : BaseEntity() {
    constructor(
        title: String,
        description: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        location: String?,
        status: Status?,
        invitedUser: List<User>?
    ) : this(
        title = title,
        description = description,
        eventDate = EventDate(startDate = startDate, endDate = endDate),
        eventLocation = EventLocation(location = location),
        members = invitedUser?.map { EventMember(invitedUser = it, status = status) } ?: emptyList()
    )
}
