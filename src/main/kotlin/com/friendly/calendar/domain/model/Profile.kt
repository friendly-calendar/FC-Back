package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.base.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne

@Entity
class Profile(
    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_key")
    val user: CalendarUser,

    val path: String
) : BaseEntity()
