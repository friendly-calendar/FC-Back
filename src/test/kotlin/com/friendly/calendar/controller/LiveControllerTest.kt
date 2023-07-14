package com.friendly.calendar.controller

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(LiveController::class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class LiveControllerTes(val mockMvc: MockMvc) : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        "Get Server Health Check Document" {
            val response = mockMvc.perform(get("/alive"))

            response
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.code", `is`(200)))
                .andExpect(jsonPath("$.description", `is`("Server Enable")))
                .andDo(
                    document(
                        "alive/health-check",
                        responseFields(
                            fieldWithPath("code").description("response code"),
                            fieldWithPath("description").description("description about data"),
                            fieldWithPath("data").description("response data"),
                            fieldWithPath("responseTime").description("server response time")
                        )
                    )
                )
        }
    }

}