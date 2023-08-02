package com.friendly.calendar.entity.baseEntity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity : AuditBaseEntity() {

    @CreatedBy
    @Column(nullable = false, updatable = false)
    val createdBy: String? = null

    @CreatedDate
    @Column(nullable = false, updatable = false)
    val createdDate: LocalDateTime? = null
}
