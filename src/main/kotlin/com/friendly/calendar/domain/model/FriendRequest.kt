package com.friendly.calendar.domain.model

import com.friendly.calendar.domain.model.base.BaseEntity
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

@Entity

class FriendRequest(sender: CalendarUser, receiver: CalendarUser, message: String) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = 0L

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    var sender: CalendarUser = sender

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    var receiver: CalendarUser = receiver

    @Enumerated(EnumType.STRING)
    var status: FriendLogStatus = FriendLogStatus.PENDING
        private set

    @Column(length = 100)
    var message: String? = message
        private set

    fun accept() {
        this.status = FriendLogStatus.ACCEPT
    }

    fun reject() {
        this.status = FriendLogStatus.REJECT
    }
}
