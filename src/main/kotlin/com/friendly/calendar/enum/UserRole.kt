package com.friendly.calendar.enum

import lombok.AllArgsConstructor
import lombok.Getter

@AllArgsConstructor
@Getter
enum class UserRole(val value: String) {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN")
}
