package com.friendly.calendar.network

import com.friendly.calendar.enum.UserRole

data class UserDTO(
    val id: Long,
    val username: String,
    val roles: List<UserRole>,
    val email: String?,
    val name: String?,
    val phoneNumber: String?,
)