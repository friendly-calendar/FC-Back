package com.friendly.calendar.entity.baseEntity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class BaseEntity : AuditBaseEntity() {

    @CreatedBy
    var createdBy: String? = null

    @CreatedDate
    var createdDate: LocalDateTime? = null
}
