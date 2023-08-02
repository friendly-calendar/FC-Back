package com.friendly.calendar.entity.baseEntity

import com.friendly.calendar.entity.enum.DelFlag
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AuditBaseEntity : java.io.Serializable {

    @LastModifiedBy
    @Column(nullable = false)
    val lastModifiedBy: String? = null

    @LastModifiedDate
    @Column(nullable = false)
    val lastModifiedDate: LocalDateTime? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val delFlag: DelFlag = DelFlag.N
}
