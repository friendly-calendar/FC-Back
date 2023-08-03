package com.friendly.calendar.entity.baseEntity

import com.friendly.calendar.entity.enum.DelFlag
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
abstract class AuditBaseEntity : java.io.Serializable {

    @LastModifiedBy
    lateinit var lastModifiedBy: String

    @LastModifiedDate
    lateinit var lastModifiedDate: LocalDateTime

    @Enumerated(EnumType.STRING)
    lateinit var delFlag: DelFlag
}
