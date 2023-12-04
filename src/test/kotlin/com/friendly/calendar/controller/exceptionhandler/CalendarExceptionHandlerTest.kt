package com.friendly.calendar.controller.exceptionhandler

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("dev")
class CalendarExceptionHandlerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @Test
    fun `알 수 없는 예외가 발생하면 500 에러를 반환한다`() {
        mockMvc.get("/api/v1/auth") {
        }.andExpect {
            status { isOk() }
            jsonPath("$.code") { value(HttpStatus.INTERNAL_SERVER_ERROR.value()) }
            jsonPath("$.description") { value(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase) }
            jsonPath("$.data") { doesNotExist() }
        }
    }
}
