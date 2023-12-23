package com.friendly.calendar.dto

import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.domain.model.base.DelFlag
import com.friendly.calendar.enums.UserRole

data class UserDTO(
    val id: Long,
    val username: String,
    val roles: List<UserRole>,
    val email: String?,
    val name: String?,
    val phoneNumber: String?,
    val path: String?,
    val introduce: String?,
    val delFlag: DelFlag,
)

fun CalendarUser.toDto(): UserDTO = UserDTO(
    id = id,
    username = username,
    roles = roles.toList(),
    email = email,
    name = name,
    phoneNumber = phoneNumber,
    path = profile?.path,
    introduce = profile?.introduce,
    delFlag = delFlag
)
