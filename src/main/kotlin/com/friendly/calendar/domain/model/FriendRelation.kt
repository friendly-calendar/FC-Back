package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.BaseEntity
import com.friendly.calendar.domain.model.enum.FriendStatus
import javax.persistence.*

@Entity
@Table(
    name = "FRIEND_RELATION",
    uniqueConstraints = [
        UniqueConstraint(name = "FRIEND_RELATION_UNIQUE", columnNames = ["user_key", "friend_key"])
    ]
)
class FriendRelation(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "relation_key")
    val relationKey: Long,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    val user: User,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_key")
    val friend: User,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "relation_key")
    val groupList: List<FriendGroupList>,

    @Enumerated(EnumType.STRING)
    val status: FriendStatus,

    val friendAlias: String? = null,

) : BaseEntity()
