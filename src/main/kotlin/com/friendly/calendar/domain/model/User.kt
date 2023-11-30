package com.friendly.calendar.domain.model

import com.friendly.calendar.enum.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "APP_USER")
class User: UserDetails {
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

    fun addRole(userRole: UserRole) {
        require(!roles.contains(userRole)) { "이미 존재하는 Role은 추가할 수 없습니다." }

        roles.add(userRole)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles.map { SimpleGrantedAuthority(it.value) }.toMutableList()

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}