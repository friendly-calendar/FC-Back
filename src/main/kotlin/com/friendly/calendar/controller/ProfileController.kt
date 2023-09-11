package com.friendly.calendar.controller

import com.friendly.calendar.network.ResponseDto
import com.friendly.calendar.network.enum.ErrorCode
import com.friendly.calendar.network.profile.ProfileDto
import com.friendly.calendar.service.FileService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
class ProfileController(private val fileService: FileService) {

    @GetMapping
    fun getProfile(): String {
        TODO("not implemented")
    }

    @PostMapping
    fun createProfile(@ModelAttribute profile: ProfileDto): ResponseDto<String> {
        try {
            fileService.uploadFile("profile/${profile.userId}", profile.profileImage)
        } catch (e: Exception) {
            return ResponseDto.fail(data = null, ErrorCode.FILE_UPLOAD_ERROR)
        }
        return ResponseDto.success("success")
    }

    @DeleteMapping
    fun deleteProfile(): String {
        TODO("not implemented")
    }
}
