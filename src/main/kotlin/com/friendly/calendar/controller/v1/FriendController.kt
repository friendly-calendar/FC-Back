package com.friendly.calendar.controller.v1

import com.friendly.calendar.domain.service.FriendStatusService
import com.friendly.calendar.network.FriendRequestAcceptDTO
import com.friendly.calendar.network.FriendRequestDTO
import com.friendly.calendar.security.session.CalendarPrincipal
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/friends")
class FriendController(
    private val friendStatusService: FriendStatusService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun requestFriend(
        @AuthenticationPrincipal calendarPrincipal: CalendarPrincipal,
        @RequestBody friendRequestDTO: FriendRequestDTO
    ) {
        friendStatusService.requestFriend(
            calendarPrincipal.user.id,
            friendRequestDTO.receiverId,
            friendRequestDTO.message
        )
    }

    @PatchMapping("/accept")
    @ResponseStatus(HttpStatus.OK)
    fun acceptFriend(
        @AuthenticationPrincipal calendarPrincipal: CalendarPrincipal,
        @RequestBody friendRequestAcceptDTO: FriendRequestAcceptDTO
    ) {
        friendStatusService.acceptFriend(
            friendRequestAcceptDTO.senderId,
            calendarPrincipal.user.id
        )
    }
}
