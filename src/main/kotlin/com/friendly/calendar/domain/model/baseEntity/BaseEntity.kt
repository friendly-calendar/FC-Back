package com.friendly.calendar.domain.model.baseEntity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

/**
 * HIS 테이블 생성 하는 테이블에서 사용
 */
@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class BaseEntity : java.io.Serializable {

    @CreatedBy
    lateinit var createdBy: String

    @CreatedDate
    lateinit var createdDate: LocalDateTime

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
