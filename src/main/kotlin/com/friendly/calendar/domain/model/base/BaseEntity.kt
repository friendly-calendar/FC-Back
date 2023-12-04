package com.friendly.calendar.domain.model.base

import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class BaseEntity: Serializable {

    @CreatedBy
    lateinit var createdBy: String

    @CreatedDate
    lateinit var createdAt: String

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