package com.friendly.calendar.entity

import javax.persistence.*

@Entity
class Profile(
    @Id
    @Column(name = "user_key")
    val key: Long,

    val path: String,

    @OneToOne
    @JoinColumn(name = "user_key")
    val user: User
) : BaseEntity()