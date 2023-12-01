package com.friendly.calendar.domain.model

import com.friendly.calendar.enum.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.Email
import lombok.RequiredArgsConstructor

@Entity
@RequiredArgsConstructor
class CalendarUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = 0L

    var name: String = ""

    @Column(unique = true)
    @Email(regexp = ".+@.+\\..+")
    var email: String? = null

    @Column(unique = true)
    var username: String = ""

    var password: String = ""
        private set

    @Column(unique = true)
    var phoneNumber: String? = null

    var roleAdmin: Boolean = false

    @delegate:Transient
    val roles: Set<UserRole> by lazy {
        buildSet {
            add(UserRole.USER)
            if (roleAdmin) add(UserRole.ADMIN)
        }
    }
}
