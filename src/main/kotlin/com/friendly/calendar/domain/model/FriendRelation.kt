package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.base.BaseEntity
import com.friendly.calendar.domain.model.base.DelFlag
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.envers.Audited
import org.hibernate.envers.RelationTargetAuditMode

@Entity
@Table(
    name = "FRIEND_RELATION",
    uniqueConstraints = [
        UniqueConstraint(name = "FRIEND_RELATION_UK", columnNames = ["user_key", "friend_key"])
    ]
)
@Audited
class FriendRelation(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    val user: CalendarUser,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_key")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    val friend: CalendarUser,

    status: FriendStatus = FriendStatus.PENDING,

    @Column(length = 100)
    var friendAlias: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = 0L,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var status: FriendStatus = status
        private set

    private var blockedBy: Long? = null

    fun request() {
        status = FriendStatus.PENDING

        if (delFlag == DelFlag.Y) {
            restore()
        }
    }

    fun accept() {
        status = FriendStatus.ACCEPTED

        if (delFlag == DelFlag.Y) {
            restore()
        }
    }

    fun reject() {
        status = FriendStatus.REJECTED

        if (delFlag == DelFlag.Y) {

        }
    }

    fun block(blockedByUserId: Long) {
        status = FriendStatus.BLOCKED
        blockedBy = blockedByUserId
    }

    fun unBlock(userId: Long) {
        require(blockedBy == userId) {
            "User is not blocked by this user"
        }

        delete()
    }
}
