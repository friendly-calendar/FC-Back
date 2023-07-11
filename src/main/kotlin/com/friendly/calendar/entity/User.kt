package com.friendly.calendar.entity

import com.friendly.calendar.entity.enum.DelFlag
import javax.persistence.*

@Entity
@Table(name = "APP_USER")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val userKey: Long = 0,

    val name: String,

    val email: String? = null,

    val id: String,

    val password: String,

    val phoneNumber: String? = null,

    @Enumerated(EnumType.STRING)
    val delFlag: DelFlag = DelFlag.N,

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    val profile: Profile? = null

) : BaseEntity()
