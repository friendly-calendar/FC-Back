package com.friendly.calendar.domain.model.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class BaseEntity : Serializable {

    @CreatedBy
    @Column(updatable = false)
    lateinit var createdBy: String

    @CreatedDate
    @Column(updatable = false)
    lateinit var createdAt: String

    @LastModifiedBy
    lateinit var lastModifiedBy: String

    @LastModifiedDate
    lateinit var lastModifiedAt: String

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
