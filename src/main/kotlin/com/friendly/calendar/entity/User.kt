package com.friendly.calendar.entity

import com.friendly.calendar.entity.enum.DelFlag
import javax.persistence.*

@Entity
@Table(name = "APP_USER")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_key")
    val key: Long = 0,

    val name: String,

    val email: String? = null,

    val id: String,

    val password: String,

    val phoneNumber: String? = null ,

    @Enumerated(EnumType.STRING)
    val delFlag: DelFlag = DelFlag.N

) : BaseEntity() {

    @OneToOne
    var profile: Profile? = null
}
