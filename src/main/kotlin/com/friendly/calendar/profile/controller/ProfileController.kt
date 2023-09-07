package com.friendly.calendar.profile.controller

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
class ProfileController {

    @GetMapping
    fun getProfile(): String {
        TODO("not implemented")
    }

    @PostMapping
    fun createOrUpdateProfile(): String {
        TODO("not implemented")
    }

    @DeleteMapping
    fun deleteProfile(): String {
        TODO("not implemented")
    }
}
