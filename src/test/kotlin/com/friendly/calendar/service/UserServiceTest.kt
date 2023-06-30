package com.friendly.calendar.service

import com.friendly.calendar.entity.User
import com.friendly.calendar.network.user.UserSignUpReq
import com.friendly.calendar.repository.UserRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import javax.persistence.EntityManager
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class UserServiceTest(
    @Autowired
    val userService: UserService,

    @Autowired
    val userRepository: UserRepository,
) : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        // 정상 흐름
        "유저 등록 API 호출하여 정상적으로 유저가 등록됩니다" {
            val newUser: UserSignUpReq = UserSignUpReq(
                nickName = "khkim",
                id = "kh",
                password = "1234",
                email = "khkim@gmail.com",
                phoneNumber = "010-1234-5678"
            )
            val saveTargetUserKey: Long = userService.signUp(newUser)
            val savedUser: Optional<User> = userRepository.findById(saveTargetUserKey)
            assertTrue(!savedUser.isEmpty)
        }

        // 중복이메일,아이디,핸드폰번호
        "유저 등록 API 호출 했을 때 RuntimeException 이 에러가 발생합니다" {
            val newUser = UserSignUpReq(
                nickName = "khkim",
                id = "kh",
                password = "1234",
                email = "khkim@gmail.com",
                phoneNumber = "010-1234-5678"
            )
            userService.signUp(newUser)

            Assertions.assertThatThrownBy {
                userService.signUp(newUser)
            }.isInstanceOf(RuntimeException::class.java)
        }

    }


}

