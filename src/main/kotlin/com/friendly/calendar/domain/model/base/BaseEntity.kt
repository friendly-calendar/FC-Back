package com.friendly.calendar.domain.model.base

import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.MappedSuperclass
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
@Audited
abstract class BaseEntity : Serializable {
    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    @Enumerated(EnumType.STRING)
    var delFlag: DelFlag = DelFlag.N
        private set

    fun delete() {
        this.delFlag = DelFlag.Y
    }

    fun restore() {
        this.delFlag = DelFlag.N
    }
}
