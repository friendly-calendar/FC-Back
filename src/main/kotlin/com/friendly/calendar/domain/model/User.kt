package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "APP_USER")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_key")
    val id: Long = 0,

    val name: String,

    val email: String? = null,

    val username: String,

    val password: String,

    val phoneNumber: String? = null,

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    val profile: Profile? = null

) : BaseEntity()
