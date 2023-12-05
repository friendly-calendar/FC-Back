package com.friendly.calendar.network

import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.enum.UserRole

data class UserDTO(
    val id: Long = 0L,
    val username: String = "",
    val roles: List<UserRole> = emptyList(),
    val email: String? = "",
    val name: String? = "",
    val phoneNumber: String? = "",
)

fun CalendarUser.toDto(): UserDTO = UserDTO(
    id = id,
    username = username,
    roles = roles.toList(),
    email = email,
    name = name,
    phoneNumber = phoneNumber
)