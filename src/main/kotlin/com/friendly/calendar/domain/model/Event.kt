package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.BaseEntity
import com.friendly.calendar.domain.model.enum.EventInvitationStatus
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
        eventInvitationStatus: EventInvitationStatus?,
        invitedUser: List<User>
    ) : this(
        title = title,
        description = description,
        eventDate = EventDate(startDate = startDate, endDate = endDate),
        eventLocation = location?.let { EventLocation(location = location) },
        members = eventInvitationStatus?.let { invitedUser.map { EventMember(invitedUser = it, eventInvitationStatus = eventInvitationStatus) } } ?: emptyList()
    )
}
