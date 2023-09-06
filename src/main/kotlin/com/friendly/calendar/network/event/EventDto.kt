package com.friendly.calendar.network.event

import com.friendly.calendar.entity.enum.Status
import java.time.LocalDateTime

data class EventDto (
    var title: String,
    var description: String?,
    var startDate: LocalDateTime?,
    var endDate: LocalDateTime?,
    var location: String?,
    var status: Status?,
    var invitedMembersId: List<String>?
)
