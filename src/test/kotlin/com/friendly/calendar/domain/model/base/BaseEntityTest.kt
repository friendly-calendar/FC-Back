package com.friendly.calendar.domain.model.base

import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.security.session.CalendarPrincipal
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit.jupiter.SpringExtension

@DataJpaTest
@EnableJpaAuditing
@ExtendWith(SpringExtension::class)
class BaseEntityTest @Autowired constructor(
    private val entityManager: EntityManager
) {

    @Test
    @WithMockCalendarUser
    fun `baseEntity를 상속 받은 클래스는 생성자를 통해 createdBy, createdAt, delFlag 값을 가져올 수 있다`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser: CalendarUser = calendarPrincipal.user
        calendarUser.createdBy = calendarUser.username

        entityManager.persist(calendarUser)
        entityManager.flush()

        assertAll(
            { assertThat(calendarUser.createdAt).isNotNull },
            { assertThat(calendarUser.createdBy).isEqualTo(calendarUser.username) },
            { assertThat(calendarUser.delFlag).isEqualTo(DelFlag.N) }
        )
    }

    @Test
    @WithMockCalendarUser
    fun `baseEntity를 상속 받은 클래스는 delete 메소드를 통해 delFlag 값을 Y로 변경할 수 있다`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser: CalendarUser = calendarPrincipal.user

        calendarUser.delete()

        assertThat(calendarUser.delFlag).isEqualTo(DelFlag.Y)
    }

    @Test
    @WithMockCalendarUser
    fun `baseEntity를 상속 받은 클래스는 restore 메소드를 통해 delFlag 값을 N으로 변경할 수 있다`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser: CalendarUser = calendarPrincipal.user

        calendarUser.delete()

        calendarUser.restore()

        assertThat(calendarUser.delFlag).isEqualTo(DelFlag.N)
    }
}