package com.friendly.calendar.controller

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.service.FriendService
import com.friendly.calendar.domain.service.FriendStatusService
import com.friendly.calendar.network.FriendRejectDto
import com.friendly.calendar.network.FriendRequestDto
import com.friendly.calendar.network.ResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/friend")
class FriendController(
    private val friendService: FriendService,
    private val friendStatusService: FriendStatusService
) {

    @GetMapping("/list")
    fun getFriendList(@RequestParam userKey: Long): ResponseDto<Any> {
        val friendList: List<FriendRelation> = friendService.getFriends(userKey)

        return ResponseDto.success(data = friendList)
    }

    @PostMapping
    fun requestFriend(@RequestBody friendRequestDto: FriendRequestDto): ResponseDto<Any> {
        friendStatusService.requestFriend(
            senderKey = friendRequestDto.sender,
            receiverKey = friendRequestDto.receiver,
            requestMessage = friendRequestDto.message
        )

        return ResponseDto.success()
    }

    @PostMapping("/accept")
    fun acceptFriend(@RequestBody friendRequestDto: FriendRequestDto): ResponseDto<Any> {
        friendStatusService.acceptFriend(
            senderKey = friendRequestDto.sender,
            receiverKey = friendRequestDto.receiver,
            acceptMessage = friendRequestDto.message
        )

        return ResponseDto.success()
    }

    @PostMapping("/reject")
    fun rejectFriend(@RequestBody friendRejectDto: FriendRejectDto): ResponseDto<Any> {
        friendStatusService.rejectFriend(
            senderKey = friendRejectDto.sender,
            receiverKey = friendRejectDto.receiver,
            rejectMessage = friendRejectDto.message,
            isBlock = friendRejectDto.isBlock
        )

        return ResponseDto.success()
    }
}
