package com.friendly.calendar.controller

import com.friendly.calendar.network.ResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LiveController {

    @GetMapping("/alive")
    fun healthCheck() :ResponseDto<String> {
        return ResponseDto.success(description = "Server Enable");
    }
}