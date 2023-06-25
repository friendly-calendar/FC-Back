package com.friendly.calendar.entity;

import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
open class BaseEntity {

    @CreatedDate
    @Column(name = "input_time", nullable = false , updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
