package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.LastModifiedLogEntity
import javax.persistence.*

@Entity
class FriendGroupList(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "grp_list_key")
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    val group: FriendGroup,

    @Column(length = 7)
    val colorCode: String,

    val sequence: Int,
) : LastModifiedLogEntity()
