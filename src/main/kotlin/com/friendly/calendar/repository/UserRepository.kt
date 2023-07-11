package com.friendly.calendar.repository

import com.friendly.calendar.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun existsByEmailOrPhoneNumberOrId(
        @Param(value = "email") email: String,
        @Param(value = "phoneNumber") phoneNumber: String,
        @Param(value = "id") id: String
    ): Boolean
}
