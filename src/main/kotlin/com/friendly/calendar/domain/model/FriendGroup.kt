package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.LastModifiedLogEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class FriendGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "grp_key")
    val id: Long = 0,

    @Column(length = 40)
    val label: String,

    @Column(length = 1000)
    val description: String,

    val imageUrl: String,
) : LastModifiedLogEntity()
