package com.friendly.calendar.network

import com.friendly.calendar.domain.model.enum.EventInvitationStatus
import java.time.LocalDateTime

data class EventDto(
    var title: String,
    var description: String?,
    var startDate: LocalDateTime,
    var endDate: LocalDateTime,
    var location: String?,
    var eventInvitationStatus: EventInvitationStatus?,
    var invitedMembersId: List<String>?
)
