package com.friendly.calendar.network.profile

import org.springframework.web.multipart.MultipartFile

class ProfileDto {

    lateinit var userId: String

    lateinit var profileImage: MultipartFile
}
