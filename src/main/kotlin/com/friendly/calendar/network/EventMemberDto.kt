package com.friendly.calendar.network

import com.friendly.calendar.domain.model.enum.EventInvitationStatus

data class EventMemberDto (
    var eventInvitationStatus: EventInvitationStatus?,
    var invitedMembersId: String?
)
