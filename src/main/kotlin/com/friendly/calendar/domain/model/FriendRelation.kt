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

    groupList: List<FriendGroupList>? = null,

    status: FriendStatus,

    friendAlias: String? = null,

) : BaseEntity() {
    @OneToMany
    @JoinColumn(name = "relation_key")
    var groupList: List<FriendGroupList>? = groupList
        private set

    fun addGroupList(addGroup: FriendGroupList) {
        this.groupList = this.groupList?.plus(addGroup) ?: listOf(addGroup)
    }

    fun removeGroupList(removeGroup: FriendGroupList) {
        this.groupList?.filter { it.id == removeGroup.id }?.apply {
            delete()
        }
    }

    @Enumerated(EnumType.STRING)
    var status: FriendStatus = status
        private set

    fun blockFriend() {
        this.status = FriendStatus.BLOCKED
    }

    fun successFriend() {
        this.status = FriendStatus.SUCCESS
    }

    var friendAlias: String? = friendAlias
        private set
}
