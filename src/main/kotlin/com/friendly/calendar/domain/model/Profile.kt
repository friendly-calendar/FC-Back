package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.LastModifiedLogEntity
import javax.persistence.*

@Entity
class Profile(

    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_key")
    val user: User,

    val path: String

) : LastModifiedLogEntity()
