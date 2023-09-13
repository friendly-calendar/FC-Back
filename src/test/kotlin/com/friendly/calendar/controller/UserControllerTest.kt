package com.friendly.calendar.controller

import com.friendly.calendar.network.UserSignUpReq
import com.google.gson.Gson
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.transaction.Transactional

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest(
    @Autowired
    val mockMvc: MockMvc,

    @Autowired
    val gson: Gson,
) : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        "phoneNumber 의 형태에서 벗어난 형식으로 작성된다면 BadRequest 를 발생시킨다. " {
            val userSignUpReq: UserSignUpReq = UserSignUpReq(
                nickName = "csGood",
                email = "cs@gmail.com",
                username = "csGood",
                password = "csGoodGoodGood",
                phoneNumber = "010-1111-1111123"
            )

            mockMvc.perform(
                MockMvcRequestBuilders.post("/user")
                    .content(gson.toJson(userSignUpReq))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }

        "nickName 에 특수문자가 입력되면 BadRequest 를 발생시킨다. " {
            val userSignUpReq: UserSignUpReq = UserSignUpReq(
                nickName = "1@$%@#$@#4",
                email = "cs@gmail.com",
                username = "csGood",
                password = "csGoodGoodGood",
                phoneNumber = "010-1111-1111"
            )

            mockMvc.perform(
                MockMvcRequestBuilders.post("/user")
                    .content(gson.toJson(userSignUpReq))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }

        "id or password 에는 알파벳, 숫자, 특수문자가 입력 가능하다." {
            val userSignUpReq: UserSignUpReq = UserSignUpReq(
                nickName = "csKim",
                email = "cs1@gmail.com",
                username = "csCS!@#123",
                password = "csCS!@#1231231231",
                phoneNumber = "010-1111-1112"
            )

            mockMvc.perform(
                MockMvcRequestBuilders.post("/user")
                    .content(gson.toJson(userSignUpReq))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk)
        }

        "email 에는 email 형식외의 String 이 들어오면 BadRequest 를 발생시킨다." {
            val userSignUpReq: UserSignUpReq = UserSignUpReq(
                nickName = "csKim",
                email = "csgmail.com",
                username = "csGood",
                password = "csGoodGoodGood",
                phoneNumber = "010-1111-1111"
            )

            mockMvc.perform(
                MockMvcRequestBuilders.post("/user")
                    .content(gson.toJson(userSignUpReq))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }

        "nickName 의 size 는 2~10 사이이다." {
            forAll(
                row("c"),
                row("asdpajpdjpajdpsadjpasjdpsaasdadas")
            ) {
                val userSignUpReq = UserSignUpReq(
                    nickName = it,
                    email = "cs@gmail.com",
                    username = "csGood",
                    password = "csGoodGoodGood",
                    phoneNumber = "010-1111-1111"
                )
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/user")
                        .content(gson.toJson(userSignUpReq))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest)
            }
        }

        "정상 case" {
            val userSignUpReq: UserSignUpReq = UserSignUpReq(
                nickName = "csGood",
                email = "cs@gmail.com",
                username = "csGood",
                password = "csGoodGoodGood",
                phoneNumber = "010-1111-1111"
            )

            mockMvc.perform(
                MockMvcRequestBuilders.post("/user")
                    .content(gson.toJson(userSignUpReq))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk)
        }
    }
}
