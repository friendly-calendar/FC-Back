package com.friendly.calendar.domain.model.base

import com.friendly.calendar.controller.v1.testannotation.WithMockCalendarUser
import com.friendly.calendar.domain.model.CalendarUser
import com.friendly.calendar.domain.persistence.CalendarUserRepository
import com.friendly.calendar.security.session.CalendarPrincipal
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
class BaseEntityTest @Autowired constructor(
    private val calendarUserRepository: CalendarUserRepository
) {

    @Test
    @WithMockCalendarUser
    @Transactional
    fun `baseEntity를 상속 받은 클래스는 생성자를 통해 createdBy, createdAt, delFlag 값을 가져올 수 있다`() {
        val calendarPrincipal = SecurityContextHolder.getContext().authentication.principal as CalendarPrincipal
        val calendarUser: CalendarUser = calendarPrincipal.user

        calendarUserRepository.save(calendarUser)

        assertAll(
            { assertThat(calendarUser.createdAt).isNotNull },
            { assertThat(calendarUser.updatedAt).isNotNull },
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
