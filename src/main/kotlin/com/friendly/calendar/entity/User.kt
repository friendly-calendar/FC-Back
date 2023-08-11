package com.friendly.calendar.entity

import com.friendly.calendar.entity.baseEntity.AuditBaseEntity
import com.friendly.calendar.entity.baseEntity.BaseEntity
import org.hibernate.envers.Audited
import javax.persistence.*

@Entity
@Table(name = "APP_USER")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_key")
    val userKey: Long = 0,

    val name: String,

    val email: String? = null,

    val id: String,

    val password: String,

    val phoneNumber: String? = null,

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    val profile: Profile? = null

) : BaseEntity()
