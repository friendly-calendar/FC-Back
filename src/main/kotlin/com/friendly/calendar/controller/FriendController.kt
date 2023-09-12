package com.friendly.calendar.controller

import com.friendly.calendar.network.ResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/friend")
class FriendController {

    @GetMapping("/list")
    fun getFriendList(): ResponseDto<Any> {
        return ResponseDto.success(data = null)
    }
}
