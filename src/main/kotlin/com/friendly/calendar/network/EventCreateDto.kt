package com.friendly.calendar.network

import java.time.LocalDateTime

data class EventCreateDto(
    var title: String,
    var description: String?,
    var startDate: LocalDateTime,
    var endDate: LocalDateTime,
    var location: String?,
    var invitedMembersId: List<String>?
)
