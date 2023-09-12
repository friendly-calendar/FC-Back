package com.friendly.calendar.repository

import com.friendly.calendar.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmailOrPhoneNumberOrUsername(
        @Param(value = "email") email: String,
        @Param(value = "phoneNumber") phoneNumber: String,
        @Param(value = "username") username: String
    ): Boolean

    fun findByUsername(@Param(value = "username") username: String?): Optional<User>

    override fun findById(@Param(value = "userKey") userKey: Long): Optional<User>
}
