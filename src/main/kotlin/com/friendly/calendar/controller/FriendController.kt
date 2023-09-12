package com.friendly.calendar.controller

import com.friendly.calendar.domain.model.FriendRelation
import com.friendly.calendar.domain.service.FriendService
import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.friend.FriendRequestDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/friend")
class FriendController(private val friendService: FriendService) {

    @GetMapping("/list")
    fun getFriendList(@RequestParam userKey: Long): ResponseDto<Any> {
        val friendList: List<FriendRelation> = friendService.getFriends(userKey);

        return ResponseDto.success(data = friendList)
    }

    @PostMapping
    fun requestFriend(@RequestBody friendRequestDto: FriendRequestDto): String? {
        TODO("not implemented")
    }
}
