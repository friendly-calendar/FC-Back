package com.friendly.calendar.repository

import com.friendly.calendar.entity.User
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class UserRepositoryTest(
    @Autowired
    val userRepository: UserRepository,
) : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        "유저가 생성 시간이 자동으로 기록되야 합니다"{

            val newUser = User(name = "khkim", id = "kh", password = "1234")
            val savedUser = userRepository.save(newUser)
            assertNotNull(savedUser.createdAt)
        }

        "중복 유저 조회 쿼리가 정상적으로 실행되야 합니다"{
            val newUser =
                User(name = "khkim", id = "kh", password = "1234", email = "khkim@crscube.io", phoneNumber = "112")
            userRepository.save(newUser)

            val result =
                userRepository.existsByEmailOrPhoneNumberOrId(email = "khkim@crscube.io", phoneNumber = "112", id = "kh")
            assertTrue(result)
        }
    }
}