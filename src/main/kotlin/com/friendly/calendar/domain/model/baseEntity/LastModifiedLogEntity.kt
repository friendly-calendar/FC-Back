package com.friendly.calendar.domain.model.baseEntity

import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

/**
 * HIS 테이블 생성 하지 않는 테이블에서 사용
 */
@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class LastModifiedLogEntity : BaseEntity() {

    @LastModifiedBy
    lateinit var lastModifiedBy: String

    @LastModifiedDate
    lateinit var lastModifiedDate: LocalDateTime
}
