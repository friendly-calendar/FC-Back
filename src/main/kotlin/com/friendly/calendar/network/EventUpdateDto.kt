package com.friendly.calendar.network

import java.time.LocalDateTime

data class EventUpdateDto (
    var eventKey: Long,
    var title: String,
    var description: String?,
    var startDate: LocalDateTime,
    var endDate: LocalDateTime,
    var location: String?,
    var eventMemberDto: List<EventMemberDto>?
)
