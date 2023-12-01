package com.friendly.calendar.domain.model

import com.friendly.calendar.enum.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "APP_USER")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = 0L

    var name: String = ""

    @Column(unique = true)
    var email: String? = null

    @Column(unique = true)
    private var username: String = ""

    private var password: String = ""

    @Column(unique = true)
    var phoneNumber: String? = null

    @Enumerated(value = EnumType.STRING)
    var roles: MutableList<UserRole> = mutableListOf(UserRole.USER)
        private set
}
