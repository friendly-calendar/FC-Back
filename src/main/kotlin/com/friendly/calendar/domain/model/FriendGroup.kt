package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.LastModifiedLogEntity
import javax.persistence.*

@Entity
class FriendGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "grp_key")
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "group")
    val groupList: FriendGroupList,

    @Column(length = 40)
    val label: String,

    @Column(length = 1000)
    val description: String,

    val imageUrl: String,
) : LastModifiedLogEntity()
