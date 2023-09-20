package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.baseEntity.BaseEntity
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
    val relationKey: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    val user: User,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_key")
    val friend: User,

    @OneToMany
    @JoinColumn(name = "relation_key")
    val groupList: List<FriendGroupList>? = null,

    @Enumerated(EnumType.STRING)
    val status: FriendStatus,

    val friendAlias: String? = null,

) : BaseEntity()
