package com.friendly.calendar.entity.baseEntity

import com.friendly.calendar.entity.enum.DelFlag
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
abstract class AuditBaseEntity : java.io.Serializable {

    @LastModifiedBy
    var lastModifiedBy: String? = null

    @LastModifiedDate
    var lastModifiedDate: LocalDateTime? = null

    @Enumerated(EnumType.STRING)
    var delFlag: DelFlag = DelFlag.N
}
