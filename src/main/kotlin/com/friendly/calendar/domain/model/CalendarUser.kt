package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.base.BaseEntity
import com.friendly.calendar.enums.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.validation.constraints.Email
import lombok.AllArgsConstructor
import lombok.RequiredArgsConstructor
import kotlin.jvm.Transient

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
class CalendarUser : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = 0L

    var name: String = ""

    @Column(unique = true)
    @Email
    var email: String? = null

    @Column(unique = true)
    var username: String = ""

    var password: String = ""

    @Column(unique = true)
    var phoneNumber: String? = null

    var roleAdmin: Boolean = false

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    val profile: Profile? = null

    @delegate:Transient
    val roles: Set<UserRole> by lazy {
        buildSet {
            add(UserRole.USER)
            if (roleAdmin) add(UserRole.ADMIN)
        }
    }
}
