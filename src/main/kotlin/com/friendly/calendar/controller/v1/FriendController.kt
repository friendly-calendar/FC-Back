package com.friendly.calendar.controller.v1

import com.friendly.calendar.domain.service.FriendService
import com.friendly.calendar.dto.domain.FriendDTO.FriendPatchDTO
import com.friendly.calendar.dto.domain.FriendDTO.FriendRejectDTO
import com.friendly.calendar.dto.domain.FriendDTO.FriendRequestDTO
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
    private val friendService: FriendService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun requestFriend(
        @AuthenticationPrincipal calendarPrincipal: CalendarPrincipal,
        @RequestBody friendRequestDTO: FriendRequestDTO
    ) {
        friendService.requestFriend(
            calendarPrincipal.user.id,
            friendRequestDTO.receiverId,
        )
    }

    @PatchMapping("/accept")
    @ResponseStatus(HttpStatus.OK)
    fun acceptFriend(
        @AuthenticationPrincipal calendarPrincipal: CalendarPrincipal,
        @RequestBody friendRequestPatchDTO: FriendPatchDTO
    ) {
        friendService.acceptFriend(
            friendRequestPatchDTO.senderId,
            calendarPrincipal.user.id
        )
    }

    @PatchMapping("/reject")
    @ResponseStatus(HttpStatus.OK)
    fun rejectFriend(
        @AuthenticationPrincipal calendarPrincipal: CalendarPrincipal,
        @RequestBody friendRejectDTO: FriendRejectDTO
    ) {
        friendService.rejectFriend(
            friendRejectDTO.senderId,
            calendarPrincipal.user.id,
            friendRejectDTO.isBlock
        )
    }

    @PatchMapping("/block")
    @ResponseStatus(HttpStatus.OK)
    fun blockFriend(
        @AuthenticationPrincipal calendarPrincipal: CalendarPrincipal,
        @RequestBody friendPatchDTO: FriendPatchDTO
    ) {
        friendService.blockFriend(
            calendarPrincipal.user.id,
            friendPatchDTO.senderId
        )
    }
}
